package com.neoga.autionnara.config;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import sun.net.www.http.HttpClient;

@Configuration
public class RestTemplateConfig {

    @Autowired
    Environment env;

    @Bean
    public RestTemplate restTemplate() {

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();

        CloseableHttpClient client = HttpClientBuilder.create()
                .setMaxConnTotal(env.getProperty("rest.connPool.MaxConnTotal",Integer.class))
                .setMaxConnPerRoute(env.getProperty("rest.connPool.MaxConnPerRoute",Integer.class))
                .build();

        factory.setHttpClient(client);
        factory.setConnectTimeout(env.getProperty("rest.factory.setConnectTimeout",Integer.class));
        factory.setReadTimeout(env.getProperty("rest.factory.setReadTimeout",Integer.class));

        return new RestTemplate(factory);
    }
}
