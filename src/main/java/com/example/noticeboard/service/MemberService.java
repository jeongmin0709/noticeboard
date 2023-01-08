package com.example.noticeboard.service;

import com.example.noticeboard.dto.MemberDTO;
import com.example.noticeboard.entity.member.Member;
import com.example.noticeboard.entity.member.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface MemberService {

    boolean signUp(MemberDTO memberDTO);
    Map<String, String> findUsername(MemberDTO memberDTO);
    Map<String, String> findPassword(MemberDTO memberDTO);

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
