package com.nhanik.poll.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ValidationExceptionResponse extends ExceptionResponse {
    List<String> validationErrors;

    public ValidationExceptionResponse(
            int status, String message, LocalDateTime dateTime, List<String> validationErrors) {
        super(status, message, dateTime);
        this.validationErrors = validationErrors;
    }


}
