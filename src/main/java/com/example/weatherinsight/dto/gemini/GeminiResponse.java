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
public class GeminiResponse {
    private List<Candidate> candidates;
    private PromptFeedBack promptFeedBack;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Candidate {
        private Content content;
        private String finishReason;
        private Integer index;
        private List<SafetyRating> safetyRatings;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Content {
        private List<Part> parts;
        private String role;
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
    public static class SafetyRating {
        private String category;
        private String probability;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PromptFeedBack {
        private List<SafetyRating> safetyRatings;
    }
}
