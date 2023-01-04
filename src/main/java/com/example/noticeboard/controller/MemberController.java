package com.example.noticeboard.controller;

import com.example.noticeboard.dto.MemberDTO;
import com.example.noticeboard.dto.PageRequestDTO;
import com.example.noticeboard.service.EmailService;
import com.example.noticeboard.security.MemberDetailsService;
import com.example.noticeboard.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/noticeboard")
@Log4j2
@RequiredArgsConstructor
@PreAuthorize("permitAll()")
public class MemberController {

    private final MemberService memberService;
    private final MemberDetailsService memberDetailsService;
    private final EmailService emailService;

    @GetMapping("/signup")
    public void singup(PageRequestDTO pageRequestDTO){

    }

    @PostMapping("/signup")
    public String singUp(MemberDTO memberDTO){
        log.info("회원가입 요청");
        memberService.signUp(memberDTO);
        return "redirect:/noticeboard/list";
    }

    @PostMapping("/emailcheck")
    @ResponseBody
    public int mailCheck(String email) throws Exception {
        log.info("이메일 인증요청: ");
        int code = emailService.sendMail(email);
        return code;
    }

    @PostMapping("/checkusername")
    @ResponseBody
    public boolean checkUserNmae(String username){
        log.info("아이디 중복 확인 요청");
        try {
            memberDetailsService.loadUserByUsername(username);
        }catch(UsernameNotFoundException e) {
            return true;
        }
        return false;
    }

    @GetMapping("/loginForm")
    public void loginForm(HttpServletRequest request, PageRequestDTO pageRequestDTO){
        log.info("로그인 화면 요청");
        String uri = request.getHeader("Referer");
        request.getSession().setAttribute("prevPage", uri);
    }

    @GetMapping("/findUsername")
    public void findUsername(PageRequestDTO pageRequestDTO){

    }
}
