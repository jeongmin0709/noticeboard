package com.example.noticeboard.security.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class LoginSuccessHandler implements AuthenticationSuccessHandler{

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        String url = "/list";

        // 강제 인터셉트 당했을 경우의 데이터
        RequestCache requestCache = new HttpSessionRequestCache();
        SavedRequest savedRequest = requestCache.getRequest(request, response);

        //로그인 버튼을 누르거나 rest 요청으로 리다이렉트 돼었을 경우의 데이터
        String prevPage = (String) request.getSession().getAttribute("prevPage");

        // 메모리 누수 방지
        if (prevPage != null) {
            request.getSession().removeAttribute("prevPage");
        }

        // null이 아니라면 강제 인터셉트 당했다는 것
        if (savedRequest != null) {
            url = savedRequest.getRedirectUrl();
        } else if(prevPage != null && !prevPage.equals("")) {
            url = prevPage;
        }

        request.getSession().setAttribute("message", "로그인이 성공하였습니다.");
        response.sendRedirect(url);

    }
}
