package com.example.noticeboard.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    //500번대 에러
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에러 입니다. 관리자에게 문의해 주세요."),

    //400번대 에러
    MESSAGING_ERROR(HttpStatus.BAD_REQUEST, "이메일 전송에 실패하였습니다."),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "입력값이 유효하지 않습니다."),
    AUTHENTICATION_ERROR(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    SELF_RECOMMEND(HttpStatus.FORBIDDEN, "자신의 글에는 추천할 수 없습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근이 거부되었습니다."),
    NOT_FOUND_BOARD(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),
    NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),
    NOT_FOUND_EMAIL(HttpStatus.NOT_FOUND, "이메일을 찾을 수 없습니다"),
    NOT_FOUND_FILE(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다."),
    DUPLICATE_RECOMMEND(HttpStatus.CONFLICT, "더 이상 추천을 할 수 없습니다.");

    private final HttpStatus status;
    private final String message;
}
