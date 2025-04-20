package com.test.ecommerce.domain.payment.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PaymentRequest {

    private BigDecimal refundValue;
    private String reason;
}
