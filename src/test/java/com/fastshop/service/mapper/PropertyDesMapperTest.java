package com.fastshop.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PropertyDesMapperTest {

    private PropertyDesMapper propertyDesMapper;

    @BeforeEach
    public void setUp() {
        propertyDesMapper = new PropertyDesMapperImpl();
    }
}
