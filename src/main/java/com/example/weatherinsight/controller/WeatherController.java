package com.example.weatherinsight.controller;

import com.example.weatherinsight.dto.ApiResponseDto;
import com.example.weatherinsight.dto.WeatherAnalysisData;
import com.example.weatherinsight.dto.recommendation.RecommendationRequest;
import com.example.weatherinsight.dto.recommendation.RecommendationResponse;
import com.example.weatherinsight.dto.weather_info.WeatherInfoRequest;
import com.example.weatherinsight.dto.weather_info.WeatherInfoResponse;
import com.example.weatherinsight.service.WeatherInfoService;
import com.example.weatherinsight.service.WeatherRecommendationService;
import com.example.weatherinsight.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;
    private final WeatherRecommendationService weatherRecommendationService;
    private final WeatherInfoService weatherInfoService;

    // 위도 경도, 날씨 정보 조회
    @GetMapping("/weather")
    public ResponseEntity<ApiResponseDto<WeatherInfoResponse>> getWeatherData(@RequestParam String address) {
        log.info("[WeatherController] Received Address: {}", address);
        try {
            WeatherInfoResponse weatherInfoResponse = weatherService.getWeatherInfo(address);
            return ResponseEntity.ok(ApiResponseDto.<WeatherInfoResponse>builder()
                    .success(true)
                    .message("날씨 정보 조회에 성공했습니다")
                    .data(weatherInfoResponse)
                    .timestamp(LocalDateTime.now())
                    .build());
        } catch (Exception e) {
            log.error("[WeatherController] Failed To Get WeatherInfoResponse: ", e);
            return ResponseEntity.internalServerError().body(ApiResponseDto.<WeatherInfoResponse>builder()
                    .success(false)
                    .message("날씨 정보 조회에 실패했습니다")
                    .errorCode(e.getClass().getName())
                    .timestamp(LocalDateTime.now())
                    .build());
        }
    }

    // 날씨 정보 저장
    @PostMapping("/weather")
    public ResponseEntity<ApiResponseDto<WeatherInfoResponse>> saveWeatherInfo(@RequestBody WeatherInfoRequest weatherInfoRequest) {
        try {
            WeatherInfoResponse weatherInfoResponse = weatherInfoService.save(weatherInfoRequest);
            return ResponseEntity.status(HttpStatus.SC_CREATED)
                    .body(ApiResponseDto.<WeatherInfoResponse>builder()
                            .success(true)
                            .message("날씨 정보 저장에 성공했습니다")
                            .data(weatherInfoResponse)
                            .timestamp(LocalDateTime.now())
                            .build());
        } catch (Exception e) {
            log.error("[WeatherController] Failed To Save WeatherInfo: ", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponseDto.<WeatherInfoResponse>builder()
                            .success(false)
                            .message("날씨 정보 저장에 실패했습니다")
                            .errorCode(e.getClass().getName())
                            .timestamp(LocalDateTime.now())
                            .build());
        }
    }

    // 날씨 정보 조회
    @GetMapping("/weather/{id}")
    public ResponseEntity<ApiResponseDto<WeatherInfoResponse>> getWeatherInfo(@PathVariable long id) {
        log.info("[WeatherController] Received WeatherInfo ID: {}", id);
        try {
            WeatherInfoResponse weatherInfoResponse = weatherInfoService.find(id);
            return ResponseEntity.ok(ApiResponseDto.<WeatherInfoResponse>builder()
                    .success(true)
                    .message("날씨 정보 조회에 성공했습니다")
                    .data(weatherInfoResponse)
                    .timestamp(LocalDateTime.now())
                    .build());
        } catch (Exception e) {
            log.error("[WeatherController] Failed To Get WeatherInfo: ", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponseDto.<WeatherInfoResponse>builder()
                            .success(false)
                            .message("날씨 정보 조회에 실패했습니다")
                            .errorCode(e.getClass().getName())
                            .timestamp(LocalDateTime.now())
                            .build());
        }
    }

    // 날씨 정보 조회 -> Gemini API 에서 추천 받기
    @PostMapping("/weather/{id}")
    public ResponseEntity<ApiResponseDto<WeatherAnalysisData>> generateWeatherAnalysis(@PathVariable Long id) {
        log.info("[WeatherController] Received WeatherInfo ID: {}", id);
        try {
            WeatherAnalysisData weatherAnalysisData = weatherService.getWeatherAnalysis(id);
            return ResponseEntity.ok(ApiResponseDto.<WeatherAnalysisData>builder()
                    .success(true)
                    .message("날씨 분석/추천 생성에 성공했습니다")
                    .data(weatherAnalysisData)
                    .timestamp(LocalDateTime.now())
                    .build());
        } catch (Exception e) {
            log.error("[WeatherController] Failed To Generate WeatherAnalysisData: ", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponseDto.<WeatherAnalysisData>builder()
                            .success(false)
                            .message("날씨 분석/추천 생성에 실패했습니다")
                            .errorCode(e.getClass().getName())
                            .timestamp(LocalDateTime.now())
                            .build());
        }

    }

    // 날씨 분석/추천 저장
    @PostMapping("/analysis")
    public ResponseEntity<ApiResponseDto<RecommendationResponse>> saveRecommendation(@RequestBody RecommendationRequest recommendationRequest) {
        try {
            RecommendationResponse recommendationResponse = weatherRecommendationService.save(recommendationRequest);
            return ResponseEntity.status(HttpStatus.SC_CREATED)
                    .body(ApiResponseDto.<RecommendationResponse>builder()
                            .success(true)
                            .message("날씨 분석/추천이 성공적으로 저장되었습니다")
                            .data(recommendationResponse)
                            .timestamp(LocalDateTime.now())
                            .build());
        } catch (Exception e) {
            log.error("[WeatherController] Failed To Save WeatherRecommendation: ", e);
            return ResponseEntity.internalServerError().body(ApiResponseDto.<RecommendationResponse>builder()
                    .success(false)
                    .message("추천 저장에 실패했습니다.")
                    .errorCode(e.getClass().getName())
                    .timestamp(LocalDateTime.now())
                    .build());
        }
    }

    // 날씨 분석/추천 조회
    @GetMapping("/analysis/{id}")
    public ResponseEntity<ApiResponseDto<RecommendationResponse>> getRecommendation(@PathVariable Long id) {
        log.info("[WeatherController] Received Recommendation Id: {}", id);
        try {
            RecommendationResponse recommendationResponse = weatherRecommendationService.find(id);
            return ResponseEntity.ok(ApiResponseDto.<RecommendationResponse>builder()
                    .success(true)
                    .message("저장된 추천 조회에 성공했습니다.")
                    .data(recommendationResponse)
                    .timestamp(LocalDateTime.now())
                    .build());
        } catch (Exception e) {
            log.error("[WeatherController] Failed To Get Recommendation: ", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponseDto.<RecommendationResponse>builder()
                            .success(false)
                            .message("저장된 추천 조회에 실패했습니다.")
                            .errorCode(e.getClass().getName())
                            .timestamp(LocalDateTime.now())
                            .build());
        }
    }
}
