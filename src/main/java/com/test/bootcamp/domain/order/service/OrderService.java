package com.test.bootcamp.domain.order.service;

import com.test.bootcamp.common.GlobalException;
import com.test.bootcamp.common.exceptionCode.OrderExceptionCode;
import com.test.bootcamp.domain.order.dto.OrderRequest;
import com.test.bootcamp.domain.order.dto.OrderResponse;
import com.test.bootcamp.domain.order.entity.Order;
import com.test.bootcamp.domain.order.entity.OrderItem;
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

    private Order findOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new GlobalException(OrderExceptionCode.NOT_FOUND_ORDER));
    }

    @Transactional
    public OrderResponse registerOrder(OrderRequest request) {
        List<Product> products = orderSupportService.getOrderInProduct(request);
        orderSupportService.checkProductStock(request.getOrderItems(), products);
        orderSupportService.reduceStock(request.getOrderItems(), products);

        Order order = orderSupportService.initOrder(request, products);
        Order savedOrder = orderRepository.save(order);
        orderSupportService.pay(savedOrder);

        return orderSupportService.toResponse(savedOrder);
    }

    @Transactional
    public OrderResponse completeOrder(Long orderId) {
        Order order = findOrder(orderId);

        orderSupportService.checkOrderPending(order);
        orderSupportService.completeOrder(order);
        return OrderMapper.INSTANCE.toOrderResponse(order);
    }

    @Transactional
    public OrderResponse cancelOrder(Long orderId) {
        Order order = findOrder(orderId);

        orderSupportService.checkOrderPending(order);
        orderSupportService.orderCancel(order);
        return OrderMapper.INSTANCE.toOrderResponse(order);
    }

    @Transactional
    public OrderResponse requiredRefundOrder(Long orderId) {
        Order order = findOrder(orderId);

        orderSupportService.checkOrderRefundRequest(order);
        orderSupportService.applyRefundRequestOrder(order);
        return OrderMapper.INSTANCE.toOrderResponse(order);
    }

    @Transactional
    public OrderResponse requiredRefundOrderItem(Long orderId, Long orderItemId) {
        Order order = findOrder(orderId);

        OrderItem orderItem = order.getOrderItems().stream()
                .filter(item -> item.getId().equals(orderItemId))
                .findFirst()
                .orElseThrow(() -> new GlobalException(OrderExceptionCode.NOT_FOUND_ORDER_Item));

        orderSupportService.checkOrderItemRefundRequest(orderItem);
        orderSupportService.applyRefundRequestOrderItem(orderItem);
        return OrderMapper.INSTANCE.toOrderResponse(order);
    }

    @Transactional
    public OrderResponse refundOrder(Long orderId) {
        Order order = findOrder(orderId);

        orderSupportService.checkRequestRefundOrder(order);
        orderSupportService.refundOrder(order);
        return OrderMapper.INSTANCE.toOrderResponse(order);
    }

    @Transactional
    public OrderResponse refundOrderItem(Long orderId, Long orderItemId) {
        Order order = findOrder(orderId);

        OrderItem orderItem = order.getOrderItems().stream()
                .filter(item -> item.getId().equals(orderItemId))
                .findFirst()
                .orElseThrow(() -> new GlobalException(OrderExceptionCode.NOT_FOUND_ORDER_Item));

        orderSupportService.checkRequestRefundOrderItem(orderItem);
        orderSupportService.refundOrderItem(orderItem);
        return OrderMapper.INSTANCE.toOrderResponse(order);
    }
}
