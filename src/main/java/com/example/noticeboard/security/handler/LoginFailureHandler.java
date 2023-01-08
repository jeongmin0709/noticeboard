package com.example.noticeboard.security.handler;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@Log4j2
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        log.info("loginFauiluerHandler");
        RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
        String defaultFailureUrl = "/login";
        String error = "";
        log.info(exception);
        if (exception instanceof BadCredentialsException) {
                error = "1"; //아이디 또는 비밀번호를 잘못 입력했습니다.
        } else if (exception instanceof UsernameNotFoundException) {
            error = "2";//아이디 또는 비밀번호를 잘못 입력했습니다.
        } else if (exception instanceof DisabledException) {
            error = "3"; //이메일 인증을 해주세요.
        } else if (exception instanceof SessionAuthenticationException) {
            error = "4";//중복 로그인입니다.
        }
        // 로그인 페이지로 다시 포워딩
        setDefaultFailureUrl("/login?error="+error);

        super.onAuthenticationFailure(request,response,exception);
    }
}
