package com.example.weatherinsight.dto.weather_info;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherInfoRequest {
    private String address;
    private String description;
    private Double temperature;
    private Double feelsLike;
    private Double latitude;
    private Double longitude;
}
