package com.example.weatherinsight.exception.custom;

import org.springframework.http.HttpStatusCode;

public class GeocoderApiException extends ApplicationException {

    public GeocoderApiException(String message, HttpStatusCode httpStatusCode) {
        super(message, httpStatusCode);
    }
}
