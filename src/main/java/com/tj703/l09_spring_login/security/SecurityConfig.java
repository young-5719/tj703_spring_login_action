package com.tj703.l09_spring_login.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // 기존의 SecurityFilter 이 세션 기반으로 인증을 진행중이었는데
    // jwt 로 진행하면 세션기반인증을 하지 않게 되고 로그인 액션 자동완성과 로그인 인증 필터가 jwt 기반으로 생성 후 참조

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {
        return http
                .csrf(csrf->csrf.disable())
                .authorizeHttpRequests(auth->auth
                        .requestMatchers(
                                "/",
                                "/public/**",
                                "/user/jwt/login.do", // post loginAction
                                "favicon.ico"
                        ).permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(form->form
                        .loginPage("/user/login.do") // login form
                        .failureUrl("/user/login?error=true")
                        .permitAll()
                )
                // 세션 기반의 인증을 사용하지 않겠다. -> jwt 기반 인증을 생성해서 추가해야함
                // .addFilterBefore()
                .build();
    }

    // 우리가 만든 서비스로 로그인하겠다.
    @Bean
    public AuthenticationManager authenticationManager(
            HttpSecurity http) throws Exception {
        return http
                .getSharedObject(AuthenticationManagerBuilder.class)
                .build();
    }


    @Bean // 함수 단위의 설정
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 1234를 입력하면 해쉬코드로 저장하겠다.
    }

}
