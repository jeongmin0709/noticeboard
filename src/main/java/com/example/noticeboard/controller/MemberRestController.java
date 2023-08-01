package com.example.noticeboard.controller;

import com.example.noticeboard.dto.AuthCodeResultDTO;
import com.example.noticeboard.dto.memberdto.ModifyEmailForm;
import com.example.noticeboard.dto.memberdto.ModifyPasswordForm;
import com.example.noticeboard.exception.ErrorCode;
import com.example.noticeboard.exception.custom_exception.EmailNotFoundException;
import com.example.noticeboard.service.EmailService;
import com.example.noticeboard.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MemberRestController {

    private final MemberService memberService;
    private final EmailService emailService;

    @GetMapping("/code/check")
    @PreAuthorize("permitAll()")
    public AuthCodeResultDTO checkCode(String email, String code){
        log.info("인증번호 확인 요청");
        return emailService.checkAuthCode(email, code);
    }

    @PostMapping("/email")
    @PreAuthorize("permitAll()")
    public String sendEmail(String email) throws MessagingException, UnsupportedEncodingException {
        log.info("이메일 전송 요청: " + email);
        emailService.sendMail(email);
        return "인증번호가 발송되었습니다. (유효시간: 30분)";
    }

    @PostMapping("/email/find-username")
    @PreAuthorize("isAnonymous()")
    public String sendEmailForFindUsername(String email) throws MessagingException, UnsupportedEncodingException {
        log.info("아이디 찾기 이메일 전송 요청: " + email);
        if(!memberService.checkEmail(email)) throw new EmailNotFoundException(ErrorCode.NOT_FOUND_EMAIL);
        emailService.sendMail(email);
        return "인증번호가 발송되었습니다. (유효시간: 30분)";
    }

    @PostMapping("/email/find-password")
    @PreAuthorize("isAnonymous()")
    public String sendEmailForFindPassword(String username, String email) throws MessagingException, UnsupportedEncodingException {
        log.info("비밀번호 찾기 이메일 전송 요청: " + email);
        if(!memberService.checkUsernameAndEmail(username, email)) throw new EmailNotFoundException(ErrorCode.NOT_FOUND_EMAIL);
        emailService.sendMail(email);
        return "인증번호가 발송되었습니다. (유효시간: 30분)";
    }

    @GetMapping("/members/username/check")
    @PreAuthorize("permitAll()")
    public Boolean checkUsername(String username){
        log.info("아이디 확인 요청");
        return memberService.checkUsername(username);
    }


    @GetMapping("/members/password/check")
    @PreAuthorize("isAuthenticated() and #username == authentication.name")
    public Boolean checkPassword(String username, String password){
        log.info("비밀번호 확인 요청");
        return memberService.checkPassword(username, password);
    }


    @PatchMapping("/members/email")
    @PreAuthorize("isAuthenticated() and #modifyEmailForm.username == authentication.name")
    public String modifyEmail(@RequestBody @Validated ModifyEmailForm modifyEmailForm, BindingResult bindingResult) throws Exception {
        log.info("이메일 변경 요청");
        AuthCodeResultDTO authCodeResultDTO = emailService.checkAuthCode(modifyEmailForm.getNewEmail(), modifyEmailForm.getCode());
        if(!authCodeResultDTO.getIsAuthentication()) bindingResult.rejectValue("code", authCodeResultDTO.getCode());
        if(bindingResult.hasErrors()) throw new BindException(bindingResult);

        memberService.modifyEmail(modifyEmailForm);
        return "이메일이 변경되었습니다.";
    }

    @PatchMapping("/members/password")
    @PreAuthorize("isAuthenticated() and #modifyPasswordForm.username == authentication.name")  // 비밀번호 변경 요청 클라이언트가 본인인지 확인
    public String modifyPassword(@RequestBody @Validated ModifyPasswordForm modifyPasswordForm, BindingResult bindingResult) throws Exception {
        log.info("비밀번호 변경 요청");
        if(!modifyPasswordForm.getNewPassword().equals(modifyPasswordForm.getNewPasswordConfirm())) bindingResult.rejectValue("newPasswordConfirm", "Equal");
        if(bindingResult.hasErrors()) throw new BindException(bindingResult);

        memberService.modifyPassword(modifyPasswordForm);
        return "비밀번호가 변경되었습니다.";
    }

}
