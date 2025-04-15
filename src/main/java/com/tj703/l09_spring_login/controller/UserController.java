package com.tj703.l09_spring_login.controller;

import com.tj703.l09_spring_login.entity.User;
import com.tj703.l09_spring_login.security.CustomUserDetailsService;
import com.tj703.l09_spring_login.service.UserService;
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
    @PostMapping("/jwt/login.do")
    public String loginAction(
            @ModelAttribute User user
            ) {
        System.out.println("loginAction 중!");
        UserDetails userDetails = null;
        userDetails = customUserDetailsService.loadUserByUsername(user.getId());
        System.out.println(userDetails.getUsername());

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                user.getPw(),
                userDetails.getAuthorities()
        ); // 로그인 -> 시큐리티에서 관리하는 유저 객체 토큰 발급

        SecurityContextHolder.getContext().setAuthentication(authToken);
        // 시큐리티에서 관리하는 로그인 유저(UserDetails)


        return "redirect:/";
    }

    @GetMapping("/logout.do")
    public String logoutAction(  ) {
        return "redirect:/";
    }
}
