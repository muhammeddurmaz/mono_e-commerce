package com.fastshop.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProductImage.
 */
@Entity
@Table(name = "product_image")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductImage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "image_1", nullable = false)
    private byte[] image1;

    @NotNull
    @Column(name = "image_1_content_type", nullable = false)
    private String image1ContentType;

    @Lob
    @Column(name = "image_2")
    private byte[] image2;

    @Column(name = "image_2_content_type")
    private String image2ContentType;

    @Lob
    @Column(name = "image_3")
    private byte[] image3;

    @Column(name = "image_3_content_type")
    private String image3ContentType;

    @Lob
    @Column(name = "image_4")
    private byte[] image4;

    @Column(name = "image_4_content_type")
    private String image4ContentType;

    @Lob
    @Column(name = "image_5")
    private byte[] image5;

    @Column(name = "image_5_content_type")
    private String image5ContentType;

    @Lob
    @Column(name = "image_6")
    private byte[] image6;

    @Column(name = "image_6_content_type")
    private String image6ContentType;

    @JsonIgnoreProperties(
        value = {
            "productDiscounts",
            "propertyDetails",
            "productInventories",
            "comments",
            "category",
            "subCategory",
            "color",
            "productStatistics",
            "productModel",
            "seller",
            "brand",
        },
        allowSetters = true
    )
    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Product product;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductImage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getImage1() {
        return this.image1;
    }

    public ProductImage image1(byte[] image1) {
        this.setImage1(image1);
        return this;
    }

    public void setImage1(byte[] image1) {
        this.image1 = image1;
    }

    public String getImage1ContentType() {
        return this.image1ContentType;
    }

    public ProductImage image1ContentType(String image1ContentType) {
        this.image1ContentType = image1ContentType;
        return this;
    }

    public void setImage1ContentType(String image1ContentType) {
        this.image1ContentType = image1ContentType;
    }

    public byte[] getImage2() {
        return this.image2;
    }

    public ProductImage image2(byte[] image2) {
        this.setImage2(image2);
        return this;
    }

    public void setImage2(byte[] image2) {
        this.image2 = image2;
    }

    public String getImage2ContentType() {
        return this.image2ContentType;
    }

    public ProductImage image2ContentType(String image2ContentType) {
        this.image2ContentType = image2ContentType;
        return this;
    }

    public void setImage2ContentType(String image2ContentType) {
        this.image2ContentType = image2ContentType;
    }

    public byte[] getImage3() {
        return this.image3;
    }

    public ProductImage image3(byte[] image3) {
        this.setImage3(image3);
        return this;
    }

    public void setImage3(byte[] image3) {
        this.image3 = image3;
    }

    public String getImage3ContentType() {
        return this.image3ContentType;
    }

    public ProductImage image3ContentType(String image3ContentType) {
        this.image3ContentType = image3ContentType;
        return this;
    }

    public void setImage3ContentType(String image3ContentType) {
        this.image3ContentType = image3ContentType;
    }

    public byte[] getImage4() {
        return this.image4;
    }

    public ProductImage image4(byte[] image4) {
        this.setImage4(image4);
        return this;
    }

    public void setImage4(byte[] image4) {
        this.image4 = image4;
    }

    public String getImage4ContentType() {
        return this.image4ContentType;
    }

    public ProductImage image4ContentType(String image4ContentType) {
        this.image4ContentType = image4ContentType;
        return this;
    }

    public void setImage4ContentType(String image4ContentType) {
        this.image4ContentType = image4ContentType;
    }

    public byte[] getImage5() {
        return this.image5;
    }

    public ProductImage image5(byte[] image5) {
        this.setImage5(image5);
        return this;
    }

    public void setImage5(byte[] image5) {
        this.image5 = image5;
    }

    public String getImage5ContentType() {
        return this.image5ContentType;
    }

    public ProductImage image5ContentType(String image5ContentType) {
        this.image5ContentType = image5ContentType;
        return this;
    }

    public void setImage5ContentType(String image5ContentType) {
        this.image5ContentType = image5ContentType;
    }

    public byte[] getImage6() {
        return this.image6;
    }

    public ProductImage image6(byte[] image6) {
        this.setImage6(image6);
        return this;
    }

    public void setImage6(byte[] image6) {
        this.image6 = image6;
    }

    public String getImage6ContentType() {
        return this.image6ContentType;
    }

    public ProductImage image6ContentType(String image6ContentType) {
        this.image6ContentType = image6ContentType;
        return this;
    }

    public void setImage6ContentType(String image6ContentType) {
        this.image6ContentType = image6ContentType;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductImage product(Product product) {
        this.setProduct(product);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductImage)) {
            return false;
        }
        return id != null && id.equals(((ProductImage) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductImage{" +
            "id=" + getId() +
            ", image1='" + getImage1() + "'" +
            ", image1ContentType='" + getImage1ContentType() + "'" +
            ", image2='" + getImage2() + "'" +
            ", image2ContentType='" + getImage2ContentType() + "'" +
            ", image3='" + getImage3() + "'" +
            ", image3ContentType='" + getImage3ContentType() + "'" +
            ", image4='" + getImage4() + "'" +
            ", image4ContentType='" + getImage4ContentType() + "'" +
            ", image5='" + getImage5() + "'" +
            ", image5ContentType='" + getImage5ContentType() + "'" +
            ", image6='" + getImage6() + "'" +
            ", image6ContentType='" + getImage6ContentType() + "'" +
            "}";
    }
}
