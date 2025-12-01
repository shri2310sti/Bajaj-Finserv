package com.bajajfinserv.hiring.service;

import com.bajajfinserv.hiring.model.SolutionRequest;
import com.bajajfinserv.hiring.model.WebhookRequest;
import com.bajajfinserv.hiring.model.WebhookResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WebhookService {

    private static final Logger logger = LoggerFactory.getLogger(WebhookService.class);

    @Value("${api.base.url}")
    private String baseUrl;

    @Value("${api.webhook.generate.path}")
    private String generatePath;

    @Value("${api.webhook.test.path}")
    private String testPath;

    private final RestTemplate restTemplate;

    public WebhookService() {
        this.restTemplate = new RestTemplate();
    }

    public WebhookResponse generateWebhook(WebhookRequest request) {
        try {
            String url = baseUrl + generatePath;
            logger.info("Generating webhook at: {}", url);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<WebhookRequest> entity = new HttpEntity<>(request, headers);

            ResponseEntity<WebhookResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    WebhookResponse.class);

            WebhookResponse webhookResponse = response.getBody();
            logger.info("Webhook generated successfully");
            logger.info("Webhook URL: {}", webhookResponse.getWebhook());

            return webhookResponse;

        } catch (Exception e) {
            logger.error("Error generating webhook: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate webhook", e);
        }
    }

    public void submitSolution(String webhookUrl, String accessToken, String sqlQuery) {
        try {
            logger.info("Submitting solution to: {}", webhookUrl);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", accessToken);

            SolutionRequest solutionRequest = new SolutionRequest(sqlQuery);
            HttpEntity<SolutionRequest> entity = new HttpEntity<>(solutionRequest, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    webhookUrl,
                    HttpMethod.POST,
                    entity,
                    String.class);

            logger.info("Solution submitted successfully");
            logger.info("Response status: {}", response.getStatusCode());
            logger.info("Response body: {}", response.getBody());

        } catch (Exception e) {
            logger.error("Error submitting solution: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to submit solution", e);
        }
    }
}