package com.example.noticeboard.security.handler;

import com.example.noticeboard.exception.ErrorCode;
import com.example.noticeboard.exception.ExceptionDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final String accessDeniedPage;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        String ajaxHeader = request.getHeader("X-Requested-With");
        boolean isAjax = "XMLHttpRequest".equals(ajaxHeader);
        if(isAjax){ // ajax 요청이라면
            log.info("권한이 없는 사용자 rest 요청");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json"); //
            response.setCharacterEncoding("UTF-8");

            ExceptionDTO exceptionDTO = makeExceptionDTO(ErrorCode.ACCESS_DENIED);
            String string = objectMapper.writeValueAsString(exceptionDTO);
            response.getWriter().write(string);
        } else {
            log.info("권한이 없는 사용자 요청");
            response.sendRedirect(accessDeniedPage);
        }
    }

    private ExceptionDTO makeExceptionDTO(ErrorCode errorCode) {
        return ExceptionDTO.builder()
                .code(errorCode.name())
                .status(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .build();
    }
}
