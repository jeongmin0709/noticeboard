package com.example.noticeboard.config;

import com.example.noticeboard.security.filter.JwtAuthenticationFilter;
import com.example.noticeboard.security.filter.JwtAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

public class JwtSecurityConfig {

    //jwt 방식
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .addFilter(jwtAuthenticationFilter(authenticationManager))
                .addFilter(jwtAuthorizationFilter(authenticationManager));
        return http.build();
    }

    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    JwtAuthenticationFilter jwtAuthenticationFilter(AuthenticationManager authenticationManager){
        return new JwtAuthenticationFilter(authenticationManager);
    }


    JwtAuthorizationFilter jwtAuthorizationFilter(AuthenticationManager authenticationManager){
        return new JwtAuthorizationFilter(authenticationManager);
    }


    PasswordEncoder passwordEncoder(){ return new BCryptPasswordEncoder(); }



}
