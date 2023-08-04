package com.example.noticeboard.service;

import com.example.noticeboard.entity.member.Member;
import com.example.noticeboard.repository.MemberRepository;
import com.example.noticeboard.security.dto.MemberDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("멤버 서비스 테스트")
public class MemberServiceTests {

    @Mock
    MemberRepository memberRepository;

    @Spy
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @InjectMocks
    MemberServiceImpl memberService;

    @Test
    @DisplayName("회원가입을 한다.")
    public void singUpTest(){
        //give
        MemberDTO memberDTO = MemberDTO.builder()
                .username("testMember")
                .password("1111")
                .name("testName")
                .email("test@testEmail.com")
                .fromSocial(false)
                .build();
        //when
        String username = memberService.signUp(memberDTO);
        //then
        Assertions.assertThat(memberDTO.getUsername()).isEqualTo(username);
    }

    @Nested
    @DisplayName("비밀번호를 확인한다.")
    public class checkPasswordTest{
        @Test
        @DisplayName("다른 비밀번호를 확인한다.")
        public void checkDiffPasswordTest(){
            //given
            String username = "testMember";
            String password = "test1619132!";
            Member member = Member.builder().username("testMember12").password(passwordEncoder.encode("diffPassword123!")).build();
            when(memberRepository.findByUsername(username, false)).thenReturn(Optional.of(member));
            //when, then
            Assertions.assertThat(memberService.checkPassword(username, password)).isFalse();
        }
        @Test
        @DisplayName("같은 비밀번호를 확인한다.")
        public void checkSamePasswordTest(){
            //given
            String username = "testMember";
            String password = "test1619132!";
            Member member = Member.builder().username("testMember").password(passwordEncoder.encode("test1619132!")).build();
            when(memberRepository.findByUsername(username, false)).thenReturn(Optional.of(member));
            //when, then
            Assertions.assertThat(memberService.checkPassword(username, password)).isTrue();
        }
    }
}
