package com.example.noticeboard.exception.custom_exception;

public class ExpiredCodeException extends RuntimeException{
    public ExpiredCodeException() { }
    public ExpiredCodeException(String message) {
        super(message);
    }
}
