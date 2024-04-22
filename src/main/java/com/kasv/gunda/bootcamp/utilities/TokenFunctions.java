package com.kasv.gunda.bootcamp.utilities;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TokenFunctions {

    private final Map<Long, String> userTokens;
    private static TokenFunctions instance;

    private TokenFunctions() {
        userTokens = new ConcurrentHashMap<>();
    }

    public static synchronized TokenFunctions getInstance() {
        if (instance == null) {
            instance = new TokenFunctions();
        }
        return instance;
    }

    public void storeToken(Long userId, String token) {
        userTokens.put(userId, token);
        System.out.println(userTokens);
    }

    public boolean isTokenValid(String token) {
        return userTokens.containsValue(token);
    }

    public void removeToken(Long userId) {
        userTokens.remove(userId);
        System.out.println(userTokens);
    }

    public String getToken(Long userId) {
        return userTokens.get(userId);
    }

    public boolean isTokenExists(Long userId) {
        return userTokens.containsKey(userId);
    }

    public void clearTokens() {
        userTokens.clear();
    }

    public long getUserId(String token) {
        for (Map.Entry<Long, String> entry : userTokens.entrySet()) {
            if (entry.getValue().equals(token)) {
                return entry.getKey();
            }
        }
        return -1;
    }

}
