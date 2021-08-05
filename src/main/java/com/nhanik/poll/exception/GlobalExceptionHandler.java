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
import java.util.List;
import java.util.stream.Collectors;

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
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());
        ValidationExceptionResponse response = new ValidationExceptionResponse(
                status.value(), "Validation failure", LocalDateTime.now(), errors
        );
        return new ResponseEntity<>(response, headers, status);
    }

    private ResponseEntity<ExceptionResponse> sendErrorResponse(Exception ex, HttpStatus httpStatus) {
        ExceptionResponse response = new ExceptionResponse(
                httpStatus.value(), ex.getMessage(), LocalDateTime.now()
        );
        return new ResponseEntity<>(response, httpStatus);
    }
}
