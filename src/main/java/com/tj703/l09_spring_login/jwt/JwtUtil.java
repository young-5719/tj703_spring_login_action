package com.tj703.l09_spring_login.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET_KEY="안녕하세요!비밀번호입니다34자입니다.ㅠㅠ 더길어야하네요..";//
    //비밀번호는 34자 이상
    private final SecretKey secretKey;
    private final long EXPIRATION=1000*60*30;
    private final Logger logger= LoggerFactory.getLogger(JwtUtil.class);
    public JwtUtil() {
        this.secretKey= Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
    //{id:경민} <-SecretKey
    //SDKLFJSOIU12U901327U123 토큰 생성
    public String generateToken(String username) {
        //토큰은 로그인 인증을 하기위해 꼭 id를 포함해야한다.
        return Jwts.builder()
                .subject(username)
                .expiration(new Date(System.currentTimeMillis()+EXPIRATION))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }
    public String getUsername(String token) {
        String username=Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload() //header  머릿말(메타정보) payload 본문 {subject:경민}
                .getSubject();
        return username;
    }


    public boolean validateToken(String token) throws JwtException {
        try {
            //파서 "십삼"->13
            //13->"13"
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException e) {//서명이 잘못된 것
            e.printStackTrace();
            throw new JwtException("Invalid JWT");
        } catch (ExpiredJwtException e){ //만료된 토큰
            e.printStackTrace();
            throw new JwtException("Expired JWT");
        }catch (UnsupportedJwtException e){ //지원하지 않는 토큰 (알고리즘)
            e.printStackTrace();
            throw new JwtException("Unsupported JWT");
        } catch (Exception e){
            e.printStackTrace();
            throw new JwtException("Invalid ex JWT");
        }
    }


}