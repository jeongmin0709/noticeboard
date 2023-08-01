package com.example.noticeboard.exception.custom_exception;

import com.example.noticeboard.exception.ErrorCode;

public class EmailNotFoundException extends CustomException{
    public EmailNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
