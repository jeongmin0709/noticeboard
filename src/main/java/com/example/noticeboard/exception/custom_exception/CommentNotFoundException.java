package com.example.noticeboard.exception.custom_exception;

import com.example.noticeboard.exception.ErrorCode;

public class CommentNotFoundException extends CustomException{
    public CommentNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
