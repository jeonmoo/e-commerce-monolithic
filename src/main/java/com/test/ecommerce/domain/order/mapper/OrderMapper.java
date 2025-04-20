package com.test.ecommerce.domain.order.mapper;

import com.test.ecommerce.domain.order.dto.OrderRequest;
import com.test.ecommerce.domain.order.dto.OrderResponse;
import com.test.ecommerce.domain.order.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    Order toOrder(OrderRequest request);

    OrderResponse toOrderResponse(Order order);
}
