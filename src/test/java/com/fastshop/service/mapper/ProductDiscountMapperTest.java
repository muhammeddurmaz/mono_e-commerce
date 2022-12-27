package com.fastshop.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductDiscountMapperTest {

    private ProductDiscountMapper productDiscountMapper;

    @BeforeEach
    public void setUp() {
        productDiscountMapper = new ProductDiscountMapperImpl();
    }
}
