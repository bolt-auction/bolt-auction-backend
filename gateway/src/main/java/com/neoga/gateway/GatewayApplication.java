package com.neoga.gateway;

import com.neoga.gateway.config.RibbonConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy
@EnableEurekaClient
//@RibbonClients(defaultConfiguration = RibbonConfig.class)
@SpringBootApplication
public class GatewayApplication {

    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "/home/ec2-user/app/config/boltauction/server-secret.yml,"
            + "classpath:common-application.yml,"
            + "classpath:bootstrap.yml,"
            + "classpath:application.yml,"
            + "classpath:aws.yml";

    public static void main(String[] args) {
        new SpringApplicationBuilder(GatewayApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
    }
}

