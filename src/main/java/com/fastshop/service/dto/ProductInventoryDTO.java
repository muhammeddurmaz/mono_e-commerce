package com.fastshop.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.fastshop.domain.ProductInventory} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductInventoryDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 0)
    private Integer total;

    private ProductDTO product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
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
        if (!(o instanceof ProductInventoryDTO)) {
            return false;
        }

        ProductInventoryDTO productInventoryDTO = (ProductInventoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productInventoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductInventoryDTO{" +
            "id=" + getId() +
            ", total=" + getTotal() +
            ", product=" + getProduct() +
            "}";
    }
}
