package com.taskmanager.exception;

public class NotExistException extends RuntimeException {
    public NotExistException(String message) {
        super(message);
    }
}
