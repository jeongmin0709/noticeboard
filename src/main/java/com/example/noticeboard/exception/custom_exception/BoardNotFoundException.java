package com.example.noticeboard.exception.custom_exception;

public class BoardNotFoundException extends RuntimeException{
    public BoardNotFoundException() {
        super();
    }

    public BoardNotFoundException(String message) {
        super(message);
    }

    public BoardNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
