package com.test.ecommerce.domain.user.service;

import com.test.ecommerce.common.GlobalException;
import com.test.ecommerce.common.exceptionCode.UserExceptionCode;
import com.test.ecommerce.domain.order.dto.OrderResponse;
import com.test.ecommerce.domain.order.entity.Order;
import com.test.ecommerce.domain.order.enums.OrderStatus;
import com.test.ecommerce.domain.order.mapper.OrderItemMapper;
import com.test.ecommerce.domain.order.mapper.OrderMapper;
import com.test.ecommerce.domain.order.repository.OrderRepository;
import com.test.ecommerce.domain.user.dto.UserCreateRequest;
import com.test.ecommerce.domain.user.dto.UserModifyRequest;
import com.test.ecommerce.domain.user.dto.UserResponse;
import com.test.ecommerce.domain.user.entity.User;
import com.test.ecommerce.domain.user.enums.UserRole;
import com.test.ecommerce.domain.user.mapper.UserMapper;
import com.test.ecommerce.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new GlobalException(UserExceptionCode.NOT_FOUND_USER));
    }

    @Cacheable(value = "user", key = "#id")
    public UserResponse getUser(Long id) {
        User user = findById(id);
        return UserMapper.INSTANCE.toResponse(user);
    }

    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        User user = UserMapper.INSTANCE.toUser(request);
        user.setUserRole(UserRole.USER);
        User savedUser = userRepository.save(user);
        return UserMapper.INSTANCE.toResponse(savedUser);
    }

    @Transactional
    @CachePut(value = "user", key = "#id")
    public UserResponse modifyUser(Long id, UserModifyRequest request) {
        User user = findById(id);
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        return UserMapper.INSTANCE.toResponse(user);
    }

    public List<OrderResponse> getOrderInUser(Long id) {
        List<Order> orders = orderRepository.findByUserId(id);
        return orders.stream()
                .map(order -> {
                    List<OrderResponse.OrderResponseItem> orderResponseItem = order.getOrderItems().stream()
                            .map(OrderItemMapper.INSTANCE::toOrderResponseItem)
                            .toList();

                    OrderResponse orderResponse = OrderMapper.INSTANCE.toOrderResponse(order);
                    orderResponse.setOrderItems(orderResponseItem);
                    return orderResponse;
                }).toList();
    }

    public List<OrderResponse> getRefunds(Long userId) {
        List<Order> orders = orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.REFUNDED);

        return orders.stream()
                .map(order -> {
            List<OrderResponse.OrderResponseItem> orderResponseItem = order.getOrderItems().stream()
                    .map(OrderItemMapper.INSTANCE::toOrderResponseItem)
                    .toList();

            OrderResponse orderResponse = OrderMapper.INSTANCE.toOrderResponse(order);
            orderResponse.setOrderItems(orderResponseItem);
            return orderResponse;
        }).toList();
    }
}
