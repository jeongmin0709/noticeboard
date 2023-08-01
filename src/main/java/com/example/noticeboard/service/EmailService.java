package com.example.noticeboard.service;



import com.example.noticeboard.dto.AuthCodeResultDTO;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public interface EmailService {
    void sendMail(String receiver)throws MessagingException, UnsupportedEncodingException;
    AuthCodeResultDTO checkAuthCode(String email, String code);
}
