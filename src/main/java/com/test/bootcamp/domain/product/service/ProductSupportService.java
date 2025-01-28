package com.test.bootcamp.domain.product.service;

import com.test.bootcamp.common.GlobalException;
import com.test.bootcamp.common.exceptionCode.DiscountExceptionCode;
import com.test.bootcamp.domain.discount.entity.Discount;
import com.test.bootcamp.domain.discount.enums.DiscountType;
import com.test.bootcamp.domain.discount.repository.DiscountRepository;
import com.test.bootcamp.domain.product.dto.ProductRequest;
import com.test.bootcamp.domain.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class ProductSupportService {

    private final DiscountRepository discountRepository;

    @Transactional
    protected void initDiscountToProduct(Product product, ProductRequest request) {
        Discount discount = discountRepository.findById(request.getDiscountId())
                .orElseThrow(() -> new GlobalException(DiscountExceptionCode.NOT_FOUND_DISCOUNT));

        checkDiscountTypeCommon(discount.getDiscountType());

        BigDecimal finalPrice = calculateFinalPrice(product, discount);
        product.setFinalPrice(finalPrice);
    }

    @Transactional
    protected BigDecimal calculateFinalPrice(Product product, Discount discount) {
        BigDecimal resultPrice = BigDecimal.ZERO;
        DiscountType discountType = discount.getDiscountType();
        BigDecimal discountValue = discount.getDiscountAmount();
        BigDecimal originPrice = product.getOriginPrice();

        switch (discountType) {
            case AMOUNT -> resultPrice = originPrice.subtract(discountValue);
            case RATE -> resultPrice = originPrice.subtract(originPrice.multiply(discountValue).divide(BigDecimal.valueOf(100d), RoundingMode.HALF_UP));
        }
        return resultPrice;
    }

    private void checkDiscountTypeCommon(DiscountType discountType) {
        if (!DiscountType.COMMON.equals(discountType)) {
            throw new GlobalException(DiscountExceptionCode.NOT_COMMON_TYPE);
        }
    }
}
