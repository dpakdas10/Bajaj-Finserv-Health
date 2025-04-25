package com.bajaj.service;

import com.bajaj.model.WebhookResponse;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WebhookServiceTest {

    @Test
    public void testFindMutualFollowers() {
        WebhookService service = new WebhookService(new RestTemplate());
        
        // Create test data
        List<WebhookResponse.User> users = Arrays.asList(
            createUser(1, Arrays.asList(2, 3)),
            createUser(2, Arrays.asList(1)),
            createUser(3, Arrays.asList(4)),
            createUser(4, Arrays.asList(3))
        );
        
        List<Integer> result = service.findMutualFollowers(users);
        
        // Verify the result
        assertEquals(Arrays.asList(1, 2, 3, 4), result);
    }
    
    private WebhookResponse.User createUser(int id, List<Integer> follows) {
        WebhookResponse.User user = new WebhookResponse.User();
        user.setId(id);
        user.setFollows(follows);
        return user;
    }
} 