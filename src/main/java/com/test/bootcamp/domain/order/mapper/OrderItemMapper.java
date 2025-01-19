package com.test.bootcamp.domain.order.mapper;

import com.test.bootcamp.domain.order.dto.OrderRequest;
import com.test.bootcamp.domain.order.dto.OrderResponse;
import com.test.bootcamp.domain.order.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    OrderItemMapper INSTANCE = Mappers.getMapper(OrderItemMapper.class);

    OrderItem toOrderItem(OrderRequest.OrderItem request);

    OrderResponse.OrderItem toOrderItemResponse(OrderItem orderItem);
}
