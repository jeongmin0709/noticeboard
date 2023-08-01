package com.example.noticeboard.service;

import com.example.noticeboard.dto.memberdto.ModifyEmailForm;
import com.example.noticeboard.dto.memberdto.ModifyPasswordForm;
import com.example.noticeboard.entity.member.Member;
import com.example.noticeboard.repository.MemberRepository;
import com.example.noticeboard.security.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String signUp(MemberDTO memberDTO) {
        Member member = dtoToEntity(memberDTO, passwordEncoder);
        memberRepository.save(member);
        return member.getUsername();
    }

    @Override
    public boolean checkUsername(String username) {
        return memberRepository.existsByUsername(username);
    }

    @Override
    public boolean checkEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    @Override
    public boolean checkUsernameAndEmail(String username, String email) {
        return memberRepository.existsByUsernameAndEmail(username, email);
    }

    @Override
    public boolean checkPassword(String username, String password) {
        Optional<Member> result = memberRepository.findByUsername(username, false);
        Member member = result.get();
        return passwordEncoder.matches(password, member.getPassword());
    }

    @Override
    public void modifyEmail(ModifyEmailForm modifyEmailForm){
        Optional<Member> result = memberRepository.findByUsername(modifyEmailForm.getUsername(), false);
        Member member = result.get();
        member.setEmail(modifyEmailForm.getNewEmail());
    }

    @Override
    public void modifyPassword(ModifyPasswordForm modifyPasswordForm){
        Optional<Member> result = memberRepository.findByUsername(modifyPasswordForm.getUsername(), false);
        Member member = result.get();
        member.setPassword(passwordEncoder.encode(modifyPasswordForm.getNewPassword()));
    }

    @Override
    public List<Map<String, Object>> findUsername(String email) {
        List<Member> memberList = memberRepository.findByEmail(email, false);
        return memberList.stream().map(member -> {
            Map<String, Object> map = new HashMap<>();
            map.put("username", member.getUsername());
            map.put("createDate", member.getCreateDate());
            return map;
        }).collect(Collectors.toList());

    }


    @Override
    public String findPassword(String username) {
        Optional<Member> result = memberRepository.findByUsername(username, false);
        Member member = result.get();
        String tempPassword = makeRandomPassword(12);
        member.setPassword(passwordEncoder.encode(tempPassword));
        return tempPassword;
    }

    private String makeRandomPassword(int size) {
        char[] charSet = new char[] {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                '!', '@', '#', '$', '%', '^', '&' };

        StringBuffer sb = new StringBuffer();
        SecureRandom sr = new SecureRandom();
        sr.setSeed(new Date().getTime());

        int idx = 0;
        int len = charSet.length;
        for (int i=0; i<size; i++) {
            // idx = (int) (len * Math.random());
            idx = sr.nextInt(len);    // 강력한 난수를 발생시키기 위해 SecureRandom을 사용한다.
            sb.append(charSet[idx]);
        }
        return sb.toString();
    }
}
