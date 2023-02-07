package com.example.noticeboard.service;

import com.example.noticeboard.entity.EmailAuthToken;
import com.example.noticeboard.exception.custom_exception.ExpiredCodeException;
import com.example.noticeboard.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.Random;

@RequiredArgsConstructor
@Service
@Slf4j
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender emailSender;
    private final RedisRepository redisRepository;

    @Value("${AdminMail.id}")
    private String sender;


    private MimeMessage createMessage(String receiver, int code) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, receiver);//보내는 대상
        message.setSubject("Notice Board Service 이메일 인증");//제목

        String msg="";
        msg+= "<div style='margin:20px;'>";
        msg+= "<h1> 안녕하세요. Notice Board Service입니다. </h1>";
        msg+= "<br>";
        msg+= "<p>아래 코드를 복사해 입력해주세요<p>";
        msg+= "<br>";
        msg+= "<p>감사합니다.<p>";
        msg+= "<br>";
        msg+= "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msg+= "<h3 style='color:blue;'>인증 코드입니다.</h3>";
        msg+= "<div style='font-size:130%'>";
        msg+= "CODE : <strong>";
        msg+= code+"</strong><div><br/> ";
        msg+= "</div>";
        message.setText(msg, "utf-8", "html");//내용
        message.setFrom(new InternetAddress(sender,"NoticeBoard"));//보내는 사람

        return message;
    }
    
    private int createCode(){
        Random generator = new Random();
        generator.setSeed(System.currentTimeMillis());
        return generator.nextInt(10000000) % 1000000;
    }

    @Override
    public void sendMail(String receiver) throws MessagingException, UnsupportedEncodingException {
        int code = createCode();
        MimeMessage message = createMessage(receiver, code);
        emailSender.send(message);
        EmailAuthToken emailAuthToken = EmailAuthToken.builder()
                .email(receiver)
                .code(Integer.toString(code))
                .expiration(60*30L)
                .build();
        redisRepository.save(emailAuthToken);
    }

    @Override
    public String getAuthCode(String receiver) {
        Optional<EmailAuthToken> result = redisRepository.findById(receiver);
        if(result.isEmpty()) throw new ExpiredCodeException();
        EmailAuthToken emailAuthToken = result.get();
        log.info("result code: {}", emailAuthToken.getCode());
        return emailAuthToken.getCode();
    }
}
