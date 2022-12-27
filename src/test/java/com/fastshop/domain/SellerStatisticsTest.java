package com.fastshop.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.fastshop.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SellerStatisticsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SellerStatistics.class);
        SellerStatistics sellerStatistics1 = new SellerStatistics();
        sellerStatistics1.setId(1L);
        SellerStatistics sellerStatistics2 = new SellerStatistics();
        sellerStatistics2.setId(sellerStatistics1.getId());
        assertThat(sellerStatistics1).isEqualTo(sellerStatistics2);
        sellerStatistics2.setId(2L);
        assertThat(sellerStatistics1).isNotEqualTo(sellerStatistics2);
        sellerStatistics1.setId(null);
        assertThat(sellerStatistics1).isNotEqualTo(sellerStatistics2);
    }
}
