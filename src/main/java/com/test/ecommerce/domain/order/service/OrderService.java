package com.test.ecommerce.domain.order.service;

import com.test.ecommerce.common.GlobalException;
import com.test.ecommerce.common.exceptionCode.OrderExceptionCode;
import com.test.ecommerce.domain.order.dto.OrderCreateRequest;
import com.test.ecommerce.domain.order.dto.OrderResponse;
import com.test.ecommerce.domain.order.entity.Order;
import com.test.ecommerce.domain.order.entity.OrderItem;
import com.test.ecommerce.domain.order.mapper.OrderMapper;
import com.test.ecommerce.domain.order.repository.OrderRepository;
import com.test.ecommerce.domain.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderSupportService orderSupportService;
    private final OrderRepository orderRepository;

    private Order findOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new GlobalException(OrderExceptionCode.NOT_FOUND_ORDER));
    }

    public OrderResponse getOrder(Long id) {
        Order order = findOrder(id);
        return orderSupportService.toResponse(order);
    }

    @Transactional
    public OrderResponse registerOrder(OrderCreateRequest request) {
        List<Product> products = orderSupportService.getOrderInProduct(request);
        orderSupportService.checkProductStock(request.getOrderItems(), products);
        orderSupportService.reduceStock(request.getOrderItems(), products);

        Order order = orderSupportService.initOrder(request, products);
        Order savedOrder = orderRepository.save(order);
        orderSupportService.pay(savedOrder);

        orderSupportService.updateProductScore(order.getOrderItems());

        return orderSupportService.toResponse(savedOrder);
    }

    @Transactional
    public OrderResponse completeOrder(Long id) {
        Order order = findOrder(id);

        orderSupportService.checkOrderPending(order);
        orderSupportService.completeOrder(order);
        return OrderMapper.INSTANCE.toOrderResponse(order);
    }

    @Transactional
    public OrderResponse cancelOrder(Long id, OrderCreateRequest request) {
        Order order = findOrder(id);
        String reason = request.getReason();

        orderSupportService.checkOrderPending(order);
        orderSupportService.cancelOrder(order, reason);
        return OrderMapper.INSTANCE.toOrderResponse(order);
    }

    @Transactional
    public OrderResponse requestRefundOrder(Long id) {
        Order order = findOrder(id);

        orderSupportService.checkOrderRefundRequest(order);
        orderSupportService.applyRefundRequestOrder(order);
        return OrderMapper.INSTANCE.toOrderResponse(order);
    }

    @Transactional
    public OrderResponse requestRefundOrderItem(Long id, Long orderItemId) {
        Order order = findOrder(id);

        OrderItem orderItem = order.getOrderItems().stream()
                .filter(item -> item.getId().equals(orderItemId))
                .findFirst()
                .orElseThrow(() -> new GlobalException(OrderExceptionCode.NOT_FOUND_ORDER_ITEM));

        orderSupportService.checkOrderItemRefundRequest(orderItem);
        orderSupportService.applyRefundRequestOrderItem(orderItem);
        return OrderMapper.INSTANCE.toOrderResponse(order);
    }

    @Transactional
    public OrderResponse refundOrder(Long id) {
        Order order = findOrder(id);

        orderSupportService.checkRequestRefundOrder(order);
        orderSupportService.refundOrder(order);
        return OrderMapper.INSTANCE.toOrderResponse(order);
    }

    @Transactional
    public OrderResponse refundOrderItem(Long id, Long orderItemId) {
        Order order = findOrder(id);

        OrderItem orderItem = order.getOrderItems().stream()
                .filter(item -> item.getId().equals(orderItemId))
                .findFirst()
                .orElseThrow(() -> new GlobalException(OrderExceptionCode.NOT_FOUND_ORDER_ITEM));

        orderSupportService.checkRequestRefundOrderItem(orderItem);
        orderSupportService.refundOrderItem(orderItem);
        return OrderMapper.INSTANCE.toOrderResponse(order);
    }
}
