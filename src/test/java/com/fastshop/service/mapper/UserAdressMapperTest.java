package com.fastshop.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserAdressMapperTest {

    private UserAdressMapper userAdressMapper;

    @BeforeEach
    public void setUp() {
        userAdressMapper = new UserAdressMapperImpl();
    }
}
