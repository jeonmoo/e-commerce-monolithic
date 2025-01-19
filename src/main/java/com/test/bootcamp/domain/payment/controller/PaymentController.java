package com.test.bootcamp.domain.payment.controller;

import com.test.bootcamp.common.ApiResponse;
import com.test.bootcamp.domain.payment.dto.PaymentResponse;
import com.test.bootcamp.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/{orderId}/refund-request")
    public ResponseEntity<ApiResponse<PaymentResponse>> requiredRefund(@PathVariable Long orderId) {
        PaymentResponse result = paymentService.requiredRefund(orderId);
        return ApiResponse.success(result);
    }

    @PostMapping("/{orderId}/refund")
    public ResponseEntity<ApiResponse<PaymentResponse>> refund(@PathVariable Long orderId) {
        PaymentResponse result = paymentService.refund(orderId);
        return ApiResponse.success(result);
    }
}
