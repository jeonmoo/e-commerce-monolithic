package com.test.ecommerce.domain.order.entity;

import com.test.ecommerce.domain.discount.entity.Discount;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Table(name = "orderItemDiscount")
@Entity
public class OrderItemDiscount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;

    @ManyToOne
    @JoinColumn(name = "discount_id")
    private Discount discount;



}
