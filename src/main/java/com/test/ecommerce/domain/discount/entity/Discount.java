package com.test.ecommerce.domain.discount.entity;

import com.test.ecommerce.domain.discount.enums.DiscountType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@Table(name = "discount")
@Entity
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String discountName;

    @Column
    private DiscountType discountType;

    @Column
    private BigDecimal discountAmount;

    @Column
    private Double discountPercent;

    @Column
    private LocalDateTime startDateTime;

    @Column
    private LocalDateTime endDateTime;

    @Column
    private String description;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Builder
    public Discount(String discountName, DiscountType discountType, BigDecimal discountAmount, Double discountPercent, LocalDateTime startDateTime, LocalDateTime endDateTime, String description) {
        this.discountName = discountName;
        this.discountType = discountType;
        this.discountAmount = discountAmount;
        this.discountPercent = discountPercent;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.description = description;
    }
}
