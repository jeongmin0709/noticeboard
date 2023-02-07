package com.example.noticeboard.service;

import com.example.noticeboard.entity.member.Member;
import com.example.noticeboard.entity.member.Role;
import com.example.noticeboard.security.dto.MemberDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.Map;

@Service
public interface MemberService {

    void signUp(MemberDTO memberDTO);
    boolean checkUsername(String username);
    boolean checkEmail(String email);
    boolean checkUsernameAndEmail(String username, String email);
    boolean checkNameAndEmail(String name, String email);
    boolean checkPassword(String username, String password);
    void modify(MemberDTO memberDTO);
    String findUsername(String email, String name);
    String findPassword(String email, String userName);

    default Member dtoToEntity(MemberDTO memberDTO , PasswordEncoder passwordEncoder){
        Member member = Member.builder()
                .username(memberDTO.getUsername())
                .password(passwordEncoder.encode(memberDTO.getPassword()))
                .name(memberDTO.getName())
                .email(memberDTO.getEmail())
                .fromSocial(false)
                .build();
            member.addRole(Role.ROLE_USER);
        return member;
    }
}
