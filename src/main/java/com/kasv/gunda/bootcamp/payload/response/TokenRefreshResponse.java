package com.kasv.gunda.bootcamp.payload.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TokenRefreshResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";

    public TokenRefreshResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
