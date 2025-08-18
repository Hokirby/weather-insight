package com.example.weatherinsight.dto.recommendation;

import lombok.*;

import java.sql.Timestamp;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecommendationResponse {
    private Long id;
    private String cityName;
    private Map<String, String> weatherDataJson;
    private String textContent;
    private Timestamp createdAt;
}
