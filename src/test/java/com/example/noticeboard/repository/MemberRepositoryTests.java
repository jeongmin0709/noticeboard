package com.example.noticeboard.repository;

import com.example.noticeboard.entity.member.Member;
import com.example.noticeboard.entity.member.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.stream.IntStream;

@SpringBootTest
public class MemberRepositoryTests {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void insertMmeberTest(){
        IntStream.rangeClosed(1, 100).forEach(i->{
            Member member = Member.builder()
                    .username("Member"+ i)
                    .email("Member"+i+"@sample.org")
                    .password(passwordEncoder.encode("1111"))
                    .fromSocial(false)
                    .build();
            member.addRole(Role.ROLE_USER);
            memberRepository.save(member);
        });
    }
}
