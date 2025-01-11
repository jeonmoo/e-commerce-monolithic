package com.test.bootcamp.domain.user.controller;

import com.test.bootcamp.common.ApiResponse;
import com.test.bootcamp.domain.user.dto.UserRequest;
import com.test.bootcamp.domain.user.dto.UserResponse;
import com.test.bootcamp.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable Long userId) {
        UserResponse response = userService.getUser(userId);
        return ApiResponse.success(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody UserRequest request) {
        UserResponse response = userService.createUser(request);
        return ApiResponse.success(response);
    }

    @PutMapping
    public ResponseEntity<ApiResponse<UserResponse>> modifyUser(@Valid @RequestBody UserRequest request) {
        UserResponse response = userService.modifyUser(request);
        return ApiResponse.success(response);
    }

}
