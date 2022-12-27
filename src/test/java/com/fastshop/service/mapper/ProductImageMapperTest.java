package com.fastshop.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductImageMapperTest {

    private ProductImageMapper productImageMapper;

    @BeforeEach
    public void setUp() {
        productImageMapper = new ProductImageMapperImpl();
    }
}
