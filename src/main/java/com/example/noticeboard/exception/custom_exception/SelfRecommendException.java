package com.example.noticeboard.exception.custom_exception;

import com.example.noticeboard.exception.ErrorCode;

public class SelfRecommendException extends CustomException {
    public SelfRecommendException(ErrorCode errorCode) {
        super(errorCode);
    }
}
