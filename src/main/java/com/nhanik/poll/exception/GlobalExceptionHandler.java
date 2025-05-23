package com.nhanik.poll.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.nhanik.poll.payload.Response;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<?> handleNotFound(Exception ex) {
        return sendErrorResponse(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            RegistrationFailureException.class,
            ChoiceRemoveFailureException.class,
            MultipleVoteException.class,
            ConstraintViolationException.class,
            ExpiredPollException.class
    })
    public ResponseEntity<?> handleBadRequest(Exception ex) {
        return sendErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        
        record ValidationError(String field, String message) {}

        var errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ValidationError(error.getField(), error.getDefaultMessage()))
                .toList();
        var response = new Response<>(
            "error",
            status.value(),
            LocalDateTime.now(),
            "Validation failure",
            errors
        );
        return new ResponseEntity<>(response, status);
    }

    private ResponseEntity<?> sendErrorResponse(Exception ex, HttpStatus httpStatus) {
        return new ResponseEntity<>(Response.withErrorMsg(httpStatus, ex.getMessage()), httpStatus);
    }
}
