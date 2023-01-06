package com.example.noticeboard.service;

import com.example.noticeboard.dto.MemberDTO;
import com.example.noticeboard.entity.member.Member;
import com.example.noticeboard.entity.member.Role;
import com.example.noticeboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final PasswordEncoder passwordEncoder;

    private final MemberRepository memberRepository;

    public void signUp(MemberDTO memberDTO){
        //DTOtoEntity
        Member member = dtoToEntity(memberDTO, passwordEncoder);
        memberRepository.save(member);
    }

    @Override
    public String findUsername(MemberDTO memberDTO) {
        String name = memberDTO.getName();
        String email = memberDTO.getEmail();
        Optional<Member> result = memberRepository.findByNameAndEmail(name, email);
        if (result.isPresent()){
            Member member = result.get();
            return member.getUsername();
        }
        return null;
    }
}
