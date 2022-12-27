package com.fastshop.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.fastshop.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductImageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductImage.class);
        ProductImage productImage1 = new ProductImage();
        productImage1.setId(1L);
        ProductImage productImage2 = new ProductImage();
        productImage2.setId(productImage1.getId());
        assertThat(productImage1).isEqualTo(productImage2);
        productImage2.setId(2L);
        assertThat(productImage1).isNotEqualTo(productImage2);
        productImage1.setId(null);
        assertThat(productImage1).isNotEqualTo(productImage2);
    }
}
