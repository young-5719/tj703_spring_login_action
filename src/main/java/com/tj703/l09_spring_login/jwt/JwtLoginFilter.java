package com.tj703.l09_spring_login.jwt;

import com.tj703.l09_spring_login.security.CustomUserDetailsService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// 모든 요청을 가로채서 쿠키로 넘어온 jwt 로 로그인 인증을 하는 곳
@Component
@AllArgsConstructor
public class JwtLoginFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        System.out.println("JwtLoginFilter");

        // cookie 를 찾아와서 가져와야함
        Cookie [] cookies = request.getCookies();
        Cookie jwtCookie = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwt")) { // jwt 라는 쿠키를 찾으면
                    jwtCookie = cookie; // 로그인 정보가 담김
                }
            }
        }
        if (jwtCookie != null) { // 로그인되어 있음
            boolean check = false;
            try{
                check = jwtUtil.validateToken(jwtCookie.getValue());
            }catch (JwtException e){
                logger.error(e.getMessage());
            }
            if (check) {
                String username = jwtUtil.getUsername(jwtCookie.getValue());
                System.out.println(username); // username 인 사람이 로그인을 했다.
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                  userDetails,
                        null, // 비밀번호
                        userDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(token);

                // security 에게 로그인정보 전달 -> 인증
            }
        }
        filterChain.doFilter(request, response);
    }
}
