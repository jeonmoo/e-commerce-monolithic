package com.test.bootcamp.domain.discount.controller;

import com.test.bootcamp.domain.discount.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountService discountService;
}
