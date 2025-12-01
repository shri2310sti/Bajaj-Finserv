package com.bajajfinserv.hiring.config;

import com.bajajfinserv.hiring.model.WebhookRequest;
import com.bajajfinserv.hiring.model.WebhookResponse;
import com.bajajfinserv.hiring.service.SQLSolver;
import com.bajajfinserv.hiring.service.WebhookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartupRunner implements ApplicationRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(ApplicationStartupRunner.class);
    
    @Value("${user.name}")
    private String name;
    
    @Value("${user.regNo}")
    private String regNo;
    
    @Value("${user.email}")
    private String email;
    
    private final WebhookService webhookService;
    private final SQLSolver sqlSolver;
    
    public ApplicationStartupRunner(WebhookService webhookService, SQLSolver sqlSolver) {
        this.webhookService = webhookService;
        this.sqlSolver = sqlSolver;
    }
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("===========================================");
        logger.info("Starting Bajaj Finserv Hiring Challenge");
        logger.info("===========================================");
        logger.info("Name: {}", name);
        logger.info("RegNo: {}", regNo);
        logger.info("Email: {}", email);
        logger.info("===========================================");
        
        try {
            // Step 1: Generate webhook
            logger.info("Step 1: Generating webhook...");
            WebhookRequest request = new WebhookRequest(name, regNo, email);
            WebhookResponse webhookResponse = webhookService.generateWebhook(request);
            
            // Step 2: Solve SQL question based on regNo
            logger.info("Step 2: Solving SQL question...");
            String sqlQuery = sqlSolver.solveSQLQuestion(regNo);
            logger.info("SQL Query generated:");
            logger.info("{}", sqlQuery);
            
            // Step 3: Submit solution
            logger.info("Step 3: Submitting solution...");
            webhookService.submitSolution(
                webhookResponse.getWebhook(),
                webhookResponse.getAccessToken(),
                sqlQuery
            );
            
            logger.info("===========================================");
            logger.info("Challenge completed successfully!");
            logger.info("===========================================");
            
        } catch (Exception e) {
            logger.error("===========================================");
            logger.error("Challenge failed: {}", e.getMessage(), e);
            logger.error("===========================================");
            throw e;
        }
    }
}