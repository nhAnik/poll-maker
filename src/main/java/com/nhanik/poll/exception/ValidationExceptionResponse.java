package com.nhanik.poll.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record ValidationExceptionResponse (
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
        LocalDateTime timestamp,
        int status,
        String message,
        List<ValidationError> validationErrors
) {
    record ValidationError(String field, String message) {}

    public void addValidationError(String field, String message) {
        this.validationErrors.add(new ValidationError(field, message));
    }
}
