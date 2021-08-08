package com.nhanik.poll.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhanik.poll.payload.AuthenticationRequest;
import com.nhanik.poll.payload.AuthenticationResponse;
import com.nhanik.poll.payload.RegistrationRequest;
import com.nhanik.poll.services.JwtTokenService;
import com.nhanik.poll.services.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenService jwtTokenService;

    @Test
    @DisplayName("Successful registration with valid inputs")
    public void whenRegisterWithValidInput_thenReturns200AndResponse() throws Exception {
        RegistrationRequest request = new RegistrationRequest(
                "John", "Doe", "abc@test.com", "password"
        );
        final MockHttpServletRequestBuilder registerUserRequest = post("/register")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request));
        mockMvc.perform(registerUserRequest)
                .andExpect(status().isOk())
                .andExpect(content().string("User created"));
        ArgumentCaptor<RegistrationRequest> requestCaptor =
                ArgumentCaptor.forClass(RegistrationRequest.class);
        verify(userService, times(1)).createNewUser(requestCaptor.capture());
    }

    @Test
    @DisplayName("Failed registration with blank email and invalid password")
    public void whenRegisterWithInvalidInput1_thenReturns400AndErrorResponse() throws Exception {
        RegistrationRequest request = new RegistrationRequest(
                "John", "Doe","  ", "pass"
        );
        final MockHttpServletRequestBuilder registerUserRequest = post("/register")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request));
        mockMvc.perform(registerUserRequest)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Validation failure")))
                .andExpect(jsonPath("$.validationErrors", hasSize(2)))
                .andExpect(jsonPath("$.validationErrors[*].field",
                        containsInAnyOrder("email", "password")))
                .andExpect(jsonPath("$.validationErrors[*].message",
                        containsInAnyOrder("must not be blank", "size must be between 8 and 15")));
        ArgumentCaptor<RegistrationRequest> requestCaptor =
                ArgumentCaptor.forClass(RegistrationRequest.class);
        verify(userService, never()).createNewUser(requestCaptor.capture());
    }

    @Test
    @DisplayName("Failed registration with null input")
    public void whenRegisterWithInvalidInput2_thenReturns400AndErrorResponse() throws Exception {
        RegistrationRequest request = RegistrationRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("abc@test.com")
                .build();
        final MockHttpServletRequestBuilder registerUserRequest = post("/register")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request));
        mockMvc.perform(registerUserRequest)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Validation failure")))
                .andExpect(jsonPath("$.validationErrors", hasSize(1)))
                .andExpect(jsonPath("$.validationErrors[0].field", is("password")))
                .andExpect(jsonPath("$.validationErrors[0].message", is("must not be blank")));
    }

    @Test
    @DisplayName("Successful login with valid inputs")
    public void whenLoginWithValidInput_thenReturns200AndResponse() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest(
                "abc@test.com", "password"
        );
        AuthenticationResponse response = new AuthenticationResponse("jwt.jwt.jwt");
        final MockHttpServletRequestBuilder loginUserRequest = post("/login")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request));
        when(userService.authenticateUser(ArgumentMatchers.any(AuthenticationRequest.class)))
                .thenReturn(response);

        mockMvc.perform(loginUserRequest)
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.jwt", is("jwt.jwt.jwt")));

        ArgumentCaptor<AuthenticationRequest> requestCaptor =
                ArgumentCaptor.forClass(AuthenticationRequest.class);
        verify(userService, times(1)).authenticateUser(requestCaptor.capture());
    }

    @Test
    @DisplayName("Failed login with too small password")
    public void whenLoginWithInvalidInput_thenReturns400AndErrorResponse() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest(
                "abc@test.com", "pass"
        );
        final MockHttpServletRequestBuilder loginUserRequest = post("/login")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request));
        mockMvc.perform(loginUserRequest)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Validation failure")))
                .andExpect(jsonPath("$.validationErrors", hasSize(1)))
                .andExpect(jsonPath("$.validationErrors[0].field", is("password")))
                .andExpect(jsonPath("$.validationErrors[0].message",
                        is("size must be between 8 and 15")));

        ArgumentCaptor<AuthenticationRequest> requestCaptor =
                ArgumentCaptor.forClass(AuthenticationRequest.class);
        verify(userService, never()).authenticateUser(requestCaptor.capture());
    }
}
