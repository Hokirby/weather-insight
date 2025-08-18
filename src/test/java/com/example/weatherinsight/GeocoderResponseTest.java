package com.example.weatherinsight;

import com.example.weatherinsight.dto.geocoder.GeocoderResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class GeocoderResponseTest {

    @Test
    void testParsingGeocoderResponse() throws Exception {
        // 브라우저에서 받은 응답 JSON
        String json = """
        {
          "response": {
            "service": {
              "name": "address",
              "version": "2.0",
              "operation": "getCoord",
              "time": "84(ms)"
            },
            "status": "OK",
            "input": {
              "type": "ROAD",
              "address": "서울"
            },
            "refined": {
              "text": "서울특별시 중구 세종대로 110 (태평로1가)",
              "structure": {
                "level0": "대한민국",
                "level1": "서울특별시",
                "level2": "중구",
                "level3": "태평로1가",
                "level4L": "세종대로",
                "level4LC": "",
                "level4A": "명동",
                "level4AC": "1114055000",
                "level5": "110",
                "detail": "서울특별시 청사 신관"
              }
            },
            "result": {
              "crs": "EPSG:4326",
              "point": {
                "x": "126.978346780",
                "y": "37.566700969"
              }
            }
          }
        }
        """;

        ObjectMapper objectMapper = new ObjectMapper();

        // UTF-8 인코딩 그대로 바이트 → DTO 변환
        GeocoderResponse response = objectMapper.readValue(
                json.getBytes(StandardCharsets.UTF_8),
                GeocoderResponse.class
        );

        // ===== 검증 =====
        assertThat(response).isNotNull();
        assertThat(response.getResponse().getStatus()).isEqualTo("OK");
        assertThat(response.getResponse().getInput()).isNotNull();
        assertThat(response.getResponse().getInput().getAddress()).isEqualTo("서울");

        assertThat(response.getResponse().getResult()).isNotNull();
        assertThat(response.getResponse().getResult().getPoint().getX()).isEqualTo(126.97834678);
        assertThat(response.getResponse().getResult().getPoint().getY()).isEqualTo(37.566700969);

        // 출력 확인
        System.out.println("파싱 결과 DTO: " + response);
    }
}