package com.fastshop.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.fastshop.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserCartTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserCart.class);
        UserCart userCart1 = new UserCart();
        userCart1.setId(1L);
        UserCart userCart2 = new UserCart();
        userCart2.setId(userCart1.getId());
        assertThat(userCart1).isEqualTo(userCart2);
        userCart2.setId(2L);
        assertThat(userCart1).isNotEqualTo(userCart2);
        userCart1.setId(null);
        assertThat(userCart1).isNotEqualTo(userCart2);
    }
}
