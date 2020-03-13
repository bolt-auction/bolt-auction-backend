package com.neoga.boltauction;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableCaching
@SpringBootApplication
public class AutionnaraApplication {

    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:common-application.yml,"
            + "classpath:application.yml,"
            + "/app/config/boltauction/real-application.yml,"
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

