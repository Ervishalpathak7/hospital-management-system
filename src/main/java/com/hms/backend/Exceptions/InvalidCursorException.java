package com.hms.backend.Exceptions;

public class InvalidCursorException extends RuntimeException {
    public InvalidCursorException() {
        super("Invalid or malformed cursor");
    }
}