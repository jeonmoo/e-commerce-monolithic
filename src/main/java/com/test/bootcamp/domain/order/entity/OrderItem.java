package com.test.bootcamp.domain.order.entity;

import com.test.bootcamp.domain.order.enums.DiscountType;
import com.test.bootcamp.domain.product.entity.Product;
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
@Table(name = "orderItem")
@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @JoinColumn(name = "order_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column
    private Integer quantity;

    @Column
    private DiscountType discountType;

    @Column
    private BigDecimal discountValue;

    @Column
    private BigDecimal originPrice;

    @Setter
    @Column
    private BigDecimal finalPrice;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Builder

    public OrderItem(Order order, Product product, Integer quantity, DiscountType discountType, BigDecimal discountValue, BigDecimal originPrice, BigDecimal finalPrice) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.originPrice = originPrice;
        this.finalPrice = finalPrice;
    }
}
