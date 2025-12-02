package com.test.ecommerce.domain.product.mapper;

import com.test.ecommerce.domain.product.dto.ProductRankResponse;
import com.test.ecommerce.domain.product.dto.ProductCreateRequest;
import com.test.ecommerce.domain.product.dto.ProductResponse;
import com.test.ecommerce.domain.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    Product toProduct(ProductCreateRequest request);

    @Mapping(target = "categoryId", source = "category.id")
    ProductResponse toResponse(Product product);

    ProductRankResponse toRankResponse(long id, String name, long rank, long score);
}
