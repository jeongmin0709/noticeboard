package com.example.noticeboard.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTests {

    @Autowired
    EmailService emailService;

    @Test
    public void sendMail() throws Exception {
        String receiver = "hw03198@gmail.com";
        emailService.sendMail(receiver);
    }
}
