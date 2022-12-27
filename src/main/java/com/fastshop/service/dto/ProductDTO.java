package com.fastshop.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.fastshop.domain.Product} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductDTO implements Serializable {

    private Long id;

    @NotNull
    private String barcode;

    @NotNull
    private String modelCode;

    @NotNull
    @Size(min = 5)
    private String name;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal price;

    private BigDecimal discountPrice;

    @Lob
    private String description;

    @Lob
    private byte[] image;

    private String imageContentType;

    @NotNull
    private Instant addedDate;

    private Float rating;

    private String sizee;

    @NotNull
    @Min(value = 0)
    private Integer stock;

    private Boolean active;

    private CategoryDTO category;

    private SubCategoryDTO subCategory;

    private ColorDTO color;

    private ProductModelDTO productModel;

    private SellerDTO seller;

    private BrandDTO brand;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Instant getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Instant addedDate) {
        this.addedDate = addedDate;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getSizee() {
        return sizee;
    }

    public void setSizee(String sizee) {
        this.sizee = sizee;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    public SubCategoryDTO getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(SubCategoryDTO subCategory) {
        this.subCategory = subCategory;
    }

    public ColorDTO getColor() {
        return color;
    }

    public void setColor(ColorDTO color) {
        this.color = color;
    }

    public ProductModelDTO getProductModel() {
        return productModel;
    }

    public void setProductModel(ProductModelDTO productModel) {
        this.productModel = productModel;
    }

    public SellerDTO getSeller() {
        return seller;
    }

    public void setSeller(SellerDTO seller) {
        this.seller = seller;
    }

    public BrandDTO getBrand() {
        return brand;
    }

    public void setBrand(BrandDTO brand) {
        this.brand = brand;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductDTO)) {
            return false;
        }

        ProductDTO productDTO = (ProductDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductDTO{" +
            "id=" + getId() +
            ", barcode='" + getBarcode() + "'" +
            ", modelCode='" + getModelCode() + "'" +
            ", name='" + getName() + "'" +
            ", price=" + getPrice() +
            ", discountPrice=" + getDiscountPrice() +
            ", description='" + getDescription() + "'" +
            ", image='" + getImage() + "'" +
            ", addedDate='" + getAddedDate() + "'" +
            ", rating=" + getRating() +
            ", sizee='" + getSizee() + "'" +
            ", stock=" + getStock() +
            ", active='" + getActive() + "'" +
            ", category=" + getCategory() +
            ", subCategory=" + getSubCategory() +
            ", color=" + getColor() +
            ", productModel=" + getProductModel() +
            ", seller=" + getSeller() +
            ", brand=" + getBrand() +
            "}";
    }
}
