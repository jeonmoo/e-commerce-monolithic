package com.test.bootcamp.domain.order.controller;

import com.test.bootcamp.common.ApiResponse;
import com.test.bootcamp.domain.order.dto.OrderRequest;
import com.test.bootcamp.domain.order.dto.OrderResponse;
import com.test.bootcamp.domain.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> registerOrder(@Valid @RequestBody OrderRequest request) {
        OrderResponse response = orderService.registerOrder(request);
        return ApiResponse.success(response);
    }

    @PostMapping("/{orderId}/complete")
    public ResponseEntity<ApiResponse<OrderResponse>> completeOrder(@PathVariable Long orderId) {
        OrderResponse result = orderService.completeOrder(orderId);
        return ApiResponse.success(result);
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(@PathVariable Long orderId) {
        OrderResponse result = orderService.cancelOrder(orderId);
        return ApiResponse.success(result);
    }
}
