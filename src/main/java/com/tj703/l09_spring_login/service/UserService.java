package com.tj703.l09_spring_login.service;

import com.tj703.l09_spring_login.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> login(String id, String pw);

    boolean loginHash(User user);
}
