package com.example.noticeboard.service;

import com.example.noticeboard.dto.memberdto.ModifyEmailForm;
import com.example.noticeboard.dto.memberdto.ModifyPasswordForm;
import com.example.noticeboard.entity.member.Member;
import com.example.noticeboard.entity.member.Role;
import com.example.noticeboard.security.dto.MemberDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface MemberService {

    String signUp(MemberDTO memberDTO);
    boolean checkUsername(String username);
    boolean checkEmail(String email);
    boolean checkUsernameAndEmail(String username, String email);
    boolean checkPassword(String username, String password);
    void modifyEmail(ModifyEmailForm modifyEmailForm);
    void modifyPassword(ModifyPasswordForm modifyPasswordForm);
    List<Map<String, Object>> findUsername(String email);
    String findPassword(String userName);

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
