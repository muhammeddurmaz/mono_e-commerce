package com.fastshop.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SellerStatisticsMapperTest {

    private SellerStatisticsMapper sellerStatisticsMapper;

    @BeforeEach
    public void setUp() {
        sellerStatisticsMapper = new SellerStatisticsMapperImpl();
    }
}
