package com.tracker.jobtracker.service;

import com.tracker.jobtracker.dto.AiJobAnalyzeRequest;
import com.tracker.jobtracker.dto.AiJobAnalyzeResponse;
import com.tracker.jobtracker.exception.AiServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Service
public class AiJobAnalyzerClient {

    private static final Logger log = LoggerFactory.getLogger(AiJobAnalyzerClient.class);

    private final RestClient restClient;

    @org.springframework.beans.factory.annotation.Autowired
    public AiJobAnalyzerClient(
            @Value("${ai.service.url:http://localhost:8000}") String aiServiceUrl
    ) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(3));
        requestFactory.setReadTimeout(Duration.ofSeconds(5));

        this.restClient = RestClient.builder()
                .baseUrl(aiServiceUrl)
                .requestFactory(requestFactory)
                .build();
    }

    // Overload for testing with custom RestClient
    public AiJobAnalyzerClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public AiJobAnalyzeResponse analyzeJob(AiJobAnalyzeRequest request) {
        try {
            return restClient.post()
                    .uri("/analyze-job")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(AiJobAnalyzeResponse.class);
        } catch (Exception ex) {
            log.error("Failed to connect or retrieve response from AI service: {}", ex.getMessage());
            throw new AiServiceException("Failed to call AI service: " + ex.getMessage(), ex);
        }
    }
}
