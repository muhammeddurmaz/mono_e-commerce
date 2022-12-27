package com.fastshop.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.fastshop.domain.SubCategory} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubCategoryDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @Lob
    private byte[] image;

    private String imageContentType;
    private CategoryDTO category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubCategoryDTO)) {
            return false;
        }

        SubCategoryDTO subCategoryDTO = (SubCategoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, subCategoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubCategoryDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", image='" + getImage() + "'" +
            ", category=" + getCategory() +
            "}";
    }
}
