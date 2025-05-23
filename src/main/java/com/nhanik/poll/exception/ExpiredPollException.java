package com.nhanik.poll.exception;

public class ExpiredPollException extends RuntimeException{

    public ExpiredPollException(Long questionId) {
        super("Poll with id " + questionId + " has expired");
    }

    public ExpiredPollException(String message) {
        super(message);
    }
}
