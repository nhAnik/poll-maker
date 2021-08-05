package com.nhanik.poll.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QuestionRequest {
    @NotBlank
    private String questionText;

    @Min(1)
    @Max(20)
    private int numOfChoices;

    @NotNull
    @Size(min = 1)
    private List<String> choices;
}



