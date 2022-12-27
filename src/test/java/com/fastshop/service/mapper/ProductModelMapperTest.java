package com.fastshop.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductModelMapperTest {

    private ProductModelMapper productModelMapper;

    @BeforeEach
    public void setUp() {
        productModelMapper = new ProductModelMapperImpl();
    }
}
