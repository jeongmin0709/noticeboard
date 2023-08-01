package com.example.noticeboard.security.handler;

import com.example.noticeboard.exception.ErrorCode;
import com.example.noticeboard.exception.ExceptionDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final String loginForm;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException{

        String ajaxHeader = request.getHeader("X-Requested-With");
        boolean isAjax = "XMLHttpRequest".equals(ajaxHeader);
        if(isAjax){
            log.info("가입되지 않은 사용자 Rest 요청");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            ExceptionDTO exceptionDTO = makeExceptionDTO(ErrorCode.AUTHENTICATION_ERROR);
            String string = objectMapper.writeValueAsString(exceptionDTO);
            response.getWriter().write(string);
        } else {
            log.info("가입되지 않은 사용자 요청");
            response.sendRedirect(loginForm);
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
