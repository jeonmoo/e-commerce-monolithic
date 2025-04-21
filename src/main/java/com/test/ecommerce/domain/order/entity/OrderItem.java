package com.test.ecommerce.domain.order.entity;

import com.test.ecommerce.domain.order.enums.OrderStatus;
import com.test.ecommerce.domain.product.entity.Product;
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
import java.util.List;

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

    @Setter
    @Column
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column
    private Integer quantity;

    @Column
    private BigDecimal originPrice;

    @Setter
    @Column
    private BigDecimal finalPrice;

    @Column
    private BigDecimal discountPrice;

    @OneToMany(mappedBy = "orderItem")
    private List<OrderItemDiscount> discounts;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Builder

    public OrderItem(Order order, Product product, OrderStatus orderStatus, Integer quantity, BigDecimal originPrice, BigDecimal finalPrice, BigDecimal discountPrice, List<OrderItemDiscount> discounts) {
        this.order = order;
        this.product = product;
        this.orderStatus = OrderStatus.PENDING;
        this.quantity = quantity;
        this.originPrice = originPrice;
        this.finalPrice = finalPrice;
        this.discountPrice = discountPrice;
        this.discounts = discounts;
    }
}
