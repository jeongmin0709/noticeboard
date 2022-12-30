package com.example.noticeboard.service;

import com.example.noticeboard.dto.MemberDTO;
import com.example.noticeboard.entity.member.Member;
import com.example.noticeboard.entity.member.Role;
import com.example.noticeboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Username: "+username);
        Optional<Member> result = memberRepository.findByUsername(username);
        if(result.isEmpty()){
            throw new UsernameNotFoundException("chek Email or Social");
        }
        Member member = result.get();
        log.info("-------------------------------");
        log.info(member);


        // entity to DTO
        MemberDTO memberDTO = MemberDTO.builder()
                .username(member.getUsername())
                .password(member.getPassword())
                .email(member.getEmail())
                .RoleSet(member.getRoleSet())
                .fromSocial(member.isFromSocial())
                .build();
        return memberDTO;
    }
    public void signUp(MemberDTO memberDTO){
        //DTOtoEntity
        Member member = Member.builder()
                .username(memberDTO.getUsername())
                .password(passwordEncoder.encode(memberDTO.getPassword()))
                .email(memberDTO.getEmail())
                .fromSocial(false)
                .build();
        member.addRole(Role.ROLE_USER);
        memberRepository.save(member);
    }
}
