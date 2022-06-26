package com.kps.backend.auth.service;

import com.kps.backend.auth.request.LoginRequest;
import com.kps.backend.auth.request.RegisterRequest;
import com.kps.backend.auth.response.RegisterResponse;
import com.kps.backend.auth.response.TokenResponse;
import com.kps.backend.response.SuccessResponse;
import com.kps.backend.security.exception.AuthorizationException;

public interface AuthService {
    RegisterResponse register(RegisterRequest request);
    TokenResponse login(LoginRequest request) throws AuthorizationException;
    TokenResponse mobileLogin(LoginRequest request) throws AuthorizationException;
    SuccessResponse logout(String token);
}
