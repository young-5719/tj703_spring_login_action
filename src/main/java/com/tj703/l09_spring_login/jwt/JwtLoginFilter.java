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

//모든 요청을 가로채서 쿠키로 넘어온 jwt로 로그인 인증을 하는 곳
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
        //요청해더에 존재하는 jwt가 있는지 검사
        String authHeader=request.getHeader("Authorization");
        System.out.println(authHeader);
        //요청 header :Authorization -> "Bearer dlfkjsdoif12313....."
        if( authHeader!=null && authHeader.startsWith("Bearer ")) {
            String token=authHeader.substring(7);
            System.out.println(token);
            boolean isValidToken=false;
            try {
                isValidToken=jwtUtil.validateToken(token);
            }catch (JwtException e) {
                logger.error(e.getMessage());
            }
            if(isValidToken) { //로그인한 유저를 저장했다!!
                String username = jwtUtil.getUsername(token);
                //spring-security 가 인증 인가를 위한 토큰 생성 및 저장
                UserDetails userDetails=userDetailsService.loadUserByUsername(username);
                if(userDetails!=null) {
                    System.out.println(userDetails.getUsername());
                    UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    //spring-security 가 인증 인가를 위한 토큰 생성 및 저장 끝
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}