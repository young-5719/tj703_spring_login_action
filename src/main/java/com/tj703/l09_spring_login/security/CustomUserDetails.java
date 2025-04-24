package com.tj703.l09_spring_login.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tj703.l09_spring_login.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private final User user;
    public CustomUserDetails(User user) {
        this.user = user;
    }
    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_"+user.getRole()));
    }
    @JsonIgnore
    @Override
    public String getPassword() {return user.getPw();}

    @Override
    public String getUsername() {
        return user.getId();
    }
    public String getRole() {
        return user.getRole();
    }
    public String getName() {
        return user.getName();
    }
}
