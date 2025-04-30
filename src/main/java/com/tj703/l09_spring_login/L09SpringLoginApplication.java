package com.tj703.l09_spring_login;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@PropertySources(@PropertySource(value= "classpath:env.properties"))
@SpringBootApplication
public class L09SpringLoginApplication {

    public static void main(String[] args) {
        SpringApplication.run(L09SpringLoginApplication.class, args);
    }

}
