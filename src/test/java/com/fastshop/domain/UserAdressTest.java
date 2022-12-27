package com.fastshop.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.fastshop.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserAdressTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserAdress.class);
        UserAdress userAdress1 = new UserAdress();
        userAdress1.setId(1L);
        UserAdress userAdress2 = new UserAdress();
        userAdress2.setId(userAdress1.getId());
        assertThat(userAdress1).isEqualTo(userAdress2);
        userAdress2.setId(2L);
        assertThat(userAdress1).isNotEqualTo(userAdress2);
        userAdress1.setId(null);
        assertThat(userAdress1).isNotEqualTo(userAdress2);
    }
}
