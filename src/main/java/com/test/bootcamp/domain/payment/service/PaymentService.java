package com.test.bootcamp.domain.payment.service;

import com.test.bootcamp.common.GlobalException;
import com.test.bootcamp.common.exceptionCode.PaymentExceptionCode;
import com.test.bootcamp.domain.payment.dto.PaymentResponse;
import com.test.bootcamp.domain.payment.entity.Payment;
import com.test.bootcamp.domain.payment.enums.PaymentStatus;
import com.test.bootcamp.domain.payment.mapper.PaymentMapper;
import com.test.bootcamp.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentResponse requiredRefund(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new GlobalException(PaymentExceptionCode.NOT_FOUND_PAYMENT));

        checkPaid(payment);
        payment.setPaymentStatus(PaymentStatus.PENDING_REFUND);
        return PaymentMapper.INSTANCE.toResponse(payment);
    }

    public PaymentResponse refund(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new GlobalException(PaymentExceptionCode.NOT_FOUND_PAYMENT));

        checkPendingRefund(payment);
        payment.setPaymentStatus(PaymentStatus.REFUND);
        return PaymentMapper.INSTANCE.toResponse(payment);
    }

    private void checkPaid(Payment payment) {
        PaymentStatus status = payment.getPaymentStatus();
        if (!PaymentStatus.PAID.equals(status)) {
            throw new GlobalException(PaymentExceptionCode.NOT_PAID);
        }
    }

    private void checkPendingRefund(Payment payment) {
        PaymentStatus status = payment.getPaymentStatus();
        if (!PaymentStatus.PENDING_REFUND.equals(status)) {
            throw new GlobalException(PaymentExceptionCode.NOT_PENDING_REFUND);
        }
    }
}
