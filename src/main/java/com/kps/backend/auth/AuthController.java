package com.kps.backend.auth;

import com.kps.backend.auth.request.LoginRequest;
import com.kps.backend.auth.request.RegisterRequest;
import com.kps.backend.auth.response.RegisterResponse;
import com.kps.backend.auth.response.TokenResponse;
import com.kps.backend.auth.service.AuthService;
import com.kps.backend.response.SuccessResponse;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("register")
    public RegisterResponse register(@RequestBody @Valid RegisterRequest request){
        return authService.register(request);
    }

    @PostMapping("login")
    public TokenResponse login(@RequestBody @Valid LoginRequest request){
        return authService.login(request);
    }

    @PostMapping("login/mobile")
    public TokenResponse loginMobile(@RequestBody @Valid LoginRequest request){
        return authService.mobileLogin(request);
    }

    @GetMapping("logout")
    public SuccessResponse logout(@RequestHeader("Authorization") String token){
        return authService.logout(token);
    }
}
