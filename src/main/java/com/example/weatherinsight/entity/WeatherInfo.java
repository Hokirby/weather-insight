package com.example.weatherinsight.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
@EntityListeners(AuditingEntityListener.class)
public class WeatherInfo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    // 검색어
    private String address;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double temperature;

    @Column(nullable = false)
    // 체감 온도
    private Double feelsLike;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp createdAt;
}
