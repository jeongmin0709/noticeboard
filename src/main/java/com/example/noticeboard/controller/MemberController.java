package com.example.noticeboard.controller;

import com.example.noticeboard.dto.AuthCodeResultDTO;
import com.example.noticeboard.dto.memberdto.FindPasswordForm;
import com.example.noticeboard.dto.memberdto.FindUsernameForm;
import com.example.noticeboard.dto.memberdto.MemberSaveForm;
import com.example.noticeboard.security.dto.MemberDTO;
import com.example.noticeboard.service.EmailService;
import com.example.noticeboard.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final EmailService emailService;

    @GetMapping("/signup")
    @PreAuthorize("isAnonymous()")
    public String signup(Model model){
        log.info("회원가입 페이지 요청");
        model.addAttribute("memberDTO", new MemberSaveForm());
        return "signup";
    }

    @PostMapping("/members")
    @PreAuthorize("isAnonymous()")
    public String singUp(@Validated @ModelAttribute("memberDTO") MemberSaveForm memberSaveDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes){
        log.info("회원가입 요청");
        // 유효성 검사
        if(!memberSaveDTO.getPassword().equals(memberSaveDTO.getPasswordConfirm()))
            bindingResult.rejectValue("passwordConfirm","Equal");
        if(memberService.checkUsername(memberSaveDTO.getUsername()))
            bindingResult.rejectValue("username", "Duplication");
        AuthCodeResultDTO authCodeResultDTO = emailService.checkAuthCode(memberSaveDTO.getEmail(), memberSaveDTO.getCode());
        if(!authCodeResultDTO.getIsAuthentication()) bindingResult.rejectValue("code", authCodeResultDTO.getCode());

        if(bindingResult.hasErrors()){ return "signup"; }

        MemberDTO memberDTO = MemberDTO.builder()
                .username(memberSaveDTO.getUsername())
                .password(memberSaveDTO.getPassword())
                .email(memberSaveDTO.getEmail())
                .name(memberSaveDTO.getName())
                .build();
        memberService.signUp(memberDTO);
        redirectAttributes.addFlashAttribute("message","회원가입에 성공하였습니다.");
        return "redirect:/list";
    }



    @GetMapping("/loginForm")
    @PreAuthorize("isAnonymous()")
    public String loginForm(HttpServletRequest request, Model model){
        log.info("로그인 페이지 요청");
        String uri = request.getHeader("Referer");
        if (uri != null && !uri.contains("/loginForm")) {
            request.getSession().setAttribute("prevPage", uri);
        }
        return "loginForm";
    }

    @GetMapping("/userInfo")
    @PreAuthorize("isAuthenticated()")
    public String userInfo(@AuthenticationPrincipal MemberDTO memberDTO, Model model){
        log.info("유저 정보 페이지 요청: {}", memberDTO);
        model.addAttribute("memberDTO", memberDTO);
        return "userInfo";
    }


    @GetMapping("/find-username")
    @PreAuthorize("isAnonymous()")
    public String findUsernameForm(Model model) {
        log.info("아이디 찾기 페이지 요청");
        model.addAttribute("memberDTO", new FindUsernameForm());
        return "find-username";
    }


    @GetMapping("/find-password")
    @PreAuthorize("isAnonymous()")
    public String findPasswordForm(Model model){
        log.info("비밀번호 찾기 페이지 요청");
        model.addAttribute("memberDTO", new FindPasswordForm());
        return "find-password";
    }

    @PostMapping("/find-username")
    @PreAuthorize("isAnonymous()")
    public String findUsername(@Validated @ModelAttribute("memberDTO") FindUsernameForm findUsernameForm,
                                     Model model,
                                     BindingResult bindingResult){
        log.info("아이디 찾기 요청");
        AuthCodeResultDTO authCodeResultDTO = emailService.checkAuthCode(findUsernameForm.getEmail(), findUsernameForm.getCode());
        if(!authCodeResultDTO.getIsAuthentication()) bindingResult.rejectValue("code", authCodeResultDTO.getCode());
        if(bindingResult.hasErrors()){ return "find-username"; }
        List<Map<String, Object>> usernameList = memberService.findUsername(findUsernameForm.getEmail());
        model.addAttribute("usernameList", usernameList);
        return "result-find-username";
    }

    @PostMapping("/find-password")
    @PreAuthorize("isAnonymous()")
    public String findPassword(@Validated @ModelAttribute("memberDTO") FindPasswordForm findPasswordForm,
                               Model model,
                               BindingResult bindingResult){
        log.info("비밀번호 찾기 요청");
        //유효성 검사
        AuthCodeResultDTO authCodeResultDTO = emailService.checkAuthCode(findPasswordForm.getEmail(), findPasswordForm.getCode());
        if(!authCodeResultDTO.getIsAuthentication()) bindingResult.rejectValue("code", authCodeResultDTO.getCode());
        if(bindingResult.hasErrors()){ return "find-password"; }
        String tempPassword = memberService.findPassword(findPasswordForm.getUsername());
        model.addAttribute("tempPassword", tempPassword);
        return "result-find-password";
    }

}
