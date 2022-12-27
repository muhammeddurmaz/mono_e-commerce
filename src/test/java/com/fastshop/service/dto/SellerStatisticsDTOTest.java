package com.fastshop.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.fastshop.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SellerStatisticsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SellerStatisticsDTO.class);
        SellerStatisticsDTO sellerStatisticsDTO1 = new SellerStatisticsDTO();
        sellerStatisticsDTO1.setId(1L);
        SellerStatisticsDTO sellerStatisticsDTO2 = new SellerStatisticsDTO();
        assertThat(sellerStatisticsDTO1).isNotEqualTo(sellerStatisticsDTO2);
        sellerStatisticsDTO2.setId(sellerStatisticsDTO1.getId());
        assertThat(sellerStatisticsDTO1).isEqualTo(sellerStatisticsDTO2);
        sellerStatisticsDTO2.setId(2L);
        assertThat(sellerStatisticsDTO1).isNotEqualTo(sellerStatisticsDTO2);
        sellerStatisticsDTO1.setId(null);
        assertThat(sellerStatisticsDTO1).isNotEqualTo(sellerStatisticsDTO2);
    }
}
