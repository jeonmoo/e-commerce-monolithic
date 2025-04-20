package com.test.ecommerce.domain.order.controller;

import com.test.ecommerce.common.ApiResponse;
import com.test.ecommerce.domain.order.dto.OrderRequest;
import com.test.ecommerce.domain.order.dto.OrderResponse;
import com.test.ecommerce.domain.order.dto.RefundResponse;
import com.test.ecommerce.domain.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(@PathVariable Long orderId
            , @RequestBody OrderRequest request) {
        OrderResponse result = orderService.cancelOrder(orderId, request);
        return ApiResponse.success(result);
    }

    @PostMapping("/{orderId}/refund-request")
    public ResponseEntity<ApiResponse<OrderResponse>> requestRefundOrder(@PathVariable Long orderId) {
        OrderResponse result = orderService.requestRefundOrder(orderId);
        return ApiResponse.success(result);
    }

    @PostMapping("/{orderId}/{orderItemId}/refund-request")
    public ResponseEntity<ApiResponse<OrderResponse>> requestRefundOrderItem(@PathVariable Long orderId
            , @PathVariable Long orderItemId) {
        OrderResponse result = orderService.requestRefundOrderItem(orderId, orderItemId);
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

    @GetMapping("/{orderId}/refunds")
    public ResponseEntity<ApiResponse<List<RefundResponse>>> getRefunds(@PathVariable Long orderId) {
        List<RefundResponse> result = orderService.getRefunds(orderId);
        return ApiResponse.success(result);
    }
}
