package com.neoga.platform;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableCaching
@EnableEurekaClient
@SpringBootApplication
public class PlatformApplication {

    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "/home/ec2-user/app/config/boltauction/server-secret.yml,"
            + "classpath:common-application.yml,"
            + "classpath:bootstrap.yml,"
            + "classpath:application.yml,"
            + "classpath:aws.yml";

    @PostConstruct
    public void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(PlatformApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
    }
}


