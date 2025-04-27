package com.test.ecommerce.domain.order.mapper;

import com.test.ecommerce.domain.order.dto.OrderRequest;
import com.test.ecommerce.domain.order.dto.OrderResponse;
import com.test.ecommerce.domain.order.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    OrderItemMapper INSTANCE = Mappers.getMapper(OrderItemMapper.class);

    OrderItem toOrderItem(OrderRequest.OrderRequestItem request);

    @Mapping(target = "productId", source = "product.id")
    OrderResponse.OrderResponseItem toOrderResponseItem(OrderItem orderItem);
}
