package com.test.ecommerce.domain.order.service;

import com.test.ecommerce.common.GlobalException;
import com.test.ecommerce.common.exceptionCode.OrderExceptionCode;
import com.test.ecommerce.common.exceptionCode.ProductExceptionCode;
import com.test.ecommerce.common.exceptionCode.UserExceptionCode;
import com.test.ecommerce.domain.order.dto.OrderRequest;
import com.test.ecommerce.domain.order.dto.OrderResponse;
import com.test.ecommerce.domain.order.entity.Order;
import com.test.ecommerce.domain.order.entity.OrderItem;
import com.test.ecommerce.domain.order.enums.OrderStatus;
import com.test.ecommerce.domain.order.mapper.OrderItemMapper;
import com.test.ecommerce.domain.order.mapper.OrderMapper;
import com.test.ecommerce.domain.payment.entity.Payment;
import com.test.ecommerce.domain.payment.enums.PaymentStatus;
import com.test.ecommerce.domain.payment.repository.PaymentRepository;
import com.test.ecommerce.domain.product.entity.Product;
import com.test.ecommerce.domain.product.repository.ProductRepository;
import com.test.ecommerce.domain.user.entity.User;
import com.test.ecommerce.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderSupportService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;

    private final StringRedisTemplate stringRedisTemplate;

    protected OrderResponse toResponse(Order order) {
        List<OrderResponse.OrderResponseItem> orderItemResponse = order.getOrderItems()
                .stream()
                .map(OrderItemMapper.INSTANCE::toOrderResponseItem)
                .toList();

        OrderResponse orderResponse = OrderMapper.INSTANCE.toOrderResponse(order);
        orderResponse.setOrderItems(orderItemResponse);
        return orderResponse;
    }

    protected Order initOrder(OrderRequest request, List<Product> products) {
        Order order = OrderMapper.INSTANCE.toOrder(request);
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new GlobalException(UserExceptionCode.NOT_FOUND_USER));
        order.setUser(user);

        List<OrderItem> orderItems = initOrderItem(request, products);
        orderItems.forEach(item -> item.setOrder(order));
        order.setOrderItems(orderItems);
        initPriceInOrder(order);
        return order;
    }

    private List<OrderItem> initOrderItem(OrderRequest request, List<Product> products) {
        Map<Long, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
        return request.getOrderItems().stream()
                .map(itemRequest -> {
                    Product product = productMap.get(itemRequest.getProductId());
                    return OrderItem.builder()
                            .quantity(itemRequest.getQuantity())
                            .originPrice(product.getOriginPrice())
                            .discountPrice(BigDecimal.ZERO)
                            .finalPrice(product.getFinalPrice())
                            .product(product)
                            .build();
                })
                .toList();
    }

    @Transactional
    protected List<Product> getOrderInProduct(OrderRequest request) {
        List<Long> productIds = request.getOrderItems().stream()
                .map(OrderRequest.OrderRequestItem::getProductId)
                .toList();

        List<Product> products = productRepository.findByIdIn(productIds);
        if (products.size() != request.getOrderItems().size()) {
            throw new GlobalException(ProductExceptionCode.NOT_FOUND_PRODUCT);
        }
        return products;
    }

    protected void checkProductStock(List<OrderRequest.OrderRequestItem> orderItems, List<Product> products) {
        Map<Long, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
        orderItems.forEach(item -> {
            Product product = productMap.get(item.getProductId());
            Integer stock = product.getQuantity();
            if (item.getQuantity() > stock) {
                throw new GlobalException(ProductExceptionCode.OUT_OF_STOCK);
            }
        });
    }

    @Transactional
    protected void pay(Order order) {
        Payment payment = Payment.builder()
                .orderId(order.getId())
                .paymentStatus(PaymentStatus.PAID)
                .payAmount(order.getTotalFinalPrice())
                .refundAmount(BigDecimal.ZERO)
                .build();

        paymentRepository.save(payment);
    }

    @Transactional
    protected void initPriceInOrder(Order order) {
        BigDecimal totalOriginPrice = order.getOrderItems().stream()
                .map(OrderItem::getOriginPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalDiscountPrice = order.getOrderItems().stream()
                .map(OrderItem::getDiscountPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalFinalPrice = order.getOrderItems().stream()
                .map(OrderItem::getFinalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalOriginPrice(totalOriginPrice);
        order.setTotalDiscountPrice(totalDiscountPrice);
        order.setTotalFinalPrice(totalFinalPrice);
    }

    @Transactional
    protected void reduceStock(List<OrderRequest.OrderRequestItem> orderItems, List<Product> products) {
        Map<Long, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
        orderItems.forEach(item -> {
            Product product = productMap.get(item.getProductId());
            Integer newStock = product.getQuantity() - item.getQuantity();
            product.setQuantity(newStock);
        });
    }

    @Transactional
    protected void completeOrder(Order order) {
        order.setOrderStatus(OrderStatus.COMPLETE);
        order.getOrderItems()
                .forEach(item -> item.setOrderStatus(OrderStatus.COMPLETE));
    }

    @Transactional
    protected void cancelOrder(Order order, String reason) {
        order.setOrderStatus(OrderStatus.CANCELED);

        Payment payment = Payment.builder()
                .orderId(order.getId())
                .paymentStatus(PaymentStatus.CANCELED)
                .payAmount(order.getTotalFinalPrice())
                .refundAmount(BigDecimal.ZERO)
                .reason(reason)
                .build();
        paymentRepository.save(payment);
    }

    protected void checkOrderPending(Order order) {
        OrderStatus status = order.getOrderStatus();
        if (!OrderStatus.PENDING.equals(status)) {
            throw new GlobalException(OrderExceptionCode.NOT_PENDING);
        }
    }

    protected void checkOrderRefundRequest(Order order) {
        OrderStatus status = order.getOrderStatus();
        if (!OrderStatus.COMPLETE.equals(status) && !OrderStatus.PARTIALLY_REFUNDED.equals(status)) {
            throw new GlobalException(OrderExceptionCode.INVALID_REFUND_REQUEST);
        }
    }

    protected void checkOrderItemRefundRequest(OrderItem item) {
        OrderStatus itemOrderStatus = item.getOrderStatus();
        if (!OrderStatus.COMPLETE.equals(itemOrderStatus)) {
            throw new GlobalException(OrderExceptionCode.INVALID_REFUND_REQUEST);
        }
    }

    protected void applyRefundRequestOrder(Order order) {
        order.setOrderStatus(OrderStatus.REQUIRED_REFUND);
        order.getOrderItems().forEach(item -> item.setOrderStatus(OrderStatus.REQUIRED_REFUND));
    }

    protected void applyRefundRequestOrderItem(OrderItem item) {
        item.setOrderStatus(OrderStatus.REQUIRED_REFUND);
        item.getOrder().setOrderStatus(OrderStatus.PARTIALLY_REQUIRED_REFUND);
    }

    protected void checkRequestRefundOrder(Order order) {
        OrderStatus status = order.getOrderStatus();
        if (!OrderStatus.REQUIRED_REFUND.equals(status)) {
            throw new GlobalException(OrderExceptionCode.NOT_REFUNDABLE_ORDER);
        }
    }

    protected void checkRequestRefundOrderItem(OrderItem item) {
        OrderStatus itemStatus = item.getOrderStatus();

        if (!OrderStatus.REQUIRED_REFUND.equals(itemStatus)) {
            throw new GlobalException(OrderExceptionCode.NOT_REFUNDABLE_ORDER_ITEM);
        }
    }

    @Transactional
    protected void refundOrder(Order order) {
        order.setOrderStatus(OrderStatus.REFUNDED);
        order.getOrderItems().forEach(item -> item.setOrderStatus(OrderStatus.REFUNDED));

        Payment payment = Payment.builder()
                .orderId(order.getId())
                .paymentStatus(PaymentStatus.REFUND)
                .payAmount(BigDecimal.ZERO)
                .refundAmount(order.getTotalFinalPrice())
                .build();
        paymentRepository.save(payment);
    }

    @Transactional
    protected void refundOrderItem(OrderItem item) {
        item.setOrderStatus(OrderStatus.REFUNDED);

        Order order = item.getOrder();
        Set<OrderStatus> orderStatusSet = order.getOrderItems().stream()
                        .map(OrderItem::getOrderStatus)
                                .collect(Collectors.toSet());
        if (orderStatusSet.size() == 1 && orderStatusSet.contains(OrderStatus.REFUNDED)) {
            order.setOrderStatus(OrderStatus.REFUNDED);
        } else {
            item.getOrder().setOrderStatus(OrderStatus.PARTIALLY_REFUNDED);
        }

        Payment payment = Payment.builder()
                .orderId(item.getOrder().getId())
                .orderItemId(item.getId())
                .paymentStatus(PaymentStatus.PARTIALLY_REFUNDED)
                .payAmount(item.getFinalPrice())
                .refundAmount(BigDecimal.ZERO)
                .build();
        paymentRepository.save(payment);
    }

    @Transactional
    protected void updateProductScore(List<OrderItem> items) {
        Map<Long, Integer> scoreMap = items.stream()
                .collect(Collectors.toMap(orderItem -> orderItem.getProduct().getId(), OrderItem::getQuantity));

        scoreMap.forEach((id, score) -> {
            String redisKey = "product:score";
            stringRedisTemplate.opsForZSet().incrementScore(redisKey, id.toString(), score);
        });
    }
}
