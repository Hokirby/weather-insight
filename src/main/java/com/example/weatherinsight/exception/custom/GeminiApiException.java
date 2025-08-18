package com.example.weatherinsight.exception.custom;

import org.springframework.http.HttpStatusCode;

public class GeminiApiException extends ApplicationException {

    public GeminiApiException(String message, HttpStatusCode httpStatusCode) {
        super(message, httpStatusCode);
    }
}
