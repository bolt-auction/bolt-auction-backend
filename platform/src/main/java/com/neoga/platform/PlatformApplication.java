package com.neoga.platform;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableCaching
@SpringBootApplication
public class PlatformApplication {

    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "/home/ec2-user/app/config/boltauction/server-secret.yml,"
            + "classpath:common-application.yml,"
            + "classpath:application.yml,"
            + "classpath:aws.yml";

    @PostConstruct
    public void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/seoul"));
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(PlatformApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
    }
}

