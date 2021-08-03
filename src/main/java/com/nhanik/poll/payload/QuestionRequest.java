package com.nhanik.poll.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QuestionRequest {
    private String questionText;
    private int numOfChoices;
    private List<String> choices;
}



