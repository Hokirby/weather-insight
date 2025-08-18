package com.example.weatherinsight.repository;

import com.example.weatherinsight.entity.WeatherInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherInfoRepository extends JpaRepository<WeatherInfo, Long> {
}
