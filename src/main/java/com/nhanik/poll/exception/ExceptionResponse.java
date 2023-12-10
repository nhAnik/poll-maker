package com.nhanik.poll.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape;

public record ExceptionResponse (
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    LocalDateTime timestamp,
    int status,
    String message
) {}
