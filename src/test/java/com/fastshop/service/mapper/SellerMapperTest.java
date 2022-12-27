package com.fastshop.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SellerMapperTest {

    private SellerMapper sellerMapper;

    @BeforeEach
    public void setUp() {
        sellerMapper = new SellerMapperImpl();
    }
}
