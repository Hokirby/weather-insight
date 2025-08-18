package com.example.weatherinsight.service;

import com.example.weatherinsight.dto.recommendation.RecommendationRequest;
import com.example.weatherinsight.dto.recommendation.RecommendationResponse;
import com.example.weatherinsight.entity.WeatherRecommendation;
import com.example.weatherinsight.exception.custom.NotFoundException;
import com.example.weatherinsight.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class WeatherRecommendationService {

    private final RecommendationRepository recommendationRepository;

    public RecommendationResponse save(RecommendationRequest recommendationRequest) {

        Map<String, String> weatherData = Map.of(
                "temperature", recommendationRequest.getTemperature(),
                "feelsLike", recommendationRequest.getFeelsLike(),
                "description", recommendationRequest.getDescription()
        );
        WeatherRecommendation weatherRecommendation = recommendationRepository.save(
                WeatherRecommendation.builder()
                        .address(recommendationRequest.getAddress())
                        .weatherDataJson(weatherData)
                        .textContent(recommendationRequest.getTextContent())
                        .build()
        );
        return RecommendationResponse.builder()
                .id(weatherRecommendation.getId())
                .cityName(weatherRecommendation.getAddress())
                .weatherDataJson(weatherRecommendation.getWeatherDataJson())
                .textContent(weatherRecommendation.getTextContent())
                .createdAt(weatherRecommendation.getCreatedAt())
                .build();
    }

    public RecommendationResponse find(Long id) {
        WeatherRecommendation weatherRecommendation = recommendationRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("[WeatherRecommendationService] Recommendation Not Found, id = " + id, HttpStatusCode.valueOf(404)));
        return RecommendationResponse.builder()
                .id(weatherRecommendation.getId())
                .cityName(weatherRecommendation.getAddress())
                .weatherDataJson(weatherRecommendation.getWeatherDataJson())
                .textContent(weatherRecommendation.getTextContent())
                .createdAt(weatherRecommendation.getCreatedAt())
                .build();
    }

}
