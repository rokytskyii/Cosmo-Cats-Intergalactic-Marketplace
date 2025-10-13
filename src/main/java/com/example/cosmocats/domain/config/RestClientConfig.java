package com.example.cosmocats.domain.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${rest.client.timeout.connect:5000}")
    private int connectTimeout;

    @Value("${rest.client.timeout.read:10000}")
    private int readTimeout;

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .requestInterceptor((request, body, execution) -> {
                    return execution.execute(request, body);
                })
                .build();
    }
}