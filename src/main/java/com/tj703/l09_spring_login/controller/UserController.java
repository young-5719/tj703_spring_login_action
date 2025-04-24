package com.tj703.l09_spring_login.controller;

import com.tj703.l09_spring_login.dto.LoginDto;
import com.tj703.l09_spring_login.dto.OAuthUser;
import com.tj703.l09_spring_login.dto.UserLoginValid;
import com.tj703.l09_spring_login.entity.User;
import com.tj703.l09_spring_login.jwt.JwtUtil;
import com.tj703.l09_spring_login.security.CustomUserDetailsService;
import com.tj703.l09_spring_login.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000"})
public class UserController {

    private final UserService userService;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    //로그인폼 -> jwt/login.do 요청 {id:경민,pw:1234} ->
    // 로그인이 되었다면 jwt 토큰 생성 후 응답 ->
    // 로그인 양식에서 jwt 토큰을 받아서 로컬에 저장
    @GetMapping("/jwt/check.do")
    public ResponseEntity<LoginDto> checkLogin(
            @AuthenticationPrincipal UserDetails userDetails
    ){
        LoginDto loginDto=new LoginDto();
        String jwt=jwtUtil.generateToken(userDetails.getUsername());
        Optional<User> userOpt=userService.detail(userDetails.getUsername());
        if(userOpt.isPresent()) {
            User user=userOpt.get();
            loginDto.setUser(user);
            loginDto.setJwt(jwt);
            return ResponseEntity.ok(loginDto);
        }
        return ResponseEntity.badRequest().build();
    }
    @PostMapping("/oauth/signup.do")
    public ResponseEntity<LoginDto> signupAction(
            @Valid @RequestBody OAuthUser oAuthUser
    ){
        // 성공하면 바로 로그인(회원가입+로그인)
        System.out.printf("oAuthUser: %s",oAuthUser);
        User user = User.builder()
                .role("USER")
                .id(oAuthUser.getEmail())
                .name(oAuthUser.getName())
                .picture(oAuthUser.getPicture())
                .oauth(oAuthUser.getOauth())
                .build();
        try{
            userService.register(user);
            Optional<User> loginUserOpt = userService.detail(user.getId());
            String jwt = jwtUtil.generateToken(oAuthUser.getEmail());
            LoginDto loginDto=new LoginDto();
            loginDto.setJwt(jwt);
            loginDto.setUser(loginUserOpt.get());
            return ResponseEntity.ok(loginDto);
        }catch (IllegalArgumentException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return ResponseEntity.status(409).build();
        }catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/oauth/login.do")
    public ResponseEntity<LoginDto> oauthLogin(
            @Valid @RequestBody OAuthUser oAuthUser
            ) {
        System.out.printf("oAuthUser: %s", oAuthUser);
        // 만약 가입이 되어 있지 않다 : 404
        // 만약 가입은 되어 있는데 소셜이 잘못되어있다: 409 + LoginDto
        Optional<User> userOpt = userService.detail(oAuthUser.getEmail());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            LoginDto loginDto=new LoginDto();
            loginDto.setUser(user);
            if(user.getOauth().equals(oAuthUser.getOauth())) {
                // 로그인 성공 => jwt 발급
                String jwt = jwtUtil.generateToken(user.getId());
                loginDto.setJwt(jwt);
                return ResponseEntity.ok(loginDto);
            }
            return ResponseEntity.status(409).build();
            // 소셜로그인을 잘못함 => 가입된 소셜로 다시 로그인
        }
        return ResponseEntity.notFound().build(); // 가입된 유저가 없어서 가입페이지로
    }

    @PostMapping("/jwt/login.do")
    public ResponseEntity<LoginDto> loginAction(
            @Valid @RequestBody UserLoginValid userLoginValid
    ) {
        User user=User.builder() //로그인할때 사용하는 user
                .id(userLoginValid.getId())
                .pw(userLoginValid.getPw()).build();

        System.out.println("loginAction 중!"+user);
        Optional<User> userOpt=userService.loginHash(user); //로그인 완료 후 가져오는 user
        if(userOpt.isPresent()) {
            LoginDto loginDto=new LoginDto();
            String jwt=jwtUtil.generateToken(user.getId());
            loginDto.setJwt(jwt);
            loginDto.setUser(userOpt.get());
            return ResponseEntity.ok(loginDto);
        }
        return ResponseEntity.status(403).build();
        //403 인증이 안됨 (유저가 없거나 비밀번호가 틀림)
    }
    // session
    // => session 만료,

    // security+session (/logout)
    // SecurityContextHolder.getContext().setAuthentication(null);

    // cookie+jwt => cookie 만료

    // localstorage + jwt => 브라우저 로컬저장소에 jwt 를 삭제
//    @GetMapping("/logout.do")
//    public String logoutAction(
//            @CookieValue(name = "jwt",required = false) Cookie jwtCookie,
//            HttpServletResponse response
//    ) {
//        if(jwtCookie!=null){
//            jwtCookie.setMaxAge(0);
//            jwtCookie.setPath("/");
//            response.addCookie(jwtCookie);
//        }
//        return "redirect:/";
//    }
}
