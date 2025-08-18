package com.example.weatherinsight.exception.custom;

import org.springframework.http.HttpStatusCode;

public class NotFoundException extends ApplicationException {
    public NotFoundException(String message, HttpStatusCode httpStatusCode) {
        super(message, httpStatusCode);
    }
}
