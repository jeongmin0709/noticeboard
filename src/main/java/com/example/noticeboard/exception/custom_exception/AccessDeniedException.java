package com.example.noticeboard.exception.custom_exception;

import com.example.noticeboard.exception.ErrorCode;

public class AccessDeniedException extends CustomException{
    public AccessDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
