package com.test.ecommerce.domain.payment.mapper;

import com.test.ecommerce.domain.payment.dto.PaymentResponse;
import com.test.ecommerce.domain.payment.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    PaymentResponse toResponse(Payment payment);
}
