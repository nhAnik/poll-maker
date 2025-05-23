package com.nhanik.poll.payload;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

public record QuestionRequest (
    @NotBlank String questionText,
    @Min(1) @Max(20) int numOfChoices,
    @NotNull
    @Size(min = 1) List<String> choices,
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime expiresAt
) {}
