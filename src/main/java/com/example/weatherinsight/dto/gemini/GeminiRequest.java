package com.example.weatherinsight.dto.gemini;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeminiRequest {
    private List<Content> contents;
    private GenerationConfig generationConfig;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Content {
        private List<Part> parts;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Part {
        private String text;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GenerationConfig {
        private Double temperature;
    }
}
