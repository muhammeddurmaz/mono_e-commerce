package com.fastshop.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.fastshop.domain.ProductImage} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductImageDTO implements Serializable {

    private Long id;

    @Lob
    private byte[] image1;

    private String image1ContentType;

    @Lob
    private byte[] image2;

    private String image2ContentType;

    @Lob
    private byte[] image3;

    private String image3ContentType;

    @Lob
    private byte[] image4;

    private String image4ContentType;

    @Lob
    private byte[] image5;

    private String image5ContentType;

    @Lob
    private byte[] image6;

    private String image6ContentType;
    private ProductDTO product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getImage1() {
        return image1;
    }

    public void setImage1(byte[] image1) {
        this.image1 = image1;
    }

    public String getImage1ContentType() {
        return image1ContentType;
    }

    public void setImage1ContentType(String image1ContentType) {
        this.image1ContentType = image1ContentType;
    }

    public byte[] getImage2() {
        return image2;
    }

    public void setImage2(byte[] image2) {
        this.image2 = image2;
    }

    public String getImage2ContentType() {
        return image2ContentType;
    }

    public void setImage2ContentType(String image2ContentType) {
        this.image2ContentType = image2ContentType;
    }

    public byte[] getImage3() {
        return image3;
    }

    public void setImage3(byte[] image3) {
        this.image3 = image3;
    }

    public String getImage3ContentType() {
        return image3ContentType;
    }

    public void setImage3ContentType(String image3ContentType) {
        this.image3ContentType = image3ContentType;
    }

    public byte[] getImage4() {
        return image4;
    }

    public void setImage4(byte[] image4) {
        this.image4 = image4;
    }

    public String getImage4ContentType() {
        return image4ContentType;
    }

    public void setImage4ContentType(String image4ContentType) {
        this.image4ContentType = image4ContentType;
    }

    public byte[] getImage5() {
        return image5;
    }

    public void setImage5(byte[] image5) {
        this.image5 = image5;
    }

    public String getImage5ContentType() {
        return image5ContentType;
    }

    public void setImage5ContentType(String image5ContentType) {
        this.image5ContentType = image5ContentType;
    }

    public byte[] getImage6() {
        return image6;
    }

    public void setImage6(byte[] image6) {
        this.image6 = image6;
    }

    public String getImage6ContentType() {
        return image6ContentType;
    }

    public void setImage6ContentType(String image6ContentType) {
        this.image6ContentType = image6ContentType;
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
        if (!(o instanceof ProductImageDTO)) {
            return false;
        }

        ProductImageDTO productImageDTO = (ProductImageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productImageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductImageDTO{" +
            "id=" + getId() +
            ", image1='" + getImage1() + "'" +
            ", image2='" + getImage2() + "'" +
            ", image3='" + getImage3() + "'" +
            ", image4='" + getImage4() + "'" +
            ", image5='" + getImage5() + "'" +
            ", image6='" + getImage6() + "'" +
            ", product=" + getProduct() +
            "}";
    }
}
