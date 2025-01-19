package com.test.bootcamp.domain.product.entity;

import com.test.bootcamp.domain.category.entity.Category;
import jakarta.persistence.*;
import jakarta.persistence.Table;
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

    @Column
    @Setter
    private Integer quantity;

    @Setter
    @Column
    private BigDecimal price;

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
    public Product(Category category, String productName, Integer quantity, BigDecimal price, Boolean isDelete) {
        this.category = category;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.isDelete = isDelete;
    }
}
