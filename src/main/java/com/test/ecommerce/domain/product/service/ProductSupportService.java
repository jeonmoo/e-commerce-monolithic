package com.test.ecommerce.domain.product.service;

import com.test.ecommerce.common.GlobalException;
import com.test.ecommerce.common.exceptionCode.DiscountExceptionCode;
import com.test.ecommerce.domain.discount.enums.DiscountType;
import com.test.ecommerce.domain.discount.repository.DiscountRepository;
import com.test.ecommerce.domain.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ProductSupportService {

    private final DiscountRepository discountRepository;

    @Transactional
    protected void initDiscountToProduct(Product product, BigDecimal discountPrice) {
        BigDecimal originPrice = product.getOriginPrice();
        BigDecimal finalPrice = calculateFinalPrice(originPrice, discountPrice);
        product.setFinalPrice(finalPrice);
        product.setDiscountPrice(discountPrice);
    }

    @Transactional
    protected BigDecimal calculateFinalPrice(BigDecimal originPrice, BigDecimal discountPrice) {
        return originPrice.subtract(discountPrice);
    }

//    @Transactional
//    protected BigDecimal calculateFinalPrice(Product product, Discount discount) {
//        BigDecimal resultPrice = BigDecimal.ZERO;
//        DiscountType discountType = discount.getDiscountType();
//        BigDecimal discountValue = discount.getDiscountAmount();
//        BigDecimal originPrice = product.getOriginPrice();
//
//        switch (discountType) {
//            case AMOUNT -> resultPrice = originPrice.subtract(discountValue);
//            case RATE -> resultPrice = originPrice.subtract(originPrice.multiply(discountValue).divide(BigDecimal.valueOf(100d), RoundingMode.HALF_UP));
//        }
//        return resultPrice;
//    }

    private void checkDiscountTypeCommon(DiscountType discountType) {
        if (!DiscountType.COMMON.equals(discountType)) {
            throw new GlobalException(DiscountExceptionCode.NOT_COMMON_TYPE);
        }
    }
}
