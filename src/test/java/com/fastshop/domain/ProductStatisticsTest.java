package com.fastshop.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.fastshop.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductStatisticsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductStatistics.class);
        ProductStatistics productStatistics1 = new ProductStatistics();
        productStatistics1.setId(1L);
        ProductStatistics productStatistics2 = new ProductStatistics();
        productStatistics2.setId(productStatistics1.getId());
        assertThat(productStatistics1).isEqualTo(productStatistics2);
        productStatistics2.setId(2L);
        assertThat(productStatistics1).isNotEqualTo(productStatistics2);
        productStatistics1.setId(null);
        assertThat(productStatistics1).isNotEqualTo(productStatistics2);
    }
}
