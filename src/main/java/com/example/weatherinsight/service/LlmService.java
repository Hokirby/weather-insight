package com.example.weatherinsight.service;

import com.example.weatherinsight.client.GeminiApiClient;
import com.example.weatherinsight.entity.WeatherInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LlmService {

    private final GeminiApiClient geminiClient;

    public String generateRecommendation(WeatherInfo weatherInfo) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("당신은 친절한 날씨 해설가이자 하루 계획을 도울 비서입니다. 다음 데이터를 분석하여 현재 날씨에 대한 요약과 앞으로의 날씨 변화, 그리고 적합한 생활/계획 팁을 제공해주세요.").append("\n\n");

        prompt.append("아래는 사용자의 지역과 날씨 정보입니다.").append("\n")
                .append("- 지역이름: ").append(weatherInfo.getAddress()).append("\n")
                .append("- 기온: ").append(weatherInfo.getTemperature()).append("\n")
                .append("- 설명: ").append(weatherInfo.getDescription()).append("\n")
                .append("- 체감온도: ").append(weatherInfo.getFeelsLike()).append("\n");

        return geminiClient.generateRequest(prompt.toString(), 0.7, 500);
    }
}
