package com.example.weatherinsight.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Weather Application",
                description = "위치 기반 날씨 정보를 이용하고 Gemini AI 추천을 받는 어플리케이션",
                version = "1.0.0"
        )
)
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        String[] packages = {"com.example.weatherinsight"};
        return GroupedOpenApi.builder()
                .group("default")
                .packagesToScan(packages)
                .build();
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components());
    }
}