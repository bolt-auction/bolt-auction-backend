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

    /*
    @Value("${rest.connPool.MaxConnPerRoute}")
    private int maxConnPerRoute;
    @Value("${rest.connPool.MaxConnTotal}")
    private  int maxConnTotal;
    @Value("${rest.connPool.MaxConnPerRoute}")
    private int connectTimeout;
    @Value("${rest.factory.setReadTimeout}")
    private int readTimeout;
*/

    @Bean
    public RestTemplate restTemplate() {

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();

        CloseableHttpClient client = HttpClientBuilder.create()
                .setMaxConnTotal(50)
                .setMaxConnPerRoute(20)
                .build();

        factory.setHttpClient(client);
        factory.setConnectTimeout(2000);
        factory.setReadTimeout(5000);

        return new RestTemplate(factory);
    }
}
