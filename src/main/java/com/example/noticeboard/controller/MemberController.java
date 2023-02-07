package com.example.noticeboard.controller;

import com.example.noticeboard.dto.memberdto.FindPasswordDTO;
import com.example.noticeboard.dto.memberdto.FindUsernameDTO;
import com.example.noticeboard.dto.memberdto.MemberSaveDTO;
import com.example.noticeboard.exception.custom_exception.ExpiredCodeException;
import com.example.noticeboard.security.dto.MemberDTO;
import com.example.noticeboard.service.EmailService;
import com.example.noticeboard.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final EmailService emailService;

    @GetMapping("/signup")
    public String signup(Model model){
        log.info("회원가입 페이지 요청");
        model.addAttribute("memberDTO", new MemberSaveDTO());
        return "/signup";
    }

    @PostMapping("/signup")
    public String singUp(@Validated @ModelAttribute("memberDTO")MemberSaveDTO memberSaveDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes){
        log.info("회원가입 요청");

        // 유효성 검사
        if(!memberSaveDTO.getPassword().equals(memberSaveDTO.getPasswordConfirm()))
            bindingResult.rejectValue("passwordConfirm","Equal");
        if(memberService.checkUsername(memberSaveDTO.getUsername()))
            bindingResult.rejectValue("username", "Duplication");
        if(!memberSaveDTO.getCode().equals(emailService.getAuthCode(memberSaveDTO.getEmail())))
            bindingResult.rejectValue("code", "Equal");
        if(bindingResult.hasErrors()){ return "/signup"; }

        MemberDTO memberDTO = MemberDTO.builder()
                .username(memberSaveDTO.getUsername())
                .password(memberSaveDTO.getPassword())
                .email(memberSaveDTO.getEmail())
                .name(memberSaveDTO.getName())
                .build();
        memberService.signUp(memberDTO);
        redirectAttributes.addFlashAttribute("msg","회원가입에 성공하였습니다.");
        return "redirect:/list";
    }



    @GetMapping("/login")
    public String loginForm(@RequestParam(value = "error", required = false)Integer error,
                          HttpServletRequest request,
                          Model model){
        log.info("로그인 페이지 요청");
        String referrer = request.getHeader("Referer");
        request.getSession().setAttribute("prevPage", referrer);
        model.addAttribute("error", error);
        return "/login";
    }


    @GetMapping("/userInfo")
    public String userInfo(@AuthenticationPrincipal MemberDTO memberDTO, Model model){
        log.info("유저 정보 페이지 요청");
        model.addAttribute("memberDTO", memberDTO);
        return "/userInfo";
    }

    @PostMapping("/modify/user")
    public String modifyUser(@Validated @ModelAttribute("memberDTO") MemberSaveDTO memberSaveDTO,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){ return "/userInfo"; }
        log.info("유저정보 수정 요청");
        MemberDTO memberDTO = MemberDTO.builder()
                .username(memberSaveDTO.getUsername())
                .password(memberSaveDTO.getPassword())
                .email(memberSaveDTO.getEmail())
                .name(memberSaveDTO.getName())
                .build();
        memberService.modify(memberDTO);
        redirectAttributes.addFlashAttribute("msg", "수정이 완료되었습니다.");
        return "redirect:/list";
    }

    @GetMapping("/find/username")
    public String findUsernameForm(Model model) {
        log.info("아이디 찾기 페이지 요청");
        model.addAttribute("memberDTO", new FindUsernameDTO());
        return "/find/username";
    }


    @GetMapping("/find/password")
    public String findPasswordForm(Model model){
        log.info("비밀번호 찾기 페이지 요청");
        model.addAttribute("memberDTO", new FindPasswordDTO());
        return "/find/password";
    }

    @PostMapping("/find/username")
    public String findUsername(@Validated @ModelAttribute("memberDTO") FindUsernameDTO findUsernameDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes){
        log.info("아이디 찾기 요청");

        try {
            if(!findUsernameDTO.getCode().equals(emailService.getAuthCode(findUsernameDTO.getEmail())))
                bindingResult.rejectValue("code", "Equal");
        }catch (ExpiredCodeException e){
            bindingResult.rejectValue("code", "Expire");
        }
        if(bindingResult.hasErrors()) return "/find/username";

        String username = memberService.findUsername(findUsernameDTO.getEmail(), findUsernameDTO.getName());
        redirectAttributes.addFlashAttribute("msg", "아이디: " + username);
        return "redirect:/list";
    }

    @PostMapping("/find/password")
    public String findPassword(@Validated @ModelAttribute("memberDTO")FindPasswordDTO findPasswordDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes){
        log.info("비밀번호 찾기 요청");

        //유효성 검사
        try {
            if(!findPasswordDTO.getCode().equals(emailService.getAuthCode(findPasswordDTO.getEmail())))
                bindingResult.rejectValue("code", "Equal");
        }catch (ExpiredCodeException e) {
            bindingResult.rejectValue("code", "Expire");
        }
        if(bindingResult.hasErrors()) return "/find/password";

        String password = memberService.findPassword(findPasswordDTO.getEmail(), findPasswordDTO.getUsername());
        redirectAttributes.addFlashAttribute("msg", "임시 비밀번호: " + password);
        return "redirect:/list";
    }
}
