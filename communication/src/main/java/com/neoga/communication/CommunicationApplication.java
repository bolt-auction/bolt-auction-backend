package com.neoga.communication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableEurekaClient
@SpringBootApplication
public class CommunicationApplication {

    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "/home/ec2-user/app/config/boltauction/comminication-server.yml,"
            + "classpath:common-application.yml,"
            + "classpath:bootstrap.yml,"
            + "classpath:application.yml";

    @PostConstruct
    public void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(CommunicationApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
    }

}

