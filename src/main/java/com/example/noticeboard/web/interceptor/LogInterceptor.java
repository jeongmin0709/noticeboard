package com.example.noticeboard.web.interceptor;

import com.example.noticeboard.security.dto.MemberDTO;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
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
    // loginHandler 에서는 addFlashAttribute 를 사용하지 못해서 직접 구현
    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            ModelAndView modelAndView
    ) throws Exception {
        if(modelAndView == null) return;
        HttpSession session = request.getSession();
        String message = (String)session.getAttribute("message");

        if(message != null){
            if(modelAndView != null)modelAndView.addObject("message", message);
            session.removeAttribute("message");
        }

        String errorMessage = (String) session.getAttribute("errorMessage");
        if(errorMessage != null){
            if(modelAndView != null)modelAndView.addObject("errorMessage", errorMessage);
            session.removeAttribute("errorMessage");
        }
    }

}
