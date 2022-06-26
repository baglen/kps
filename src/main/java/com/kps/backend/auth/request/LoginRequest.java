package com.kps.backend.auth.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class LoginRequest {
    @JsonProperty("login")
    @NotNull
    private String login;
    @NotNull
    @JsonProperty("password")
    private String password;

    public String getUsername() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
