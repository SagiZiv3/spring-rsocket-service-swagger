package com.example.reactiveservice.logic.db;

public class NotFoundException extends RuntimeException {
    private static final long serialVersionUID = -4677790877314785096L;

    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}