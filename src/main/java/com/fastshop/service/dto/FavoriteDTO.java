package com.fastshop.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.fastshop.domain.Favorite} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FavoriteDTO implements Serializable {

    private Long id;

    private ProductDTO product;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FavoriteDTO)) {
            return false;
        }

        FavoriteDTO favoriteDTO = (FavoriteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, favoriteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FavoriteDTO{" +
            "id=" + getId() +
            ", product=" + getProduct() +
            ", user=" + getUser() +
            "}";
    }
}
