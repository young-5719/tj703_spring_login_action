package com.tj703.l09_spring_login.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter@Setter@ToString
public class OAuthUser {
    @NotBlank
    private String email;
    private String name;
    private String picture;
    @NotBlank
    private String oauth; // GOOGLE, KAKAO, NAVER, GITHUB
}
