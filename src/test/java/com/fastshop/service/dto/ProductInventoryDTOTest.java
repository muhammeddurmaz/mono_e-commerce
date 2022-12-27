package com.fastshop.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.fastshop.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductInventoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductInventoryDTO.class);
        ProductInventoryDTO productInventoryDTO1 = new ProductInventoryDTO();
        productInventoryDTO1.setId(1L);
        ProductInventoryDTO productInventoryDTO2 = new ProductInventoryDTO();
        assertThat(productInventoryDTO1).isNotEqualTo(productInventoryDTO2);
        productInventoryDTO2.setId(productInventoryDTO1.getId());
        assertThat(productInventoryDTO1).isEqualTo(productInventoryDTO2);
        productInventoryDTO2.setId(2L);
        assertThat(productInventoryDTO1).isNotEqualTo(productInventoryDTO2);
        productInventoryDTO1.setId(null);
        assertThat(productInventoryDTO1).isNotEqualTo(productInventoryDTO2);
    }
}
