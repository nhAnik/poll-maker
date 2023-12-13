package com.nhanik.poll.payload;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape;

public record Response<T>(
    String status,
    int code,

    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    LocalDateTime timestamp,

    @JsonInclude(Include.NON_NULL)
    String message,

    @JsonInclude(Include.NON_NULL)
    T data
) {

    public static <T> Response<T> withSuccessMsg(String message) {
        return new Response<T>(
            "success",
            HttpStatus.OK.value(),
            LocalDateTime.now(),
            message,
            null
        );
    }

    public static <T> Response<T> withErrorMsg(HttpStatus status, String message) {
        return new Response<T>(
            "error",
            status.value(),
            LocalDateTime.now(),
            message,
            null
        );
    }

    public static <T> Response<T> withData(T data) {
        return new Response<T>(
            "success",
            HttpStatus.OK.value(),
            LocalDateTime.now(),
            null,
            data
        );
    }
}