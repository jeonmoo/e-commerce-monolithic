package com.test.bootcamp.domain.payment.mapper;

import com.test.bootcamp.domain.payment.dto.PaymentResponse;
import com.test.bootcamp.domain.payment.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    PaymentResponse toResponse(Payment payment);
}
