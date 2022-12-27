package com.fastshop.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.fastshop.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserCartDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserCartDTO.class);
        UserCartDTO userCartDTO1 = new UserCartDTO();
        userCartDTO1.setId(1L);
        UserCartDTO userCartDTO2 = new UserCartDTO();
        assertThat(userCartDTO1).isNotEqualTo(userCartDTO2);
        userCartDTO2.setId(userCartDTO1.getId());
        assertThat(userCartDTO1).isEqualTo(userCartDTO2);
        userCartDTO2.setId(2L);
        assertThat(userCartDTO1).isNotEqualTo(userCartDTO2);
        userCartDTO1.setId(null);
        assertThat(userCartDTO1).isNotEqualTo(userCartDTO2);
    }
}
