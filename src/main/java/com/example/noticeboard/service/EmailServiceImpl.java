package com.example.noticeboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender emailSender;

    @Value("${AdminMail.id}")
    private String sender;

    private int code;

    private MimeMessage createMessage(String receiver)throws Exception{
        MimeMessage  message = emailSender.createMimeMessage();

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
    
    private void createCode(){
        Random generator = new Random();
        generator.setSeed(System.currentTimeMillis());
        code = generator.nextInt(10000000) % 1000000;
    }

    @Override
    public int sendMail(String receiver) throws Exception {

        createCode();
        MimeMessage message = createMessage(receiver);

        try {
            emailSender.send(message);

        }catch (MailException e){
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
        return code;
    }
}
