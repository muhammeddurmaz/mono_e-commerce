package com.fastshop.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.fastshop.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductModelDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductModelDTO.class);
        ProductModelDTO productModelDTO1 = new ProductModelDTO();
        productModelDTO1.setId(1L);
        ProductModelDTO productModelDTO2 = new ProductModelDTO();
        assertThat(productModelDTO1).isNotEqualTo(productModelDTO2);
        productModelDTO2.setId(productModelDTO1.getId());
        assertThat(productModelDTO1).isEqualTo(productModelDTO2);
        productModelDTO2.setId(2L);
        assertThat(productModelDTO1).isNotEqualTo(productModelDTO2);
        productModelDTO1.setId(null);
        assertThat(productModelDTO1).isNotEqualTo(productModelDTO2);
    }
}
