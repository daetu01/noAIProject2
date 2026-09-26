package com.city.monitoring.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient seoulRestClient() {
        return RestClient.builder()
                .baseUrl("http://openapi.seoul.go.kr:8088")
                .build();
    }
}
