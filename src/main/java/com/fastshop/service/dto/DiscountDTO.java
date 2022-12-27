package com.fastshop.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.fastshop.domain.Discount} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DiscountDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    @Lob
    private byte[] image;

    private String imageContentType;
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DiscountDTO)) {
            return false;
        }

        DiscountDTO discountDTO = (DiscountDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, discountDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DiscountDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", image='" + getImage() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
