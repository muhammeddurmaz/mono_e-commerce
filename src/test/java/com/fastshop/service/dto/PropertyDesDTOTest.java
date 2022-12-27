package com.fastshop.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.fastshop.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PropertyDesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PropertyDesDTO.class);
        PropertyDesDTO propertyDesDTO1 = new PropertyDesDTO();
        propertyDesDTO1.setId(1L);
        PropertyDesDTO propertyDesDTO2 = new PropertyDesDTO();
        assertThat(propertyDesDTO1).isNotEqualTo(propertyDesDTO2);
        propertyDesDTO2.setId(propertyDesDTO1.getId());
        assertThat(propertyDesDTO1).isEqualTo(propertyDesDTO2);
        propertyDesDTO2.setId(2L);
        assertThat(propertyDesDTO1).isNotEqualTo(propertyDesDTO2);
        propertyDesDTO1.setId(null);
        assertThat(propertyDesDTO1).isNotEqualTo(propertyDesDTO2);
    }
}
