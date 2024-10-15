package com.security.domain.auth.controller;

import com.security.domain.auth.model.reqest.SignupRequest;
import com.security.domain.auth.model.response.SignupResponse;
import com.security.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name="user API", description = "user API")
@RestController
@RequestMapping("/api/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final AuthService authService;

    @Operation(
            summary = "SIGNUP",
            description = "회원가입"
    )
    @PostMapping("/signup")
    public SignupResponse register(
            @RequestBody SignupRequest request
    ){
        log.info("requestis "+request.toString());
        return authService.register(request);
    }
}
