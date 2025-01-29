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

    @PostMapping("/{orderId}/refund-request")
    public ResponseEntity<ApiResponse<OrderResponse>> requiredRefundOrder(@PathVariable Long orderId) {
        OrderResponse result = orderService.requiredRefundOrder(orderId);
        return ApiResponse.success(result);
    }

    @PostMapping("/{orderId}/{orderItemId}/refund-request")
    public ResponseEntity<ApiResponse<OrderResponse>> requiredRefundOrderItem(@PathVariable Long orderId
            , @PathVariable Long orderItemId) {
        OrderResponse result = orderService.requiredRefundOrderItem(orderId, orderItemId);
        return ApiResponse.success(result);
    }

    @PostMapping("/{orderId}/refund")
    public ResponseEntity<ApiResponse<OrderResponse>> refundOrder(@PathVariable Long orderId) {
        OrderResponse result = orderService.refundOrder(orderId);
        return ApiResponse.success(result);
    }

    @PostMapping("/{orderId}/{orderItemId}/refund")
    public ResponseEntity<ApiResponse<OrderResponse>> refundOrderItem(@PathVariable Long orderId
            , @PathVariable Long orderItemId) {
        OrderResponse result = orderService.refundOrderItem(orderId, orderItemId);
        return ApiResponse.success(result);
    }
}
