package com.tj703.l09_spring_login.controller;

import com.tj703.l09_spring_login.entity.User;
import com.tj703.l09_spring_login.jwt.JwtUtil;
import com.tj703.l09_spring_login.security.CustomUserDetailsService;
import com.tj703.l09_spring_login.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;  // JWT 토큰 생성 도구 클래스

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @GetMapping("/login.do")
    public String login(
             @AuthenticationPrincipal UserDetails loginUser // 세션 기반을 막았기 때문에
             //required=true 자동으로 로그인 한 사람만 이 리소스 요청가능(400)
    ) {
        //로그인되어 있는 유저는 다시 로그인 폼으로 올 수 없다.
        //Object loginUserObj=session.getAttribute("loginUser");
        if(loginUser==null) {
            return "user/login";
        }else{
            return "redirect:/";
        }

    }
    // ?id=admin1&pw=1234
    @PostMapping("/jwt/login.do") // 로그인 폼에서 id, pw 넘기면 여기서 로그인 처리
    public String loginAction( // DB 에서 해당 ID 로 사용자 정보 조회
            @ModelAttribute User user,
            HttpServletResponse response
            ) {
        System.out.println("loginAction 중!");
        UserDetails userDetails = null;
        userDetails = customUserDetailsService.loadUserByUsername(user.getId());
        System.out.println(userDetails.getUsername());

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                user.getPw(),
                userDetails.getAuthorities()
        ); // 로그인 -> 시큐리티에서 관리하는 유저 객체 토큰 발급 (Spring Security 인증 토큰 생성)
        // SecurityContext 에 로그인 정보 등록

        SecurityContextHolder.getContext().setAuthentication(authToken);
        // 시큐리티에서 관리하는 로그인 유저(UserDetails)

        Cookie jwt = new Cookie("jwt",jwtUtil.generateToken(userDetails.getUsername()));
        // jwtUtil 로 JWT 토큰 생성
        jwt.setHttpOnly(true); // js 쿠키 탈취 못하게 -> 자바스크립트로 쿠키 접근 못하게 막음
        // jwt.setSecure(true); // https 통신에서만 쿠키 보내겠다.
        jwt.setPath("/"); // 전역 경로에사용
        jwt.setMaxAge(1000*60*30); // 쿠키 유효 시간
        response.addCookie(jwt);
        return "redirect:/";
    }

    // >> session => session 만료,
    // security + session =>
    // cookie + jwt => cookie 만료

    @GetMapping("/logout.do")
    public String logoutAction(
            @CookieValue(name = "jwt", required = false) Cookie jwtCookie,
            HttpServletResponse response
    ) {
        if (jwtCookie != null) {
            jwtCookie.setMaxAge(0);
            jwtCookie.setPath("/");
            response.addCookie(jwtCookie);
        }
        return "redirect:/";
    }
}
