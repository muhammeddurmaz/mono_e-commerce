package com.fastshop.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.fastshop.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductDiscountDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductDiscountDTO.class);
        ProductDiscountDTO productDiscountDTO1 = new ProductDiscountDTO();
        productDiscountDTO1.setId(1L);
        ProductDiscountDTO productDiscountDTO2 = new ProductDiscountDTO();
        assertThat(productDiscountDTO1).isNotEqualTo(productDiscountDTO2);
        productDiscountDTO2.setId(productDiscountDTO1.getId());
        assertThat(productDiscountDTO1).isEqualTo(productDiscountDTO2);
        productDiscountDTO2.setId(2L);
        assertThat(productDiscountDTO1).isNotEqualTo(productDiscountDTO2);
        productDiscountDTO1.setId(null);
        assertThat(productDiscountDTO1).isNotEqualTo(productDiscountDTO2);
    }
}
