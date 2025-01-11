package com.test.bootcamp.domain.user.mapper;

import com.test.bootcamp.domain.user.dto.UserRequest;
import com.test.bootcamp.domain.user.dto.UserResponse;
import com.test.bootcamp.domain.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserResponse toResponse(User entity);

    User toUser(UserRequest request);
}
