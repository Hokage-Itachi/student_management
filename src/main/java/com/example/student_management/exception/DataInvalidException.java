package com.example.student_management.exception;

public class DataInvalidException extends RuntimeException {
    public DataInvalidException(String message) {
        super(message);
    }

    public DataInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
