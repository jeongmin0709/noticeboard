package com.example.noticeboard.service;


import org.springframework.data.util.Pair;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface EmailService {
    void sendMail(String receiver)throws MessagingException, UnsupportedEncodingException;
    String getAuthCode(String receiver);
}
