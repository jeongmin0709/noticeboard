package com.example.noticeboard.config;

import com.example.noticeboard.security.handler.LoginSuccessHandler;
import com.example.noticeboard.security.service.MemberDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

    @Autowired
    MemberDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.formLogin().loginPage("/login") // 로그인 페이지 설정
                .loginProcessingUrl("/login") // 로그인 post 설정
                .successHandler(new LoginSuccessHandler());

        http.oauth2Login()
                .successHandler(new LoginSuccessHandler());

        http.rememberMe() // rememberMe 기능 작동함
                .rememberMeParameter("rememberMe") // default: remember-me, checkbox 등의 이름과 맞춰야함
                .tokenValiditySeconds(3600) // 쿠키의 만료시간 설정(초), default: 14일
                .alwaysRemember(false) // 사용자가 체크박스를 활성화하지 않아도 항상 실행, default: false
                .userDetailsService(userDetailsService);

        http.logout().logoutUrl("/logout")
            .logoutSuccessUrl("/list");

        http.csrf().disable();


        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
