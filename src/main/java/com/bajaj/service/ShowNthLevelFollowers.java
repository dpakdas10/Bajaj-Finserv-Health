package com.bajaj.service;

import java.util.*;

class User {
    int id;
    String name;
    List<Integer> follows;

    User(int id, String name, List<Integer> follows) {
        this.id = id;
        this.name = name;
        this.follows = follows;
    }
}

public class ShowNthLevelFollowers {
    public static void main(String[] args) {
        // Create test data as per example
        List<User> users = Arrays.asList(
            new User(1, "Alice", Arrays.asList(2, 3)),
            new User(2, "Bob", Arrays.asList(4)),
            new User(3, "Charlie", Arrays.asList(4, 5)),
            new User(4, "David", Arrays.asList(6)),
            new User(5, "Eva", Arrays.asList(6)),
            new User(6, "Frank", Collections.emptyList())
        );

        int n = 2;  // Level to find
        int findId = 1;  // Starting user ID

        // Find nth level followers
        Set<Integer> nthLevelFollowers = findNthLevelFollowers(users, findId, n);
        List<Integer> sortedFollowers = new ArrayList<>(nthLevelFollowers);
        Collections.sort(sortedFollowers);

        // Print the count (number of nth level followers)
        System.out.println(sortedFollowers.size());
        System.out.println();

        // Create and print the JSON output
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"regNo\": \"REG12347\",\n");
        json.append("  \"outcome\": ").append(sortedFollowers);
        json.append("\n}");
        
        System.out.println(json.toString());
    }

    private static Set<Integer> findNthLevelFollowers(List<User> users, int startId, int n) {
        // Create a map for quick user lookup
        Map<Integer, User> userMap = new HashMap<>();
        for (User user : users) {
            userMap.put(user.id, user);
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
                User user = userMap.get(userId);
                if (user != null && user.follows != null) {
                    // Add all their followers to next level
                    nextLevel.addAll(user.follows);
                }
            }

            // Update current level for next iteration
            currentLevel = new HashSet<>(nextLevel);
        }

        return currentLevel;  // After n iterations, currentLevel contains nth level followers
    }
} 