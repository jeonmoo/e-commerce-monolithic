package com.test.bootcamp.domain.user.controller;

import com.test.bootcamp.common.ApiResponse;
import com.test.bootcamp.domain.order.dto.OrderResponse;
import com.test.bootcamp.domain.user.dto.UserRequest;
import com.test.bootcamp.domain.user.dto.UserResponse;
import com.test.bootcamp.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> modifyUser(@PathVariable Long userId, @Valid @RequestBody UserRequest request) {
        UserResponse response = userService.modifyUser(userId, request);
        return ApiResponse.success(response);
    }

    @GetMapping("/{userId}/order")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrders(@PathVariable Long userId) {
        List<OrderResponse> result = userService.getOrderInUser(userId);
        return ApiResponse.success(result);
    }

}
