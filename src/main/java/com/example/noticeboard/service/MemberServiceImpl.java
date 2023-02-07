package com.example.noticeboard.service;

import com.example.noticeboard.entity.member.Member;
import com.example.noticeboard.repository.MemberRepository;
import com.example.noticeboard.security.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void signUp(MemberDTO memberDTO) {
        Member member = dtoToEntity(memberDTO, passwordEncoder);
        memberRepository.save(member);
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
    public boolean checkNameAndEmail(String name, String email) {
        return memberRepository.existsByNameAndEmail(name, email);
    }


    @Override
    public boolean checkPassword(String username, String password) {
        Optional<Member> result = memberRepository.findByUsername(username, false);
        if(result.isEmpty()) throw new UsernameNotFoundException("계정을 찾을 수 없습니다.");
        Member member = result.get();
        return passwordEncoder.matches(password, member.getPassword());
    }

    @Override
    public void modify(MemberDTO memberDTO) {
        Optional<Member> result = memberRepository.findByUsername(memberDTO.getUsername(), false);
        if(result.isEmpty()) throw new UsernameNotFoundException("계정을 찾을 수 없습니다.");
        Member member = result.get();
        member.setEmail(memberDTO.getEmail());
        member.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
    }

    @Override
    public String findUsername(String email, String name) {
        Optional<Member> result = memberRepository.findByEmail(email, false);
        if(result.isEmpty()) throw new UsernameNotFoundException("계정을 찾을 수 없습니다.");
        Member member = result.get();
        return member.getUsername();
    }


    @Override
    public String findPassword(String email, String username) {
        Optional<Member> result = memberRepository.findByEmail(email, false);
        if(result.isEmpty()) throw new UsernameNotFoundException("계정을 찾을 수 없습니다.");
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
