package com.test.ecommerce.domain.order.service;

import com.test.ecommerce.common.GlobalException;
import com.test.ecommerce.common.exceptionCode.OrderExceptionCode;
import com.test.ecommerce.domain.order.dto.OrderRequest;
import com.test.ecommerce.domain.order.dto.OrderResponse;
import com.test.ecommerce.domain.order.dto.RefundResponse;
import com.test.ecommerce.domain.order.entity.Order;
import com.test.ecommerce.domain.order.entity.OrderItem;
import com.test.ecommerce.domain.order.mapper.OrderMapper;
import com.test.ecommerce.domain.order.repository.OrderRepository;
import com.test.ecommerce.domain.payment.entity.Payment;
import com.test.ecommerce.domain.payment.enums.PaymentStatus;
import com.test.ecommerce.domain.payment.repository.PaymentRepository;
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
    private final PaymentRepository paymentRepository;

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
    public OrderResponse cancelOrder(Long orderId, OrderRequest request) {
        Order order = findOrder(orderId);
        String reason = request.getReason();

        orderSupportService.checkOrderPending(order);
        orderSupportService.cancelOrder(order, reason);
        return OrderMapper.INSTANCE.toOrderResponse(order);
    }

    @Transactional
    public OrderResponse requestRefundOrder(Long orderId) {
        Order order = findOrder(orderId);

        orderSupportService.checkOrderRefundRequest(order);
        orderSupportService.applyRefundRequestOrder(order);
        return OrderMapper.INSTANCE.toOrderResponse(order);
    }

    @Transactional
    public OrderResponse requestRefundOrderItem(Long orderId, Long orderItemId) {
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

    @Transactional
    public List<RefundResponse> getRefunds(Long orderId) {
        List<Payment> payments = paymentRepository.findByOrderIdAndPaymentStatus(orderId, PaymentStatus.REFUND);
        return payments.stream()
                .map(payment -> RefundResponse.builder()
                        .id(payment.getId())
                        .refundAmount(payment.getRefundAmount())
                        .reason(payment.getReason())
                        .createdAt(payment.getCreatedAt())
                        .build())
                .toList();


    }
}
