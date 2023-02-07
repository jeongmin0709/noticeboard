package com.example.noticeboard.exception.exception_handler;

import com.example.noticeboard.exception.custom_exception.CommentNotFoundException;
import com.example.noticeboard.exception.custom_exception.ExpiredCodeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.mail.MessagingException;
import java.nio.file.NoSuchFileException;

@RestControllerAdvice(annotations = RestController.class)
@Slf4j
public class RestControllerExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String CodeExHandler(ExpiredCodeException exception){
        String message = exception.getMessage();
        StackTraceElement[] stackTrace = exception.getStackTrace();
        log.error("{} : {}",stackTrace[0].toString(), message);;
        return "인증번호가 만료되었습니다.";
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String commentExHandler(CommentNotFoundException exception){
        String message = exception.getMessage();
        StackTraceElement[] stackTrace = exception.getStackTrace();
        log.error("{} : {}",stackTrace[0].toString(), message);
        return "존재하지 않는 댓글입니다.";
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String memberExHandler(UsernameNotFoundException exception){
        String message = exception.getMessage();
        StackTraceElement[] stackTrace = exception.getStackTrace();
        log.error("{} : {}",stackTrace[0].toString(), message);
        return "존재하지 않는 회원입니다.";
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String imageExHandler(NoSuchFileException exception){
        String message = exception.getMessage();
        StackTraceElement[] stackTrace = exception.getStackTrace();
        log.error("{} : {}",stackTrace[0].toString(), message);
        return message+ " 존재하지 않는 파일입니다.";
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String emailSendExHandler(MessagingException exception){
        String message = exception.getMessage();
        StackTraceElement[] stackTrace = exception.getStackTrace();
        log.error("{} : {}",stackTrace[0].toString(), message);
        return "이메일 전송에 실패하였습니다.";
    }

}
