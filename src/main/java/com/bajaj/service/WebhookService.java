package com.bajaj.service;

import com.bajaj.model.WebhookRequest;
import com.bajaj.model.WebhookResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
public class WebhookService {
    private final RestTemplate restTemplate;
    private static final String GENERATE_WEBHOOK_URL = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook";
    private static final int MAX_RETRIES = 4;

    public WebhookService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void processWebhook() {
        // Create webhook request
        WebhookRequest request = new WebhookRequest("John Doe", "REG12347", "john@example.com");
        
        // Get webhook response
        WebhookResponse response = restTemplate.postForObject(
            GENERATE_WEBHOOK_URL,
            request,
            WebhookResponse.class
        );

        if (response == null) {
            log.error("Failed to generate webhook");
            return;
        }

        // Process the data based on registration number
        List<Integer> result;
        if (isOddRegistrationNumber(request.getRegNo())) {
            result = findMutualFollowers(response.getData().getUsers().getUsers());
        } else {
            result = findNthLevelFollowers(
                response.getData().getUsers().getUsers(),
                response.getData().getUsers().getFindId(),
                response.getData().getUsers().getN()
            );
        }

        // Send result to webhook
        sendResultToWebhook(response.getWebhook(), response.getAccessToken(), result);
    }

    private boolean isOddRegistrationNumber(String regNo) {
        // Extract the last two digits and check if odd
        try {
            int lastTwoDigits = Integer.parseInt(regNo.substring(regNo.length() - 2));
            return lastTwoDigits % 2 != 0;
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            log.error("Error parsing registration number: {}", e.getMessage());
            return false;
        }
    }

    public List<Integer> findMutualFollowers(List<WebhookResponse.User> users) {
        List<List<Integer>> mutualPairs = new ArrayList<>();
        Map<Integer, List<Integer>> userFollows = new HashMap<>();
        
        // Create a map of user follows
        for (WebhookResponse.User user : users) {
            userFollows.put(user.getId(), user.getFollows());
        }
        
        // Find mutual followers
        for (Integer user1 : userFollows.keySet()) {
            for (Integer user2 : userFollows.keySet()) {
                if (user1 < user2) {
                    List<Integer> follows1 = userFollows.get(user1);
                    List<Integer> follows2 = userFollows.get(user2);
                    
                    if (follows1.contains(user2) && follows2.contains(user1)) {
                        mutualPairs.add(Arrays.asList(user1, user2));
                    }
                }
            }
        }
        
        // Convert to single list of IDs
        Set<Integer> uniqueIds = new HashSet<>();
        for (List<Integer> pair : mutualPairs) {
            uniqueIds.addAll(pair);
        }
        
        List<Integer> result = new ArrayList<>(uniqueIds);
        Collections.sort(result);
        return result;
    }

    public List<Integer> findNthLevelFollowers(List<WebhookResponse.User> users, int startId, int n) {
        // Create a map for quick user lookup
        Map<Integer, List<Integer>> userFollows = new HashMap<>();
        for (WebhookResponse.User user : users) {
            userFollows.put(user.getId(), user.getFollows());
        }
        
        // Set to store followers at current level
        Set<Integer> currentLevel = new HashSet<>();
        currentLevel.add(startId);
        
        // Set to store followers at next level
        Set<Integer> nextLevel = new HashSet<>();
        
        // Process each level
        for (int level = 0; level < n; level++) {
            nextLevel.clear();
            
            // For each user at current level
            for (int userId : currentLevel) {
                List<Integer> follows = userFollows.get(userId);
                if (follows != null) {
                    // Add all their followers to next level
                    nextLevel.addAll(follows);
                }
            }
            
            // Remove users from previous levels to get exactly nth level
            nextLevel.removeAll(currentLevel);
            
            // Update current level for next iteration
            currentLevel = new HashSet<>(nextLevel);
        }
        
        List<Integer> result = new ArrayList<>(currentLevel);
        Collections.sort(result);
        return result;
    }

    private void sendResultToWebhook(String webhookUrl, String accessToken, List<Integer> result) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", accessToken);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("outcome", result);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        int retryCount = 0;
        while (retryCount < MAX_RETRIES) {
            try {
                restTemplate.postForObject(webhookUrl, request, String.class);
                log.info("Successfully sent result to webhook");
                return;
            } catch (Exception e) {
                retryCount++;
                log.error("Failed to send result to webhook (attempt {}/{}): {}", 
                    retryCount, MAX_RETRIES, e.getMessage());
                if (retryCount < MAX_RETRIES) {
                    try {
                        Thread.sleep(1000); // Wait 1 second before retrying
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }
        log.error("Failed to send result to webhook after {} attempts", MAX_RETRIES);
    }
} 