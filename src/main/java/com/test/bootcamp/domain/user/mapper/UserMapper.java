package com.test.bootcamp.domain.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

//    @Mapping(source = "email", target = "emailAddress")
//    UserDto toDto(UserEntity entity);
}
