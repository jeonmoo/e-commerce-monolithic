package com.test.ecommerce.domain.payment.entity;

import com.test.ecommerce.domain.payment.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Table(name = "payment")
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long orderId;

    @Column
    private Long orderItemId;

    @Column
    @Setter
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column
    private BigDecimal payAmount;

    @Setter
    @Column
    private BigDecimal refundAmount;

    @Setter
    @Column
    private String reason;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Builder
    public Payment(Long orderId, Long orderItemId, PaymentStatus paymentStatus, BigDecimal payAmount, BigDecimal refundAmount, String reason) {
        this.orderId = orderId;
        this.orderItemId = orderItemId;
        this.paymentStatus = paymentStatus;
        this.payAmount = payAmount;
        this.refundAmount = refundAmount;
        this.reason = reason;
    }
}
