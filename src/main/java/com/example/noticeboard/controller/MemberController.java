package com.example.noticeboard.controller;

import com.example.noticeboard.dto.MemberDTO;
import com.example.noticeboard.service.EmailService;
import com.example.noticeboard.service.MemberDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/noticeboard")
@Log4j2
@RequiredArgsConstructor
public class MemberController {

    private final MemberDetailsService memberDetailsService;
    private final EmailService emailService;

    @GetMapping("/signup")
    public void singup(){
    }

    @PostMapping("/signup")
    public String singUp(MemberDTO memberDTO){
        log.info("회원가입을 요청하였습니다.");
        memberDetailsService.signUp(memberDTO);
        return "redirect:/noticeboard/list";
    }

    @PostMapping("/emailcheck")
    @ResponseBody
    public int mailCheck(String email) throws Exception {
        log.info("email인증요청: "+email);
        int code = emailService.sendMail(email);
        return code;
    }

    @PostMapping("/checkusername")
    @ResponseBody
    public boolean checkUserNmae(String username){
        log.info("checkUserNmae" + username);
        try {
            memberDetailsService.loadUserByUsername(username);
        }catch(UsernameNotFoundException e) {
            return true;
        }
        return false;
    }

    @GetMapping("/login")
    public void login(){

    }
}
