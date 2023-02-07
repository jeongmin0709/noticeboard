package com.example.noticeboard.security.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    private final String loginForm;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException{
        log.info("AuthenticationEntryPoint");
        String ajaxHeader = request.getHeader("X-Requested-With");
        boolean isAjax = "XMLHttpRequest".equals(ajaxHeader);
        if(isAjax){
            log.info("unauthorized Ajax request");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            log.info("unauthorized request!!");
            response.sendRedirect(loginForm);
        }
    }
    @Override
    public void afterPropertiesSet() {
        super.setRealmName("pilseong");
        super.afterPropertiesSet();
    }
}
