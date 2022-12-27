package com.fastshop.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.fastshop.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductDiscountTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductDiscount.class);
        ProductDiscount productDiscount1 = new ProductDiscount();
        productDiscount1.setId(1L);
        ProductDiscount productDiscount2 = new ProductDiscount();
        productDiscount2.setId(productDiscount1.getId());
        assertThat(productDiscount1).isEqualTo(productDiscount2);
        productDiscount2.setId(2L);
        assertThat(productDiscount1).isNotEqualTo(productDiscount2);
        productDiscount1.setId(null);
        assertThat(productDiscount1).isNotEqualTo(productDiscount2);
    }
}
