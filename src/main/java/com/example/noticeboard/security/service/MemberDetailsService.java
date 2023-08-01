package com.example.noticeboard.security.service;

import com.example.noticeboard.entity.member.Member;
import com.example.noticeboard.repository.MemberRepository;
import com.example.noticeboard.security.dto.MemberDTO;
import com.example.noticeboard.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> result = memberRepository.findByUsername(username, false);
        if(result.isEmpty()){
            throw new UsernameNotFoundException("chek Email or Social");
        }
        Member member = result.get();

        // entity to DTO
        MemberDTO memberDTO = MemberDTO.builder()
                .username(member.getUsername())
                .name(member.getName())
                .password(member.getPassword())
                .email(member.getEmail())
                .RoleSet(member.getRoleSet())
                .fromSocial(member.isFromSocial())
                .build();
        return memberDTO;
    }

}
