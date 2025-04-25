package com.bajaj.service;

import java.util.*;

public class ShowMutualFollowers {
    public static void main(String[] args) {
        // Create test data as per new example
        Map<Integer, List<Integer>> userFollows = new HashMap<>();
        
        // User 1 (Alice) follows User 2 and User 3
        userFollows.put(1, Arrays.asList(2, 3));
        
        // User 2 (Bob) follows User 1
        userFollows.put(2, Arrays.asList(1));
        
        // User 3 (Charlie) follows User 4
        userFollows.put(3, Arrays.asList(4));
        
        // User 4 (David) follows User 3
        userFollows.put(4, Arrays.asList(3));

        // Find mutual followers
        List<List<Integer>> mutualPairs = findMutualFollowers(userFollows);
        
        // Sort the pairs to ensure consistent output
        Collections.sort(mutualPairs, (a, b) -> {
            int firstCompare = a.get(0).compareTo(b.get(0));
            return firstCompare != 0 ? firstCompare : a.get(1).compareTo(b.get(1));
        });

        // Create and print the JSON output
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"regNo\": \"REG12347\",\n");
        json.append("  \"outcome\": ").append(mutualPairs);
        json.append("\n}");
        
        System.out.println(json.toString());
    }

    private static List<List<Integer>> findMutualFollowers(Map<Integer, List<Integer>> userFollows) {
        List<List<Integer>> mutualPairs = new ArrayList<>();
        
        for (Integer user1 : userFollows.keySet()) {
            for (Integer user2 : userFollows.keySet()) {
                if (user1 < user2) {  // Avoid duplicate pairs
                    List<Integer> follows1 = userFollows.get(user1);
                    List<Integer> follows2 = userFollows.get(user2);
                    
                    if (follows1.contains(user2) && follows2.contains(user1)) {
                        mutualPairs.add(Arrays.asList(user1, user2));
                    }
                }
            }
        }
        
        return mutualPairs;
    }
} 