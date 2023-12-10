package com.nhanik.poll.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhanik.poll.models.User;
import com.nhanik.poll.payload.AuthenticationRequest;
import com.nhanik.poll.payload.RegistrationRequest;
import com.nhanik.poll.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration-test")
@Testcontainers
@ContextConfiguration(initializers = AuthControllerIntegrationTest.Initializer.class)
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Container
    private static final PostgreSQLContainer postgresqlContainer =
            new PostgreSQLContainer("postgres:alpine")
                    .withDatabaseName("poll_db")
                    .withUsername("postgres")
                    .withPassword("password");

    static class Initializer implements
            ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext context) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgresqlContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgresqlContainer.getUsername(),
                    "spring.datasource.password=" + postgresqlContainer.getPassword()
            ).applyTo(context.getEnvironment());
        }
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Successful registration integration test")
    public void checkRegisterWorksWithValidInputs() throws Exception {
        RegistrationRequest request =
                new RegistrationRequest("John", "Doe", "abc@test.com", "password"
        );
        final MockHttpServletRequestBuilder registerUserRequest =
                getServletRequest("/register", objectMapper.writeValueAsString(request));
        mockMvc.perform(registerUserRequest)
                .andExpect(status().isOk());
        User user = userRepository.findByEmail(request.email()).get();
        assertNotNull(user);
        assertEquals(request.email(), user.getUsername());
    }

    @Test
    @DisplayName("Failed multiple registration integration test")
    public void checkMultipleRegistrationFailsWithSameEmail() throws Exception {
        RegistrationRequest request = new RegistrationRequest(
                "John", "Doe", "abc@test.com", "password"
        );
        final String failMessage = "User with email " + request.email() + " already exists";
        final MockHttpServletRequestBuilder registerUserRequest =
                getServletRequest("/register", objectMapper.writeValueAsString(request));
        mockMvc.perform(registerUserRequest)
                .andExpect(status().isOk());
        // trying to register again
        mockMvc.perform(registerUserRequest)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(failMessage)));
    }

    @Test
    @DisplayName("Successful login after registration integration test")
    public void checkLoginWorksWithValidInputs() throws Exception {
        RegistrationRequest registrationRequest = new RegistrationRequest(
                "John", "Doe", "abc@test.com", "password"
        );
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(
                "abc@test.com", "password"
        );
        final MockHttpServletRequestBuilder registerUserRequest =
                getServletRequest("/register", objectMapper.writeValueAsString(registrationRequest));
        mockMvc.perform(registerUserRequest)
                .andExpect(status().isOk());
        final MockHttpServletRequestBuilder loginUserRequest =
                getServletRequest("/login", objectMapper.writeValueAsString(authenticationRequest));
        mockMvc.perform(loginUserRequest)
                .andExpect(status().isOk());
        Optional<User> user = userRepository.findByEmail(authenticationRequest.email());
        assertTrue(user.isPresent());
        assertEquals(authenticationRequest.email(), user.get().getUsername());
    }

    @Test
    @DisplayName("Failed login integration test")
    public void checkLoginFailsWithInvalidInputs() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest(
                "abc@test.com", "password"
        );
        final MockHttpServletRequestBuilder loginUserRequest = getServletRequest(
                "/login", objectMapper.writeValueAsString(request)
        );
        mockMvc.perform(loginUserRequest)
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("Bad credentials")));
    }

    private MockHttpServletRequestBuilder getServletRequest(String url, String content) {
        return post(url)
                .contentType(APPLICATION_JSON)
                .content(content);
    }
}
