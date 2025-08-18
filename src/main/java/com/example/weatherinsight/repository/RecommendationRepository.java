package com.example.weatherinsight.repository;

import com.example.weatherinsight.entity.WeatherRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendationRepository extends JpaRepository<WeatherRecommendation, Long> {
}
