package com.test.bootcamp.domain.order.service;

import com.test.bootcamp.common.GlobalException;
import com.test.bootcamp.common.Util;
import com.test.bootcamp.common.exceptionCode.OrderExceptionCode;
import com.test.bootcamp.common.exceptionCode.ProductExceptionCode;
import com.test.bootcamp.common.exceptionCode.UserExceptionCode;
import com.test.bootcamp.domain.order.dto.OrderRequest;
import com.test.bootcamp.domain.order.dto.OrderResponse;
import com.test.bootcamp.domain.order.entity.Order;
import com.test.bootcamp.domain.order.entity.OrderItem;
import com.test.bootcamp.domain.order.enums.DiscountType;
import com.test.bootcamp.domain.order.enums.OrderStatus;
import com.test.bootcamp.domain.order.mapper.OrderItemMapper;
import com.test.bootcamp.domain.order.mapper.OrderMapper;
import com.test.bootcamp.domain.payment.entity.Payment;
import com.test.bootcamp.domain.payment.enums.PaymentStatus;
import com.test.bootcamp.domain.payment.repository.PaymentRepository;
import com.test.bootcamp.domain.product.entity.Product;
import com.test.bootcamp.domain.product.repository.ProductRepository;
import com.test.bootcamp.domain.user.entity.User;
import com.test.bootcamp.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderSupportService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;

    protected OrderResponse toResponse(Order order) {
        List<OrderResponse.OrderItem> orderItemResponse = order.getOrderItems()
                .stream()
                .map(OrderItemMapper.INSTANCE::toOrderItemResponse)
                .toList();

        OrderResponse orderResponse = OrderMapper.INSTANCE.toOrderResponse(order);
        orderResponse.setOrderItems(orderItemResponse);
        return orderResponse;
    }

    protected Order initOrder(OrderRequest request, List<Product> products) {
        Order order = OrderMapper.INSTANCE.toOrder(request);
        order.setOrderStatus(OrderStatus.PENDING);
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new GlobalException(UserExceptionCode.NOT_FOUND_USER));
        order.setUser(user);

        List<OrderItem> orderItems = initOrderItem(request, products);
        orderItems.forEach(item -> item.setOrder(order));

        order.setOrderItems(orderItems);
        return order;
    }

    private List<OrderItem> initOrderItem(OrderRequest request, List<Product> products) {
        Map<Long, Product> productMap = Util.toMap(products, Product::getId);
        return request.getOrderItems().stream()
                .map(itemRequest -> {
                    Product product = productMap.get(itemRequest.getProductId());
                    return OrderItem.builder()
                            .quantity(itemRequest.getQuantity())
                            .discountType(product.getDiscountType())
                            .discountValue(product.getDiscountValue())
                            .originPrice(product.getOriginPrice())
                            .finalPrice(product.getFinalPrice())
                            .product(product)
                            .build();
                })
                .toList();
    }

    @Transactional
    protected List<Product> getOrderInProduct(OrderRequest request) {
        List<Long> productIds = request.getOrderItems().stream()
                .map(OrderRequest.OrderItem::getProductId)
                .toList();

        List<Product> products = productRepository.findByIdIn(productIds);
        if (products.size() != request.getOrderItems().size()) {
            throw new GlobalException(ProductExceptionCode.NOT_FOUND_PRODUCT);
        }
        return products;
    }

    protected void checkProductStack(List<OrderRequest.OrderItem> orderItems, List<Product> products) {
        Map<Long, Product> productMap = Util.toMap(products, Product::getId);
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
                .order(order)
                .paymentStatus(PaymentStatus.PAID)
                .payAmount(order.getTotalFinalPrice())
                .build();

        paymentRepository.save(payment);
    }

    @Transactional
    protected void initFinalPrice(Product product) {
        BigDecimal finalPrice = calculateDiscountPrice(product.getDiscountType(), product.getOriginPrice(), product.getDiscountValue());
        product.setFinalPrice(finalPrice);
    }

    @Transactional
    protected BigDecimal calculateDiscountPrice(DiscountType discountType, BigDecimal originPrice, BigDecimal discountValue) {
        BigDecimal resultPrice = BigDecimal.ZERO;
        switch (discountType) {
            case AMOUNT -> resultPrice = originPrice.subtract(discountValue);
            case RATE -> resultPrice = originPrice.subtract(originPrice.multiply(discountValue).divide(BigDecimal.valueOf(100d), RoundingMode.HALF_UP));
        }
        return resultPrice;
    }

    @Transactional
    protected void reduceStock(List<OrderRequest.OrderItem> orderItems, List<Product> products) {
        Map<Long, Product> productMap = Util.toMap(products, Product::getId);
        orderItems.forEach(item -> {
            Product product = productMap.get(item.getProductId());
            Integer newStock = product.getQuantity() - item.getQuantity();
            product.setQuantity(newStock);
        });
    }

    @Transactional
    protected void orderComplete(Order order) {
        order.setOrderStatus(OrderStatus.COMPLETE);
    }

    @Transactional
    protected void orderCancel(Order order) {
        order.setOrderStatus(OrderStatus.CANCELED);
    }

    protected void checkOrderPending(Order order) {
        OrderStatus status = order.getOrderStatus();
        if (!OrderStatus.PENDING.equals(status)) {
            throw new GlobalException(OrderExceptionCode.NOT_PENDING);
        }
    }
}
