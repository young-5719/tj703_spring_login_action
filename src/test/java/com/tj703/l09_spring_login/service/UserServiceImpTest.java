package com.tj703.l09_spring_login.service;

import com.tj703.l09_spring_login.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UserServiceImpTest {
    @Autowired
    private UserService userService;
    @Test
    void login() {
        Optional<User> userOpt = userService.login("user1", "1234");
        userOpt.ifPresent(System.out::println);
    }

    @Test
    void list() {
        System.out.println(userService.list());
    }
}