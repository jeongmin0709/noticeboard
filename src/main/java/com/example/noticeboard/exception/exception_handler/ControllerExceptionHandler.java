package com.example.noticeboard.exception.exception_handler;

import com.example.noticeboard.controller.BoardController;
import com.example.noticeboard.controller.MemberController;
import com.example.noticeboard.exception.custom_exception.AccessDeniedException;
import com.example.noticeboard.exception.custom_exception.BoardNotfoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice(assignableTypes = {BoardController.class, MemberController.class})
public class ControllerExceptionHandler {

    @ExceptionHandler
    public String BoardNotFoundExHandler(BoardNotfoundException exception){
        log.warn(exception.getErrorCode().getMessage());
        return "/error/404";
    }
    @ExceptionHandler
    public String AccessDeniedExHandler(AccessDeniedException exception){
        log.warn(exception.getErrorCode().getMessage());
        return "/error/403";
    }
}
