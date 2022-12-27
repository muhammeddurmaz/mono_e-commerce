package com.fastshop.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.fastshop.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductInventoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductInventory.class);
        ProductInventory productInventory1 = new ProductInventory();
        productInventory1.setId(1L);
        ProductInventory productInventory2 = new ProductInventory();
        productInventory2.setId(productInventory1.getId());
        assertThat(productInventory1).isEqualTo(productInventory2);
        productInventory2.setId(2L);
        assertThat(productInventory1).isNotEqualTo(productInventory2);
        productInventory1.setId(null);
        assertThat(productInventory1).isNotEqualTo(productInventory2);
    }
}
