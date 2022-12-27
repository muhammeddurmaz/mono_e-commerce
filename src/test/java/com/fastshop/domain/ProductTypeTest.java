package com.fastshop.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.fastshop.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductType.class);
        ProductType productType1 = new ProductType();
        productType1.setId(1L);
        ProductType productType2 = new ProductType();
        productType2.setId(productType1.getId());
        assertThat(productType1).isEqualTo(productType2);
        productType2.setId(2L);
        assertThat(productType1).isNotEqualTo(productType2);
        productType1.setId(null);
        assertThat(productType1).isNotEqualTo(productType2);
    }
}
