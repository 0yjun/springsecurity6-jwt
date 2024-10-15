package com.security.domain.user.controller;

import com.security.domain.user.model.response.UserResponse;
import com.security.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="user API", description = "user API")
@RestController
@RequestMapping("/api/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "USER SEACH",
            description = "user 조회 기능 정의"
    )
    @GetMapping("/search/{name}")
    public UserResponse searchUser(
            @PathVariable("name") String name
    ){
        return userService.findByName(name);
    }
}
