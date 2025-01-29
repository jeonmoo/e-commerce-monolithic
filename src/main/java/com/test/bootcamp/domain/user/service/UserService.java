package com.test.bootcamp.domain.user.service;

import com.test.bootcamp.common.GlobalException;
import com.test.bootcamp.common.exceptionCode.UserExceptionCode;
import com.test.bootcamp.domain.order.dto.OrderResponse;
import com.test.bootcamp.domain.order.entity.Order;
import com.test.bootcamp.domain.order.mapper.OrderMapper;
import com.test.bootcamp.domain.order.repository.OrderRepository;
import com.test.bootcamp.domain.user.dto.UserRequest;
import com.test.bootcamp.domain.user.dto.UserResponse;
import com.test.bootcamp.domain.user.entity.User;
import com.test.bootcamp.domain.user.enums.Rule;
import com.test.bootcamp.domain.user.mapper.UserMapper;
import com.test.bootcamp.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;


    private User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(UserExceptionCode.NOT_FOUND_USER));
    }

    public UserResponse getUser(Long userId) {
        User user = findById(userId);
        return UserMapper.INSTANCE.toResponse(user);
    }

    @Transactional
    public UserResponse createUser(UserRequest request) {
        User user = UserMapper.INSTANCE.toUser(request);
        user.setRule(Rule.USER);
        User savedUser = userRepository.save(user);
        return UserMapper.INSTANCE.toResponse(savedUser);
    }

    @Transactional
    public UserResponse modifyUser(Long userId, UserRequest request) {
        User user = findById(userId);

        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());

        return UserMapper.INSTANCE.toResponse(user);
    }

    public List<OrderResponse> getOrderInUser(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .map(OrderMapper.INSTANCE::toOrderResponse)
                .toList();
    }
}
