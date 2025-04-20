package com.test.ecommerce.domain.product.repository;

import com.test.ecommerce.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByIdIn(List<Long> id);

    Boolean existsByCategoryId(Long categoryId);
}
