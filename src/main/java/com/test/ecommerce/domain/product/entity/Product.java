package com.test.ecommerce.domain.product.entity;

import com.test.ecommerce.domain.category.entity.Category;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Table(name = "product")
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @JoinColumn(name = "category_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @Setter
    @Column(columnDefinition = "TEXT")
    private String productName;

    @Setter
    @Column
    private Integer quantity;

    @Setter
    @Column
    private BigDecimal finalPrice;

    @Setter
    @Column
    private BigDecimal originPrice;

    @Setter
    @Column
    private BigDecimal discountPrice;

//    @Setter
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "discount_id")
//    private Discount discount;

    @Setter
    @ColumnDefault("true")
    @Column(columnDefinition = "TINYINT")
    private Boolean isDelete;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Builder
    public Product(Category category, String productName, Integer quantity, BigDecimal finalPrice, BigDecimal originPrice, BigDecimal discountPrice, Boolean isDelete) {
        this.category = category;
        this.productName = productName;
        this.quantity = quantity;
        this.finalPrice = finalPrice;
        this.originPrice = originPrice;
        this.discountPrice = discountPrice;
        this.isDelete = isDelete;
    }
}
