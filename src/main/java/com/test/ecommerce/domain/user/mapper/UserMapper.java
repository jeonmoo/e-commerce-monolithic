package com.test.ecommerce.domain.user.mapper;

import com.test.ecommerce.domain.user.dto.UserCreateRequest;
import com.test.ecommerce.domain.user.dto.UserResponse;
import com.test.ecommerce.domain.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserResponse toResponse(User entity);

    User toUser(UserCreateRequest request);
}
