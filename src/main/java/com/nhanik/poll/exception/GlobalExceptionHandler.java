package com.nhanik.poll.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<ExceptionResponse> handleNotFound(Exception ex) {
        return sendErrorResponse(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({RegistrationFailureException.class, ConstraintViolationException.class})
    public ResponseEntity<ExceptionResponse> handleBadRequest(Exception ex) {
        return sendErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ValidationExceptionResponse response = new ValidationExceptionResponse(
                LocalDateTime.now(), status.value(), "Validation failure"
        );
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error -> response.addValidationError(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(response, headers, status);
    }

    private ResponseEntity<ExceptionResponse> sendErrorResponse(Exception ex, HttpStatus httpStatus) {
        ExceptionResponse response = new ExceptionResponse(
                LocalDateTime.now(), httpStatus.value(), ex.getMessage()
        );
        return new ResponseEntity<>(response, httpStatus);
    }
}
