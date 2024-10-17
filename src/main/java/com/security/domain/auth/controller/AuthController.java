package com.security.domain.auth.controller;

import com.security.common.exception.CustomException;
import com.security.domain.auth.model.reqest.LoginRequest;
import com.security.domain.auth.model.reqest.SignupRequest;
import com.security.domain.auth.model.response.LoginResponse;
import com.security.domain.auth.model.response.SignupResponse;
import com.security.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name="user API", description = "user API")
@RestController
@RequestMapping("/api/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(
            summary = "SIGNUP",
            description = "회원가입"
    )
    @PostMapping("/signup")
    public SignupResponse register(
            @RequestBody SignupRequest request
    ){
        return authService.register(request);
    }

    @Operation(
            summary = "LOGIN",
            description = "로그인"
    )
    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody LoginRequest request
    ){
        return authService.login(request);
    }

    @Operation(
            summary = "DECODE TOKKEN",
            description = "JWT 토큰 복호화"
    )
    @GetMapping("/getuser-from-token/{token}")
    public String getUserFromToken(
            @PathVariable String token
    ){
        return authService.getUserFromToken(token);
    }
}
