package com.example.weatherinsight.exception.custom;

public class OpenWeatherApiException extends RuntimeException {
    public OpenWeatherApiException(String message) { super(message); }
}
