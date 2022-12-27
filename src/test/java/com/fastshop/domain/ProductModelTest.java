package com.fastshop.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.fastshop.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductModelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductModel.class);
        ProductModel productModel1 = new ProductModel();
        productModel1.setId(1L);
        ProductModel productModel2 = new ProductModel();
        productModel2.setId(productModel1.getId());
        assertThat(productModel1).isEqualTo(productModel2);
        productModel2.setId(2L);
        assertThat(productModel1).isNotEqualTo(productModel2);
        productModel1.setId(null);
        assertThat(productModel1).isNotEqualTo(productModel2);
    }
}
