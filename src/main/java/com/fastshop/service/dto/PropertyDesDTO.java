package com.fastshop.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.fastshop.domain.PropertyDes} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PropertyDesDTO implements Serializable {

    private Long id;

    @NotNull
    private String detail;

    private PropertyDTO property;

    private ProductDTO product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public PropertyDTO getProperty() {
        return property;
    }

    public void setProperty(PropertyDTO property) {
        this.property = property;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PropertyDesDTO)) {
            return false;
        }

        PropertyDesDTO propertyDesDTO = (PropertyDesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, propertyDesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PropertyDesDTO{" +
            "id=" + getId() +
            ", detail='" + getDetail() + "'" +
            ", property=" + getProperty() +
            ", product=" + getProduct() +
            "}";
    }
}
