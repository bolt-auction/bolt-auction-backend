package com.neoga.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class AuthApplication {
    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:common-application.yml,"
            + "classpath:application.yml";

    @PostConstruct
    public void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(AuthApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
    }
}


