package com.example.weatherinsight.client;

import com.example.weatherinsight.dto.gemini.GeminiRequest;
import com.example.weatherinsight.dto.gemini.GeminiResponse;
import com.example.weatherinsight.exception.custom.GeminiApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class GeminiApiClient {

    private final RestClient restClient;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    public String generateRequest(String prompt, Double temperature, Integer maxTokens) {
        GeminiRequest geminiRequest = createRequest(prompt, temperature, maxTokens);

        URI uri = buildUri();
        log.info("[Gemini] API URI: {}", uri);

        return restClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(geminiRequest)
                .exchange((request, response) -> {
                    if (response.getStatusCode().isError()) {
                        String errorBody = "";
                        try {
                            errorBody = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
                        } catch (IOException e) {
                            log.error("[Gemini] Error Body Read Failed: ", e);
                        }
                        log.error("[Gemini] Request Failed ({}): {}", response.getStatusCode(), errorBody);
                        throw new GeminiApiException("Gemini Request Failed: " + errorBody, response.getStatusCode());
                    }
                    GeminiResponse geminiResponse = response.bodyTo(GeminiResponse.class);
                    log.info("[Gemini] Response: {}", geminiResponse);
                    String generatedText = extractText(geminiResponse);
                    log.info("[Gemini] Text Extracted: {}", generatedText);
                    return generatedText;
                });
    }

    public GeminiRequest createRequest(String prompt, Double temperature, Integer maxTokens) {
        GeminiRequest.Part part = GeminiRequest.Part.builder()
                .text(prompt)
                .build();

        GeminiRequest.Content content = GeminiRequest.Content.builder()
                .parts(Collections.singletonList(part))
                .build();

        GeminiRequest.GenerationConfig config = GeminiRequest.GenerationConfig.builder()
                .temperature(temperature)
                .build();

        return GeminiRequest.builder()
                .contents(Collections.singletonList(content))
                .generationConfig(config)
                .build();
    }

    public String extractText(GeminiResponse response) {
        if (response == null || response.getCandidates() == null || response.getCandidates().isEmpty()) {
            log.warn("Empty Response From Gemini");
            return "";
        }
        GeminiResponse.Candidate candidate = response.getCandidates().get(0);
        if (candidate.getContent() == null || candidate.getContent().getParts() == null || candidate.getContent().getParts().isEmpty()) {
            log.warn("No Content parts in Gemini API Response");
            return "";
        }
        return candidate.getContent().getParts().get(0).getText();
    }

    private URI buildUri() {
        return UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("key", apiKey)
                .build()
                .toUri();
    }

}
