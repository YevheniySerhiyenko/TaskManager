package com.taskmanager.exception;

public class NotEmptyException extends RuntimeException {
    public NotEmptyException(String message) {
        super(message);
    }
}
