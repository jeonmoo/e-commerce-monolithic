package com.test.ecommerce.domain.payment.repository;

import com.test.ecommerce.domain.payment.entity.Payment;
import com.test.ecommerce.domain.payment.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByOrderIdAndPaymentStatus(Long orderId, PaymentStatus paymentStatus);
}
