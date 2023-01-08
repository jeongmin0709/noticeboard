package com.example.noticeboard.config;

import com.example.noticeboard.security.entrypoint.AjaxAuthenticationEntryPoint;
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
                .authenticationEntryPoint(authenticationEntryPoint());


        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder(){ return new BCryptPasswordEncoder(); }

    @Bean
    LoginSuccessHandler loginSuccessHandler(){return new LoginSuccessHandler();}

    @Bean
    LoginFailureHandler loginFailureHandler(){return new LoginFailureHandler();}

    @Bean
    AjaxAuthenticationEntryPoint authenticationEntryPoint(){return new AjaxAuthenticationEntryPoint("/login");}

}
