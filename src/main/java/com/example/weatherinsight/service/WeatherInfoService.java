package com.example.weatherinsight.service;

import com.example.weatherinsight.dto.weather_info.WeatherInfoRequest;
import com.example.weatherinsight.dto.weather_info.WeatherInfoResponse;
import com.example.weatherinsight.entity.WeatherInfo;
import com.example.weatherinsight.exception.custom.NotFoundException;
import com.example.weatherinsight.repository.WeatherInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeatherInfoService {

    private final WeatherInfoRepository weatherInfoRepository;

    public WeatherInfoResponse save(WeatherInfoRequest weatherInfoRequest){
        WeatherInfo weatherInfo = weatherInfoRepository.save(
                WeatherInfo.builder()
                        .address(weatherInfoRequest.getAddress())
                        .description(weatherInfoRequest.getDescription())
                        .feelsLike(weatherInfoRequest.getFeelsLike())
                        .temperature(weatherInfoRequest.getTemperature())
                        .latitude(weatherInfoRequest.getLatitude())
                        .longitude(weatherInfoRequest.getLongitude())
                        .build()
        );

        return WeatherInfoResponse.builder()
                .id(weatherInfo.getId())
                .address(weatherInfo.getAddress())
                .description(weatherInfo.getDescription())
                .feelsLike(weatherInfo.getFeelsLike())
                .temperature(weatherInfo.getTemperature())
                .latitude(weatherInfo.getLatitude())
                .longitude(weatherInfo.getLongitude())
                .build();

    }

    public WeatherInfoResponse find(Long id) {
        WeatherInfo weatherInfo = weatherInfoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("WeatherInfo Not Found With ID: {}"+ id, HttpStatusCode.valueOf(404)));

        return WeatherInfoResponse.builder()
                .id(weatherInfo.getId())
                .address(weatherInfo.getAddress())
                .description(weatherInfo.getDescription())
                .feelsLike(weatherInfo.getFeelsLike())
                .temperature(weatherInfo.getTemperature())
                .latitude(weatherInfo.getLatitude())
                .longitude(weatherInfo.getLongitude())
                .build();
    }
}
