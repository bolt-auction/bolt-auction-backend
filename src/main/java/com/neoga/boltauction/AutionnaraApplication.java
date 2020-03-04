package com.neoga.boltauction;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class AutionnaraApplication {

    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.properties,"
            + "classpath:aws.yml";

    @PostConstruct
    public void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/seoul"));
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(AutionnaraApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
    }
}

