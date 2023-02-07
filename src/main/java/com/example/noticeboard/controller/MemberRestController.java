package com.example.noticeboard.controller;

import com.example.noticeboard.dto.memberdto.FindUsernameDTO;
import com.example.noticeboard.security.dto.MemberDTO;
import com.example.noticeboard.service.EmailService;
import com.example.noticeboard.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MemberRestController {

    private final MemberService memberService;
    private final EmailService emailService;

    @GetMapping("/check/username")
    public Boolean checkUserName(String username){
        log.info("아이디 중복 확인 요청");
        return memberService.checkUsername(username);
    }


    @GetMapping("/check/password")
    public Boolean checkPassword(String username, String password){
        log.info("비밀번호 확인 요청");
        return memberService.checkPassword(username, password);
    }

    @GetMapping("/check/code")
    @ResponseBody
    public Boolean checkCode(String email, String code){
        log.info("인증번호 확인 요청");
        log.info("email:{}, code:{}", email, code);
        return emailService.getAuthCode(email).equals(code);
    }

    @PostMapping("/email")
    public ResponseEntity<String> sendEmail(String email) throws MessagingException, UnsupportedEncodingException {
        log.info("회원가입 이메일 전송 요청");
        if(memberService.checkEmail(email)) return new ResponseEntity<>("이미 사용중인 이메일입니다.", HttpStatus.BAD_REQUEST);
        emailService.sendMail(email);
        return new ResponseEntity<>("인증번호를 발송했습니다. (유효시간: 30분)", HttpStatus.OK);
    }

    @PostMapping("/username/email")
    public ResponseEntity<String> findUsernameSendEmail(String name, String email) throws MessagingException, UnsupportedEncodingException {
        log.info("아이디 찾기 이메일 전송 요청");
        if(!memberService.checkNameAndEmail(name, email)) return new ResponseEntity<>("메일을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
        emailService.sendMail(email);
        return new ResponseEntity<>("인증번호를 발송했습니다. (유효시간: 30분)", HttpStatus.OK);
    }

    @PostMapping("/password/email")
    public ResponseEntity<String> findPasswordSendEmail(String username, String email) throws MessagingException, UnsupportedEncodingException {
        log.info("비밀번호 찾기 이메일 전송 요청");
        if(!memberService.checkUsernameAndEmail(username, email)) return new ResponseEntity<>("이메일을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
        emailService.sendMail(email);
        return new ResponseEntity<>("인증번호를 발송했습니다. (유효시간: 30분)", HttpStatus.OK);
    }
}
