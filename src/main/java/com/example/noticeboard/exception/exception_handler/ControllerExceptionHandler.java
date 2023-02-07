package com.example.noticeboard.exception.exception_handler;

import com.example.noticeboard.exception.custom_exception.BoardNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice(annotations = Controller.class)
public class ControllerExceptionHandler {

    @ExceptionHandler({BoardNotFoundException.class, UsernameNotFoundException.class})
    public String notFoundExHandler(Exception exception){
        String message = exception.getMessage();
        StackTraceElement[] stackTrace = exception.getStackTrace();
        log.error("{} : {}",stackTrace[0].toString(), message);
        return "/error/404";
    }

}
