package com.nhanik.poll.exception;

public class ChoiceRemoveFailureException extends RuntimeException {

    public ChoiceRemoveFailureException(Long qid) {
        super("Question with id " + qid + " should have at least 2 choices");
    }
}
