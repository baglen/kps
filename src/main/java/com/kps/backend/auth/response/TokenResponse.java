package com.kps.backend.auth.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenResponse {
    private String token;
    @JsonProperty("user_id")
    private Long userId;

    public TokenResponse(String token, Long userId) {
        this.token = token;
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
