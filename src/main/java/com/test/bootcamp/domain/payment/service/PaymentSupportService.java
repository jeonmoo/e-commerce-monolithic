package com.test.bootcamp.domain.payment.service;

import com.test.bootcamp.common.GlobalException;
import com.test.bootcamp.common.exceptionCode.PaymentExceptionCode;
import com.test.bootcamp.domain.payment.dto.PaymentRequest;
import com.test.bootcamp.domain.payment.entity.Payment;
import com.test.bootcamp.domain.payment.enums.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentSupportService {

    protected void checkPaid(Payment payment) {
        PaymentStatus status = payment.getPaymentStatus();
        if (!PaymentStatus.PAID.equals(status)) {
            throw new GlobalException(PaymentExceptionCode.NOT_PAID);
        }
    }

    protected void checkPendingRefund(Payment payment) {
        PaymentStatus status = payment.getPaymentStatus();
        if (!PaymentStatus.PENDING_REFUND.equals(status)) {
            throw new GlobalException(PaymentExceptionCode.NOT_PENDING_REFUND);
        }
    }

    protected void applyRefund(Payment payment, PaymentRequest request) {
        payment.setPaymentStatus(PaymentStatus.REFUND);
        payment.setRefundAmount(payment.getRefundAmount().add(request.getRefundValue()));
    }
}
