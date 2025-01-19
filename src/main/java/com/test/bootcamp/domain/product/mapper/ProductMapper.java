package com.test.bootcamp.domain.product.mapper;

import com.test.bootcamp.domain.product.dto.ProductRequest;
import com.test.bootcamp.domain.product.dto.ProductResponse;
import com.test.bootcamp.domain.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    Product toProduct(ProductRequest request);
    ProductResponse toResponse(Product product);
}
