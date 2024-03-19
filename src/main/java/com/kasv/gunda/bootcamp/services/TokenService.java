package com.kasv.gunda.bootcamp.services;

import java.util.Map;

public class TokenService {

    private final Map<Long, String> userTokens;



    public TokenService(Map<Long, String> userTokens) {
        this.userTokens = userTokens;
    }

    public void storeToken(Long userId, String token) {
        userTokens.put(userId, token);
        System.out.println(userTokens);
    }


}
