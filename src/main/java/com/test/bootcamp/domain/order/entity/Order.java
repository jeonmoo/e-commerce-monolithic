package com.test.bootcamp.domain.order.entity;

import com.test.bootcamp.domain.order.enums.OrderStatus;
import com.test.bootcamp.domain.user.entity.User;
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
@Table(name = "orders")
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Setter
    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderItem> orderItems;

    @Column
    @Setter
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column
    @Setter
    private BigDecimal totalFinalPrice;

    @Column
    @Setter
    private BigDecimal totalOriginPrice;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Builder
    public Order(User user, List<OrderItem> orderItems, OrderStatus orderStatus, String address, BigDecimal totalFinalPrice, BigDecimal totalOriginPrice) {
        this.user = user;
        this.orderItems = orderItems;
        this.orderStatus = orderStatus;
        this.address = address;
        this.totalFinalPrice = totalFinalPrice;
        this.totalOriginPrice = totalOriginPrice;
    }
}
