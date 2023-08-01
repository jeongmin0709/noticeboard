package com.example.noticeboard.repository;

import com.example.noticeboard.DummyDataProvider;
import com.example.noticeboard.config.JpaConfig;
import com.example.noticeboard.entity.member.Member;
import com.example.noticeboard.entity.member.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@DataJpaTest
@Import({JpaConfig.class, DummyDataProvider.class})
public class MemberRepositoryTests {

    @Autowired
    MemberRepository memberRepository;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @Test
    @DisplayName("유저 저장")
    public void saveMember(){
        //given
        Member member = Member.builder()
                .username("Member")
                .email("Member"+"@sample.org")
                .name("name")
                .password(passwordEncoder.encode("1111"))
                .fromSocial(false)
                .build();
        member.addRole(Role.ROLE_USER);
        //when, then
        Member save = memberRepository.save(member);
    }

    @Test
    @DisplayName("유저 조회")
    public void findMemberByUsernameAmdFromSocialTest(){
        //given
        Member member = Member.builder().username("user1").build();
        //when
        Optional<Member> result = memberRepository.findByUsername(member.getUsername(), member.isFromSocial());
        Member findMember = result.get();
        //then
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember.isFromSocial()).isFalse();
    }

    @Test
    @DisplayName("저장된 유저를 email로 조회한다.")
    public void findMemberByEmailAndFromSocialTest(){
        //given
        Member member = Member.builder().username("user1").email("user1@gmail.com").build();
        //when
        List<Member> result = memberRepository.findByEmail(member.getEmail(), member.isFromSocial());
        Member findMember = result.get(0); //
        //then
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember.isFromSocial()).isFalse();
        Assertions.assertThat(findMember.getEmail()).isEqualTo(member.getEmail());
    }
    @Test
    @DisplayName("저장된 유저가 존재하는지 id로 확인한다.")
    public void existByUsernameTest(){
        //given
        String SavedUsername = "user1";
        String unSavedUsername = "unSavedUsername";
        //when
        boolean savedResult = memberRepository.existsByUsername(SavedUsername);
        boolean unSavedResult = memberRepository.existsByUsername(unSavedUsername);
        //then
        Assertions.assertThat(savedResult).isTrue();
        Assertions.assertThat(unSavedResult).isFalse();
    }
    @Test
    @DisplayName("저장된 유저가 존재하는지 Email로 확인한다.")
    public void existByEmailTest(){
        //given
        String savedUEmail = "user1@gmail.com";
        String unSavedUEmail = "unSavedUser@gmail.com";
        //when
        boolean savedResult = memberRepository.existsByEmail(savedUEmail);
        boolean unSavedResult = memberRepository.existsByEmail(unSavedUEmail);
        //then
        Assertions.assertThat(savedResult).isTrue();
        Assertions.assertThat(unSavedResult).isFalse();
    }

    @Test
    @DisplayName("저장된 유저가 존재하는지 id와 Email로 확인한다.")
    public void existByUsernameAndEmailTest(){
        //given
        String savedUsername = "user1";
        String savedEmail = "user1@gmail.com";
        String unSavedUsername = "unSavedUsername";
        String unSavedUEmail = "unSavedEmail";
        //when
        boolean savedResult = memberRepository.existsByUsernameAndEmail(savedUsername, savedEmail);
        boolean unSavedUsernameResult = memberRepository.existsByUsernameAndEmail(unSavedUsername, savedEmail);
        boolean unSavedEmailResult = memberRepository.existsByUsernameAndEmail(savedUsername, unSavedUEmail);
        boolean unSavedResult = memberRepository.existsByUsernameAndEmail(unSavedUsername, unSavedUEmail);
        //then
        Assertions.assertThat(savedResult).isTrue();
        Assertions.assertThat(unSavedUsernameResult).isFalse();
        Assertions.assertThat(unSavedEmailResult).isFalse();
        Assertions.assertThat(unSavedResult).isFalse();
    }

    @Test
    @DisplayName("저장된 유저가 존재하는지 Name과 Email로 확인한다.")
    public void existByNameAndEmailTest(){
        //given
        String savedName = "name...1";
        String savedEmail = "user1@gmail.com";
        String unSavedName = "unSavedName";
        String unSavedUEmail = "unSavedUser@gmail.com";
        //when
        boolean savedResult = memberRepository.existsByNameAndEmail(savedName, savedEmail);
        boolean unSavedNameResult = memberRepository.existsByNameAndEmail(unSavedName, savedEmail);
        boolean unSavedEmailResult = memberRepository.existsByNameAndEmail(savedName, unSavedUEmail);
        boolean unSavedResult = memberRepository.existsByNameAndEmail(unSavedName, unSavedUEmail);
        //then
        Assertions.assertThat(savedResult).isTrue();
        Assertions.assertThat(unSavedNameResult).isFalse();
        Assertions.assertThat(unSavedEmailResult).isFalse();
        Assertions.assertThat(unSavedResult).isFalse();
    }
}
