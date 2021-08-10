package com.nhanik.poll.exception;

public class MultipleVoteException extends RuntimeException {

    public MultipleVoteException() {
        super("User has already cast a vote in this question");
    }
}
