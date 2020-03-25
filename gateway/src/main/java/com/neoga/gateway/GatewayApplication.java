package com.neoga.gateway;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableZuulProxy
@EnableEurekaClient
@SpringBootApplication
public class GatewayApplication {

    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:common-application.yml,"
            + "classpath:bootstrap.yml,"
            + "classpath:application.yml";

    @PostConstruct
    public void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(GatewayApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
    }
}

