package com.bajaj.model;

import lombok.Data;
import java.util.List;

@Data
public class WebhookResponse {
    private String webhook;
    private String accessToken;
    private WebhookData data;

    @Data
    public static class WebhookData {
        private Users users;
    }

    @Data
    public static class Users {
        private int n;
        private int findId;
        private List<User> users;
    }

    @Data
    public static class User {
        private int id;
        private String name;
        private List<Integer> follows;
    }
} 