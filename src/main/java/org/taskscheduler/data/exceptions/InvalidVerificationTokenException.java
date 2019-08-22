package org.taskscheduler.domain.exceptions;

public class InvalidVerificationTokenException extends RuntimeException{
    public InvalidVerificationTokenException(String message) {
        super(message);
    }
}
