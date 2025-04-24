package com.tj703.l09_spring_login.security;

import com.tj703.l09_spring_login.jwt.JwtLoginFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
    //기존의 SecurityFilterChain 이 세션기반으로 인증을 진행중이었는데
    //jwt로 진행하면 세션기반인증을 하지 않게 되고 로그인액션 자동완성과 로그인인증필터가 jwt 기반으로 생성 후 참조
    private final JwtLoginFilter jwtLoginFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())    // cors 허용
                .csrf(csrf->csrf.disable())
                .authorizeHttpRequests(auth->auth
                        .requestMatchers(
                                "/",
                                "/public/**",
                                "/user/jwt/login.do", //post loginAction
                                "user/oauth/login.do", // post oauth loginAction
                                "/user/oauth/signup.do", // post oauth signupAction
                                "/user/jwt/check.do",
                                "/favicon.ico"
                        ).permitAll()
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN","MANAGER")
                        .anyRequest().authenticated())
                //jwt로 쿠키로 로그인할때 설정
                .sessionManagement(session->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                //세션 기반의 이증을 사용하지 않겠다. -> jwt 기반 인증을 생성해서 추가해야함
                .addFilterBefore(jwtLoginFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    @Bean
    public AuthenticationManager authenticationManager(
            HttpSecurity http) throws Exception {
        return http
                .getSharedObject(AuthenticationManagerBuilder.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
