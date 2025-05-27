package com.nhanik.poll.exception;

public class InvalidVoteException extends RuntimeException {

    public InvalidVoteException(String message) {
        super(message);
    }
}
