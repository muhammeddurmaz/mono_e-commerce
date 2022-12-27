package com.fastshop.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.fastshop.domain.ProductDiscount} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductDiscountDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant addedDate;

    @NotNull
    private Instant dueDate;

    private String description;

    private DiscountDTO discount;

    private ProductDTO product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Instant addedDate) {
        this.addedDate = addedDate;
    }

    public Instant getDueDate() {
        return dueDate;
    }

    public void setDueDate(Instant dueDate) {
        this.dueDate = dueDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DiscountDTO getDiscount() {
        return discount;
    }

    public void setDiscount(DiscountDTO discount) {
        this.discount = discount;
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
        if (!(o instanceof ProductDiscountDTO)) {
            return false;
        }

        ProductDiscountDTO productDiscountDTO = (ProductDiscountDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productDiscountDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductDiscountDTO{" +
            "id=" + getId() +
            ", addedDate='" + getAddedDate() + "'" +
            ", dueDate='" + getDueDate() + "'" +
            ", description='" + getDescription() + "'" +
            ", discount=" + getDiscount() +
            ", product=" + getProduct() +
            "}";
    }
}
