package com.example.noticeboard.web.interceptor;

import com.example.noticeboard.security.dto.MemberDTO;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);
        if(principal instanceof String){
            MDC.put("memberId", (String) principal);
        }else {
            MemberDTO memberDTO = (MemberDTO)principal;
            MDC.put("memberId", memberDTO.getUsername());
        }
        return true;
    }
}
