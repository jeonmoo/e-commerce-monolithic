package com.test.ecommerce.domain.order.controller;

import com.test.ecommerce.common.ApiResponse;
import com.test.ecommerce.domain.order.dto.OrderRequest;
import com.test.ecommerce.domain.order.dto.OrderResponse;
import com.test.ecommerce.domain.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> registerOrder(@Valid @RequestBody OrderRequest request) {
        OrderResponse response = orderService.registerOrder(request);
        return ApiResponse.success(response);
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<ApiResponse<OrderResponse>> completeOrder(@PathVariable Long id) {
        OrderResponse result = orderService.completeOrder(id);
        return ApiResponse.success(result);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(@PathVariable Long id
            , @RequestBody OrderRequest request) {
        OrderResponse result = orderService.cancelOrder(id, request);
        return ApiResponse.success(result);
    }

    @PostMapping("/{id}/refund-request")
    public ResponseEntity<ApiResponse<OrderResponse>> requestRefundOrder(@PathVariable Long id) {
        OrderResponse result = orderService.requestRefundOrder(id);
        return ApiResponse.success(result);
    }

    @PostMapping("/{id}/{orderItemId}/refund-request")
    public ResponseEntity<ApiResponse<OrderResponse>> requestRefundOrderItem(@PathVariable Long id
            , @PathVariable Long orderItemId) {
        OrderResponse result = orderService.requestRefundOrderItem(id, orderItemId);
        return ApiResponse.success(result);
    }

    @PostMapping("/{id}/refund")
    public ResponseEntity<ApiResponse<OrderResponse>> refundOrder(@PathVariable Long id) {
        OrderResponse result = orderService.refundOrder(id);
        return ApiResponse.success(result);
    }

    @PostMapping("/{id}/{orderItemId}/refund")
    public ResponseEntity<ApiResponse<OrderResponse>> refundOrderItem(@PathVariable Long id
            , @PathVariable Long orderItemId) {
        OrderResponse result = orderService.refundOrderItem(id, orderItemId);
        return ApiResponse.success(result);
    }
}
