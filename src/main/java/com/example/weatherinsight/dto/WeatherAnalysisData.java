package com.example.weatherinsight.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeatherAnalysisData {
    private String address;
    private Coordinates coordinates;
    private WeatherData weatherData;
    private String analysis;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class WeatherData {
        private Double temperature;
        private Double feelsLike;
        private String description;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Coordinates {
        private Double lat;
        private Double lon;
    }

}
