package com.example.noticeboard.config;

import com.example.noticeboard.security.handler.CustomAccessDeniedHandler;
import com.example.noticeboard.security.handler.CustomAuthenticationEntryPoint;
import com.example.noticeboard.security.handler.LoginFailureHandler;
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
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    MemberDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //csrf 토큰 검사 비활성화
        http.csrf().disable()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .failureHandler(loginFailureHandler())
                .successHandler(loginSuccessHandler())
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/list")
                .and()
                .oauth2Login()
                .successHandler(loginSuccessHandler())
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandler())
                .and()
                .rememberMe() // rememberMe 기능 작동함
                .rememberMeParameter("rememberMe") // default: remember-me, checkbox 등의 이름과 맞춰야함
                .tokenValiditySeconds(3600) // 쿠키의 만료시간 설정(초), default: 14일
                .alwaysRemember(false) // 사용자가 체크박스를 활성화하지 않아도 항상 실행, default: false
                .userDetailsService(userDetailsService); // 기능을 사용할 때 사용자 정보가 필요함. 반드시 이 설정 필요함.


        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder(){ return new BCryptPasswordEncoder(); }

    @Bean
    LoginSuccessHandler loginSuccessHandler(){return new LoginSuccessHandler();}

    @Bean
    LoginFailureHandler loginFailureHandler(){return new LoginFailureHandler();}

    @Bean
    CustomAuthenticationEntryPoint authenticationEntryPoint(){return new CustomAuthenticationEntryPoint("/login");}

    @Bean
    CustomAccessDeniedHandler accessDeniedHandler(){return new CustomAccessDeniedHandler("error/403");}


}
