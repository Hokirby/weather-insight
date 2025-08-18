package com.example.weatherinsight.dto.open_weather;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OpenWeatherResponse {
    private Coord coord;
    private List<Weather> weather;
    private String base;
    private Main main;
    private Integer visibility;
    private Wind wind;
    private Rain rain;
    private Clouds clouds;
    private Long dt;
    private Sys sys;
    private Integer timezone;
    private Integer id;
    private String name;
    private Integer cod;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Coord {
        private Double lon;
        private Double lat;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Weather {
        private Integer id;
        private String main;
        private String description;
        private String icon;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Main {
        private Double temp;
        private Double feels_like;
        private Double temp_min;
        private Double temp_max;
        private Integer pressure;
        private Integer humidity;
        private Integer sea_level; // Optional
        private Integer grnd_level; // Optional
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Wind {
        private Double speed;
        private Integer deg;
        private Double gust; // Optional
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Rain {
        // JSON 키가 "1h", "3h" 형태라서 변수명에 직접 쓸 수 없으므로 @SerializedName 사용
        @com.google.gson.annotations.SerializedName("1h")
        private Double oneHour;
        @com.google.gson.annotations.SerializedName("3h")
        private Double threeHour;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Clouds {
        private Integer all;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Sys {
        private Integer type; // Optional
        private Integer id;   // Optional
        private String country;
        private Long sunrise;
        private Long sunset;
    }
}