package com.example.noticeboard.exception.custom_exception;

import com.example.noticeboard.exception.ErrorCode;

public class DuplicateException extends CustomException{
    public DuplicateException(ErrorCode errorCode) { super(errorCode); }
}
