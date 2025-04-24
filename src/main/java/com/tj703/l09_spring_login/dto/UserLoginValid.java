package com.tj703.l09_spring_login.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter
@ToString
public class UserLoginValid {
        @NotEmpty
        private String id;
        @NotEmpty
        private String pw;
    }

