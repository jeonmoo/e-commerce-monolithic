package com.test.ecommerce.domain.user.controller;

import com.test.ecommerce.common.ApiResponse;
import com.test.ecommerce.domain.order.dto.OrderResponse;
import com.test.ecommerce.domain.user.dto.UserCreateRequest;
import com.test.ecommerce.domain.user.dto.UserModifyRequest;
import com.test.ecommerce.domain.user.dto.UserResponse;
import com.test.ecommerce.domain.user.service.UserService;
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

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable Long id) {
        UserResponse response = userService.getUser(id);
        return ApiResponse.success(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody UserCreateRequest request) {
        UserResponse response = userService.createUser(request);
        return ApiResponse.success(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> modifyUser(@PathVariable Long id, @Valid @RequestBody UserModifyRequest request) {
        UserResponse response = userService.modifyUser(id, request);
        return ApiResponse.success(response);
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrders(@PathVariable Long id) {
        List<OrderResponse> result = userService.getOrderInUser(id);
        return ApiResponse.success(result);
    }

    @GetMapping("/{id}/orders/refunds")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getRefunds(@PathVariable Long id) {
        List<OrderResponse> result = userService.getRefunds(id);
        return ApiResponse.success(result);
    }

}
