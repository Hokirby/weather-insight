package com.example.weatherinsight.dto.recommendation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendationRequest {
    private String address;
    private String temperature;
    private String feelsLike;
    private String description;
    private String textContent;
}
