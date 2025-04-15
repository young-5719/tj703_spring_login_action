package com.tj703.l09_spring_login.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void generateToken() {
        String token = jwtUtil.generateToken("user1");
        System.out.println(token);
        // eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyMSIsImV4cCI6MTc0NDY4NzM0MX0.OCTE8hEdaJfKkSqS-56DTaWpRLfySxYvFe5g6cl5MdsGaTsllOievPTPkYY9k1U4vVaEZBi8ewBG6_5lHCel6w
    }

    @Test
    void validateToken() {
        String token ="eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyMSIsImV4cCI6MTc0NDY4NzM0MX0.OCTE8hEdaJfKkSqS-56DTaWpRLfySxYvFe5g6cl5MdsGaTsllOievPTPkYY9k1U4vVaEZBi8ewBG6_5lHCel6w";
        boolean check = jwtUtil.validateToken(token);
        assertTrue(check);
    }

    @Test
    void getUsernameToken() {
        String token = jwtUtil.generateToken("user1");
        String username = jwtUtil.getUsernameFromToken(token);
        System.out.println(username); // user1 이면 성공
        assertEquals(username, "user1");
    }
}