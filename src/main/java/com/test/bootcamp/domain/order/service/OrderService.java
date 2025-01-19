package com.test.bootcamp.domain.order.service;

import com.test.bootcamp.common.GlobalException;
import com.test.bootcamp.common.exceptionCode.OrderExceptionCode;
import com.test.bootcamp.domain.order.dto.OrderRequest;
import com.test.bootcamp.domain.order.dto.OrderResponse;
import com.test.bootcamp.domain.order.entity.Order;
import com.test.bootcamp.domain.order.enums.OrderStatus;
import com.test.bootcamp.domain.order.mapper.OrderMapper;
import com.test.bootcamp.domain.order.repository.OrderRepository;
import com.test.bootcamp.domain.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderSupportService orderSupportService;
    private final OrderRepository orderRepository;

    @Transactional
    public OrderResponse registerOrder(OrderRequest request) {
        List<Product> products = orderSupportService.getOrderInProduct(request);
        orderSupportService.checkProductStack(request.getOrderItems(), products);
        orderSupportService.reduceStock(request.getOrderItems(), products);

        Order order = orderSupportService.toOrder(request, products);
        order.setOrderStatus(OrderStatus.PENDING);
        orderSupportService.updateTotalPrice(order);
        Order savedOrder = orderRepository.save(order);
        orderSupportService.pay(savedOrder);

        return orderSupportService.toResponse(savedOrder);
    }

    @Transactional
    public OrderResponse completeOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new GlobalException(OrderExceptionCode.NOT_FOUND_ORDER));

        orderSupportService.checkOrderPending(order);
        orderSupportService.orderComplete(order);
        return OrderMapper.INSTANCE.toOrderResponse(order);
    }

    @Transactional
    public OrderResponse cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new GlobalException(OrderExceptionCode.NOT_FOUND_ORDER));

        orderSupportService.checkOrderPending(order);
        orderSupportService.orderCancel(order);
        return OrderMapper.INSTANCE.toOrderResponse(order);
    }
}
