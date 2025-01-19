package com.test.bootcamp.domain.payment.entity;

import com.test.bootcamp.domain.order.entity.Order;
import com.test.bootcamp.domain.payment.enums.PaymentStatus;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column
    private BigDecimal payAmount;

    @Column
    @Setter
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column
    private String reason;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Builder
    public Payment(Order order, BigDecimal payAmount, PaymentStatus paymentStatus, String reason) {
        this.order = order;
        this.payAmount = payAmount;
        this.paymentStatus = paymentStatus;
        this.reason = reason;
    }
}
