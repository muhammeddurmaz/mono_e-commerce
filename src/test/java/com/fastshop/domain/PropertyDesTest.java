package com.fastshop.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.fastshop.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PropertyDesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PropertyDes.class);
        PropertyDes propertyDes1 = new PropertyDes();
        propertyDes1.setId(1L);
        PropertyDes propertyDes2 = new PropertyDes();
        propertyDes2.setId(propertyDes1.getId());
        assertThat(propertyDes1).isEqualTo(propertyDes2);
        propertyDes2.setId(2L);
        assertThat(propertyDes1).isNotEqualTo(propertyDes2);
        propertyDes1.setId(null);
        assertThat(propertyDes1).isNotEqualTo(propertyDes2);
    }
}
