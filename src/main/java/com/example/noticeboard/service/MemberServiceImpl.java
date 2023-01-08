package com.example.noticeboard.service;

import com.example.noticeboard.dto.MemberDTO;
import com.example.noticeboard.entity.member.Member;
import com.example.noticeboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService{

    private final PasswordEncoder passwordEncoder;

    private final MemberRepository memberRepository;

    private final EmailService emailService;

    public boolean signUp(MemberDTO memberDTO){

        //이름과 이메일로 조회해서 이미 회원가입이 되어있으면 회원가입 안함.
        String name = memberDTO.getName();
        String email = memberDTO.getEmail();
        Optional<Member> result = memberRepository.findByNameAndEmail(name, email);
        if(result.isPresent()) return false;

        Member member = dtoToEntity(memberDTO, passwordEncoder);
        memberRepository.save(member);
        return true;
    }

    @Override
    public Map<String, String> findUsername(MemberDTO memberDTO) {
        String name = memberDTO.getName();
        String email = memberDTO.getEmail();
        Map<String, String> map = new HashMap<>();
        Optional<Member> result = memberRepository.findByNameAndEmail(name, email);
        if (result.isPresent()){
            int code = 0;
            try {
                code = emailService.sendMail(email);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Member member = result.get();
            map.put("code", Integer.toString(code));
            map.put("username", member.getUsername());
        }
        return map;
    }

    @Override
    public Map<String, String> findPassword(MemberDTO memberDTO) {
        String username = memberDTO.getUsername();
        String email = memberDTO.getEmail();
        Map<String, String> map = new HashMap<>();
        Optional<Member> result = memberRepository.findByUsername(username, false);
        Member member = result.get();
        if(member.getEmail().equals(email)){
            int code = 0;
            try {
                code = emailService.sendMail(email);
            }catch (Exception e) {
                e.printStackTrace();
            }
            String tempPassword = getRamdomPassword(10);
            member.changePassword(passwordEncoder.encode(tempPassword));
            map.put("code", Integer.toString(code));
            map.put("tempPassword",tempPassword);
        }
        return map;
    }
    private String getRamdomPassword(int size) {
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
