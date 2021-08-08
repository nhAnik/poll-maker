package com.nhanik.poll.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhanik.poll.models.Choice;
import com.nhanik.poll.models.Question;
import com.nhanik.poll.models.User;
import com.nhanik.poll.services.JwtTokenService;
import com.nhanik.poll.services.QuestionService;
import com.nhanik.poll.services.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QuestionController.class)
class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private QuestionService questionService;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenService jwtTokenService;

    @Test
    @WithMockUser
    @DisplayName("Get response with valid questionId")
    public void whenValidQuesId_thenReturn200AndResponse() throws Exception {
        User user = new User(
                1L, "John", "Doe", "abc@test.com", "password");
        List<Choice> choices = new ArrayList<>();
        Question question = new Question(1L, "Favourite JS framework?", choices, user);
        Choice choice1 = new Choice(1L, "React", 0, question);
        Choice choice2 = new Choice(2L, "Vue", 0, question);
        question.addChoice(choice1);
        question.addChoice(choice2);

        final MockHttpServletRequestBuilder getQuestion = get("/polls/{qid}", 1L);
        when(questionService.getPollQuestion(1L))
                .thenReturn(question);
        mockMvc.perform(getQuestion)
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.questionId", is(1)))
                .andExpect(jsonPath("$.questionText", is(question.getQuestionText())))
                .andExpect(jsonPath("$.choices", hasSize(2)))
                .andExpect(jsonPath("$.choices[*].choiceId", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$.choices[*].choiceText",
                        containsInAnyOrder(choice1.getChoiceText(), choice2.getChoiceText())));
    }
}
