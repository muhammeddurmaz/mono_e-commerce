package com.fastshop.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserCartMapperTest {

    private UserCartMapper userCartMapper;

    @BeforeEach
    public void setUp() {
        userCartMapper = new UserCartMapperImpl();
    }
}
