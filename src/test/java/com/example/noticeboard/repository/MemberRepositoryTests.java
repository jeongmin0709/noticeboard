package com.example.noticeboard.repository;

import com.example.noticeboard.entity.member.Gender;
import com.example.noticeboard.entity.member.Member;
import com.example.noticeboard.entity.member.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.stream.IntStream;

@SpringBootTest
public class MemberRepositoryTests {

    @Autowired MemberRepository memberRepository;

    @Test
    public void insertMmeberTest(){
        IntStream.rangeClosed(1, 100).forEach(i->{
            Member member = Member.builder()
                    .nickname("member..."+ i)
                    .email("member"+i+"gmail.com")
                    .name("memberName..."+ i)
                    .birth_date(LocalDate.now())
                    .fromSocial(false)
                    .role(Role.USER)
                    .gender(i%2==0?Gender.MAN:Gender.WOMEN)
                    .build();
            memberRepository.save(member);
        });
    }
}
