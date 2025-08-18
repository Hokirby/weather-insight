package com.example.weatherinsight.client;

import com.example.weatherinsight.dto.WeatherAnalysisData;
import com.example.weatherinsight.dto.open_weather.OpenWeatherResponse;
import com.example.weatherinsight.exception.custom.OpenWeatherApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenWeatherApiClient {

    private final RestClient.Builder restClientBuilder;

    @Value("${open-weather.api.url}")
    private String apiUrl;

    @Value("${open-weather.api.key}")
    private String apiKey;

    public WeatherAnalysisData.WeatherData getCurrentWeather(WeatherAnalysisData.Coordinates coordinates) {
        String uri = UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("lat", coordinates.getLat())
                .queryParam("lon", coordinates.getLon())
                .queryParam("units","metric")
                .queryParam("lang","kr")
                .queryParam("appid", apiKey)
                .toUriString();
        log.info("[Open-Weather] URi: {}", uri);
        return restClientBuilder.build()
                .get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange((request, response) -> {
                   if (response.getStatusCode().isError()) {
                       String errorBody = "";
                       try {
                           errorBody = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
                       } catch (IOException e) {
                           log.error("[Open-Weather] Error Body Read Failed: ", e);
                       }
                       log.error("[Open-Weather] Request Failed ({}): {}", response.getStatusCode(), errorBody);
                       throw new OpenWeatherApiException("Open Weather API Request Failed: " + response.getStatusCode() + " " + errorBody);
                   }
                    OpenWeatherResponse openWeatherResponse = response.bodyTo(OpenWeatherResponse.class);
                    log.info("[Open-Weather] Response: {}", openWeatherResponse);
                    return WeatherAnalysisData.WeatherData.builder()
                            .temperature(openWeatherResponse.getMain().getTemp())
                            .feelsLike(openWeatherResponse.getMain().getFeels_like())
                            .description(openWeatherResponse.getWeather().get(0).getDescription())
                            .build();
                });
    }
}
