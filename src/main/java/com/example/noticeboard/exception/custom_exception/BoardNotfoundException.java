package com.example.noticeboard.exception.custom_exception;

import com.example.noticeboard.exception.ErrorCode;

public class BoardNotfoundException extends CustomException{
    public BoardNotfoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
