package com.bajaj;

import com.bajaj.service.WebhookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WebhookProcessorApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebhookProcessorApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(WebhookService webhookService) {
        return args -> {
            webhookService.processWebhook();
        };
    }
} 