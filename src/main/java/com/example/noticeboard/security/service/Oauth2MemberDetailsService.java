package com.example.noticeboard.security.service;

import com.example.noticeboard.dto.MemberDTO;
import com.example.noticeboard.entity.member.Member;
import com.example.noticeboard.entity.member.Role;
import com.example.noticeboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class Oauth2MemberDetailsService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        String clientName = userRequest.getClientRegistration().getClientName();
        OAuth2User auth2User = super.loadUser(userRequest);

        Member member = null;

        if(clientName.equals("Google")){
            String name = auth2User.getAttribute("name");
            String email = auth2User.getAttribute("email");
            member = saveSocialMember(name, email);

        }else if(clientName.equals("Naver")){
            Map<String, String> response = auth2User.getAttribute("response");
            String name = response.get("name");
            String email = response.get("email");
            member = saveSocialMember(name, email);

        }else if(clientName.equals("Kakao")){
            Map<String, String> properties = auth2User.getAttribute("properties");
            Map<String, String> kakaoAccount = auth2User.getAttribute("kakao_account");
            String name = properties.get("nickname");
            String email = kakaoAccount.get("email");
            member = saveSocialMember(name, email);
        }

        MemberDTO memberDTO = MemberDTO.builder()
                .username(member.getUsername())
                .name(member.getName())
                .email(member.getEmail())
                .fromSocial(member.isFromSocial())
                .password(member.getPassword())
                .RoleSet(member.getRoleSet())
                .attr(auth2User.getAttributes())
                .build();

        return memberDTO;
    }
    private Member saveSocialMember(String name, String email){
        Optional<Member> result = memberRepository.findByUsername(email, true);
        if(result.isPresent()) return result.get();
        Member member = Member.builder()
                .username(email)
                .name(name)
                .email(email)
                .fromSocial(true)
                .password(passwordEncoder.encode("1111"))
                .build();
        member.addRole(Role.ROLE_USER);
        memberRepository.save(member);
        return member;
    }
}
