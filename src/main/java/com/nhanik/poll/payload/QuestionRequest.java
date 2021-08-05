package com.nhanik.poll.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QuestionRequest {
    @NotBlank
    private String questionText;

    @Min(0)
    @Max(20)
    private int numOfChoices;

    private List<String> choices;
}



