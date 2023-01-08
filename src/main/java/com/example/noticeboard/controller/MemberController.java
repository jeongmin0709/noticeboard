package com.example.noticeboard.controller;

import com.example.noticeboard.dto.MemberDTO;
import com.example.noticeboard.dto.PageRequestDTO;
import com.example.noticeboard.service.EmailService;
import com.example.noticeboard.security.service.MemberDetailsService;
import com.example.noticeboard.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.Banner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@Log4j2
@RequiredArgsConstructor
@PreAuthorize("isAnonymous()")
public class MemberController {

    private final MemberService memberService;
    private final MemberDetailsService memberDetailsService;
    private final EmailService emailService;

    @GetMapping("/")
    public String layout(){
        return "redirect:/list";
    }
    @GetMapping("/signup")
    public void singup(PageRequestDTO pageRequestDTO){
    }

    @PostMapping("/signup")
    public String singUp(MemberDTO memberDTO, RedirectAttributes redirectAttributes){
        log.info("회원가입 요청");
        boolean result = memberService.signUp(memberDTO);
        if(result) redirectAttributes.addFlashAttribute("msg", "회원가입에 성공하였습니다.");
        else redirectAttributes.addFlashAttribute("msg", "이미 아이디가 존재합니다.");
        return "redirect:/list";
    }

    @PostMapping("/check/email")
    @ResponseBody
    public int mailCheck(String email) throws Exception {
        log.info("이메일 인증요청: ");
        int code = emailService.sendMail(email);
        return code;
    }

    @PostMapping("/check/username")
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

    @GetMapping("/login")
    public void loginForm(PageRequestDTO pageRequestDTO,
                          @RequestParam(value = "error", required = false)String error,
                          HttpServletRequest request,
                          Model model){
        String referrer = request.getHeader("Referer");
        request.getSession().setAttribute("prevPage", referrer);
        log.info("로그인 화면 요청");
        model.addAttribute("error", error);
    }


    @GetMapping("/find/username")
    public void findUsernameForm(PageRequestDTO pageRequestDTO){

    }
    @PostMapping("/find/username")
    @ResponseBody
    public ResponseEntity<Map<String, String>> findUsername(@RequestBody MemberDTO memberDTO){
        log.info("아이디 찾기 요청");
        Map<String, String> map = memberService.findUsername(memberDTO);
        if(map.size() == 0){
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/find/password")
    public void findPasswordForm(PageRequestDTO pageRequestDTO){
    }


    @PostMapping("/find/password")
    @ResponseBody
    public ResponseEntity<Map<String, String>> findPassword(@RequestBody MemberDTO memberDTO){
        log.info("비밀번호 찾기 요청");
        Map<String, String> map = memberService.findPassword(memberDTO);
        if(map.size() == 0){
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
