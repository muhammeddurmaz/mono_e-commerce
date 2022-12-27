package com.fastshop.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.fastshop.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserAdressDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserAdressDTO.class);
        UserAdressDTO userAdressDTO1 = new UserAdressDTO();
        userAdressDTO1.setId(1L);
        UserAdressDTO userAdressDTO2 = new UserAdressDTO();
        assertThat(userAdressDTO1).isNotEqualTo(userAdressDTO2);
        userAdressDTO2.setId(userAdressDTO1.getId());
        assertThat(userAdressDTO1).isEqualTo(userAdressDTO2);
        userAdressDTO2.setId(2L);
        assertThat(userAdressDTO1).isNotEqualTo(userAdressDTO2);
        userAdressDTO1.setId(null);
        assertThat(userAdressDTO1).isNotEqualTo(userAdressDTO2);
    }
}
