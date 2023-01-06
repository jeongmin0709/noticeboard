package com.example.noticeboard.service;

import com.example.noticeboard.dto.MemberDTO;
import com.example.noticeboard.entity.member.Member;
import com.example.noticeboard.entity.member.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public interface MemberService {

    void signUp(MemberDTO memberDTO);
    String findUsername(MemberDTO memberDTO);

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
