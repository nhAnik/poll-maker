package com.nhanik.poll.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ValidationExceptionResponse extends ExceptionResponse {
    List<ValidationError> validationErrors;

    public ValidationExceptionResponse(
            LocalDateTime dateTime, int status, String message) {
        super(dateTime, status, message);
        validationErrors = new ArrayList<>();
    }

    public void addValidationError(String field, String message) {
        validationErrors.add(new ValidationError(field, message));
    }

    @Getter
    @AllArgsConstructor
    static class ValidationError {
        private String field;
        private String message;
    }
}
