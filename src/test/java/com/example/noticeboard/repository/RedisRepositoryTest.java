package com.example.noticeboard.repository;

import com.example.noticeboard.entity.EmailAuthToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class RedisRepositoryTest {

    @Autowired RedisRepository redisRepository;

    @DisplayName("save")
    @Test
    public void saveTest(){

        //given
        EmailAuthToken emailAuthToken = EmailAuthToken.builder()
                .email("tjrwjdals7@naver.com")
                .code("123456")
                .expiration(60*30L)
                .build();
        //when
        redisRepository.save(emailAuthToken);

        //then
        Optional<EmailAuthToken> result = redisRepository.findById(emailAuthToken.getEmail());
        EmailAuthToken find = result.get();
        System.out.println(find);
        Assertions.assertAll(
                () -> Assertions.assertEquals(emailAuthToken.getEmail(), find.getEmail()),
                () -> Assertions.assertEquals(emailAuthToken.getCode(), find.getCode()),
                () -> Assertions.assertEquals(1800, find.getExpiration())
        );

    }

}
