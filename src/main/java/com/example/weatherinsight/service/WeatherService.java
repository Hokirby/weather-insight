package com.example.weatherinsight.service;

import com.example.weatherinsight.client.GeocoderApiClient;
import com.example.weatherinsight.client.OpenWeatherApiClient;
import com.example.weatherinsight.dto.WeatherAnalysisData;
import com.example.weatherinsight.dto.weather_info.WeatherInfoResponse;
import com.example.weatherinsight.entity.WeatherInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {

    private final GeocoderApiClient geocoderClient;
    private final OpenWeatherApiClient openWeatherClient;
    private final LlmService llmService;
    private final WeatherInfoService weatherInfoService;

    // 외부 api 에서 주소값과 날씨 정보 조회
    public WeatherInfoResponse getWeatherInfo(String address) {
        // 주소 → 위도/경도 변환
        WeatherAnalysisData.Coordinates coordinates = geocoderClient.getCoordinates(address);
        log.info("[WeatherService] 좌표 변환 완료: {}", coordinates);

        // 위도/경도 기반 날씨 데이터 수집
        WeatherAnalysisData.WeatherData weatherData = openWeatherClient.getCurrentWeather(coordinates);
        log.info("[WeatherService] 날씨 데이터 수집 완료: {}", weatherData);

        return WeatherInfoResponse.builder()
                .address(address)
                .description(weatherData.getDescription())
                .feelsLike(weatherData.getFeelsLike())
                .temperature(weatherData.getTemperature())
                .latitude(coordinates.getLat())
                .longitude(coordinates.getLon())
                .build();
    }

    // gemini ai 사용해 추천 생성
    public WeatherAnalysisData getWeatherAnalysis(Long id) {
        // id로 조회
        WeatherInfoResponse weatherInfoResponse = weatherInfoService.find(id);
        WeatherInfo weatherInfo = WeatherInfo.builder()
                .address(weatherInfoResponse.getAddress())
                .temperature(weatherInfoResponse.getTemperature())
                .description(weatherInfoResponse.getDescription())
                .feelsLike(weatherInfoResponse.getFeelsLike())
                .latitude(weatherInfoResponse.getLatitude())
                .longitude(weatherInfoResponse.getLongitude())
                .build();

        // Gemini API로 분석·요약·추천 생성
        String analysisText = llmService.generateRecommendation(weatherInfo);

        // 결과 DTO로 변환
        return WeatherAnalysisData.builder()
                .address(weatherInfo.getAddress())
                .coordinates(WeatherAnalysisData.Coordinates.builder()
                        .lat(weatherInfo.getLatitude())
                        .lon(weatherInfo.getLongitude())
                        .build())
                .weatherData(WeatherAnalysisData.WeatherData.builder()
                        .description(weatherInfo.getDescription())
                        .feelsLike(weatherInfo.getFeelsLike())
                        .temperature(weatherInfo.getTemperature())
                        .build())
                .analysis(analysisText)
                .build();
    }
}
