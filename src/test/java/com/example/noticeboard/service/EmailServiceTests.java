package com.example.noticeboard.service;

import com.example.noticeboard.config.EmailConfig;
import com.example.noticeboard.repository.RedisRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import javax.mail.internet.MimeMessage;

@SpringBootTest(classes = {EmailConfig.class, EmailServiceImpl.class})
@DisplayName("이메일 서비스 테슽트")
public class EmailServiceTests {

    @MockBean
    RedisRepository redisRepository;

    @Autowired
    private EmailService emailService;


    @Test
    @DisplayName("이메일을 전송한다.")
    public void sendMail() throws Exception {
        //given
        String receiver = "hw03198@gmail.com";
        //when
        emailService.sendMail(receiver);
        //then: 이메일 갔는지 확인
    }
}
