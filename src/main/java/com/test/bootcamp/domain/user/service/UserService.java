package com.test.bootcamp.domain.user.service;

import com.test.bootcamp.common.GlobalException;
import com.test.bootcamp.common.UserExceptionCode;
import com.test.bootcamp.domain.user.dto.UserRequest;
import com.test.bootcamp.domain.user.dto.UserResponse;
import com.test.bootcamp.domain.user.entity.User;
import com.test.bootcamp.domain.user.mapper.UserMapper;
import com.test.bootcamp.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(UserExceptionCode.NOT_FOUND_USER));
        return UserMapper.INSTANCE.toResponse(user);
    }

    @Transactional
    public UserResponse createUser(UserRequest request) {
        User user = UserMapper.INSTANCE.toUser(request);
        User savedUser = userRepository.save(user);
        return UserMapper.INSTANCE.toResponse(savedUser);
    }

    @Transactional
    public UserResponse modifyUser(UserRequest request) {
        User user = userRepository.findById(request.getId())
                .orElseThrow(() -> new GlobalException(UserExceptionCode.NOT_FOUND_USER));

        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());

        return UserMapper.INSTANCE.toResponse(user);
    }
}
