package com.tj703.l09_spring_login.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.logging.Logger;

@Component
public class JwtUtil {
    private final String SECRET_KEY="안녕하세요!비밀번호입니다34자입니다.ㅠㅠ길어야합니다아주많이훨씬많이";
    // JWT 를 만들고 검증할 때 쓰는 키
    //비밀번호는 최소 32자 이상
    // 외부에 노출되면 안되는 민감 정보로 실제로는 .application.yml 에 따로 보관하는 것이 좋음
    private final SecretKey secretKey;
    private final long EXPIRATION_TIME=1000*60*30; // 토큰이 만료되는 시간
    private final Logger logger=Logger.getLogger(JwtUtil.class.getName());

    public JwtUtil() {
        this.secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        // SECRET_KEY 를 바이트 배열로 바꿔서 실제로 사용할 암호화 키 객체로 변환
    }

    // { id : 경민 } <- SecretKey
    // dsfasfasffafdscas13213 토큰 생성
    public String generateToken(String username) { // 여기가 핵심 메서드
     // 토큰은 로그인 인증을 하기 위해 꼭 id 를 포함해야 한다. (사용자 이름을 기반으로 JWT 토큰 문자열을 만들어주는 함수)
        return Jwts.builder()
                .subject(username) // 토큰안에 누구의 토큰인지를 저장
                .expiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME)) // 만료시간 지정
                .signWith(secretKey, SignatureAlgorithm.HS512) // 어떤 암호화 알고리즘을 어떤 비밀키로 서명할지 지정
                .compact(); // 이 모든걸 압축해서 JWT 토큰 문자열로 만들어 반환
    }
    public String getUsernameFromToken(String token) {
        String username = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token) // 토큰에서 본문에 있는 내용을 가지고 오겠다.
                .getPayload() // header 머릿말 (메타정보) payload 본문 {subject : 경민}
                .getSubject();
        return username;
    }




    public boolean validateToken(String token) {
        try{
            // 파서는?  "13" -> 13
            // 13 -> "13" 은 쉬움
            // 파서는 어려운걸 해주는 일
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseClaimsJws(token); // Claims 본문에 있는 내용들을 키로 파싱하겠다.

        }catch (io.jsonwebtoken.security.SecurityException e){ // 비밀번호가 잘못되었을 때
            e.printStackTrace();
            return false;
        }
        catch (ExpiredJwtException e){ // 토큰이 만료되었을 때
            e.printStackTrace();
            //  logger.error(e.getMessage()); // 로그가 짧게 나와서 이건 회사갔을 때
            return false;
        }catch (UnsupportedJwtException e){ // 지원하지 않는 토큰
            e.printStackTrace();
            return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true; // 오류가 없다면
    }
}
