package com.bajaj.service;

import java.util.*;

public class ShowNthFollowers {
    public static void main(String[] args) {
        // Create test data as per the example
        Map<Integer, List<Integer>> userFollows = new HashMap<>();
        
        // User 1 (Alice) follows Users 2 and 3
        userFollows.put(1, Arrays.asList(2, 3));
        
        // User 2 (Bob) follows User 4
        userFollows.put(2, Arrays.asList(4));
        
        // User 3 (Charlie) follows Users 4 and 5
        userFollows.put(3, Arrays.asList(4, 5));
        
        // User 4 (David) follows User 6
        userFollows.put(4, Arrays.asList(6));
        
        // User 5 (Eva) follows User 6
        userFollows.put(5, Arrays.asList(6));
        
        // User 6 (Frank) follows no one
        userFollows.put(6, new ArrayList<>());

        // Find 2nd level followers for user 1
        int startUser = 1;
        int n = 2;
        List<Integer> nthFollowers = findNthLevelFollowers(userFollows, startUser, n);
        
        // Sort the followers to ensure consistent output
        Collections.sort(nthFollowers);

        // Create and print the JSON output
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"regNo\": \"REG12347\",\n");
        json.append("  \"outcome\": ").append(nthFollowers);
        json.append("\n}");
        
        System.out.println(json.toString());
    }

    private static List<Integer> findNthLevelFollowers(Map<Integer, List<Integer>> userFollows, int startUser, int n) {
        if (n <= 0) {
            return new ArrayList<>();
        }

        Set<Integer> visited = new HashSet<>();
        Set<Integer> currentLevel = new HashSet<>();
        Set<Integer> nextLevel = new HashSet<>();
        
        // Start with the initial user
        currentLevel.add(startUser);
        visited.add(startUser);
        
        // Process each level
        for (int level = 1; level <= n; level++) {
            nextLevel.clear();
            
            // For each user in current level
            for (int user : currentLevel) {
                List<Integer> followers = userFollows.get(user);
                if (followers != null) {
                    // Add all followers of current user to next level
                    for (int follower : followers) {
                        if (!visited.contains(follower)) {
                            nextLevel.add(follower);
                            visited.add(follower);
                        }
                    }
                }
            }
            
            // If we've reached the target level, return the followers
            if (level == n) {
                return new ArrayList<>(nextLevel);
            }
            
            // Move to next level
            currentLevel.clear();
            currentLevel.addAll(nextLevel);
        }
        
        return new ArrayList<>();
    }
} 