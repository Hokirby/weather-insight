package com.example.weatherinsight.client;

import com.example.weatherinsight.dto.WeatherAnalysisData;
import com.example.weatherinsight.dto.geocoder.GeocoderResponse;
import com.example.weatherinsight.exception.custom.GeocoderApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class GeocoderApiClient {

    private final RestClient restClient;

    @Value("${geocoder.api.key}")
    private String apiKey;

    @Value("${geocoder.api.url}")
    private String apiUrl;

    public WeatherAnalysisData.Coordinates getCoordinates(String address) {
        URI uri = buildApiUri(address);
        log.info("[Geocoder] API URI: {}", uri);

        GeocoderResponse geocoderResponse = restClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                        String body = new String(response.getBody().readAllBytes());
                        log.error("[Geocoder] Raw Response: {}", body);

                    throw new GeocoderApiException("Geocoder API Request Failed: " + body, response.getStatusCode());
                })
                .body(GeocoderResponse.class);

        String body = geocoderResponse.toString();
        log.info("[Geocoder] Response: {}", body);

        // 응답 null check
        if (geocoderResponse.getResponse() == null) {
            throw new GeocoderApiException("Invalid Geocoder API Response For : " + address, HttpStatusCode.valueOf(404));
        }

        // 상태 코드 확인 (예: NOT_FOUND → 예외 처리)
        if (!"OK".equalsIgnoreCase(geocoderResponse.getResponse().getStatus())) {
            log.error("[Geocoder] Received Input: {}", geocoderResponse.getResponse().getInput());
            throw new GeocoderApiException("Point Not Found: " + address, HttpStatusCode.valueOf(404));
        }

        // 좌표 추출
        GeocoderResponse.Response.Result.Point point =
                geocoderResponse.getResponse().getResult().getPoint();

        return WeatherAnalysisData.Coordinates.builder()
                .lat(point.getY())
                .lon(point.getX())
                .build();

    }

    private URI buildApiUri(String address) {
        String ecodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
         return UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("service", "address")
                .queryParam("request", "getCoord")
                .queryParam("key", apiKey)
                .queryParam("type", "ROAD")
                .queryParam("format", "json")
                .queryParam("address", ecodedAddress)
                .build(true)
                 .toUri();
    }

}




