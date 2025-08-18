package com.example.weatherinsight.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class RestClientConfig {

    private static final int CONNECT_TIME_OUT_SECONDS = 5;
    private static final int READ_TIME_OUT_SECONDS = 5;

    @Bean
    public RestClient restClient(RestClient.Builder builder) {
        // timeout 적용
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(CONNECT_TIME_OUT_SECONDS));
        requestFactory.setReadTimeout(Duration.ofSeconds(READ_TIME_OUT_SECONDS));

        return builder.requestFactory(requestFactory)
                .build();
    }
}
