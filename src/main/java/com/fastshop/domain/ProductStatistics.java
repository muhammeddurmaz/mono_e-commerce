package com.fastshop.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProductStatistics.
 */
@Entity
@Table(name = "product_statistics")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductStatistics implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "jhi_order")
    private Integer order;

    @Column(name = "click")
    private Integer click;

    @Column(name = "comment")
    private Integer comment;

    @Column(name = "rating")
    private Float rating;

    @Column(name = "add_cart")
    private Integer addCart;

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
    @OneToOne
    @JoinColumn(unique = true)
    private Product product;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductStatistics id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrder() {
        return this.order;
    }

    public ProductStatistics order(Integer order) {
        this.setOrder(order);
        return this;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getClick() {
        return this.click;
    }

    public ProductStatistics click(Integer click) {
        this.setClick(click);
        return this;
    }

    public void setClick(Integer click) {
        this.click = click;
    }

    public Integer getComment() {
        return this.comment;
    }

    public ProductStatistics comment(Integer comment) {
        this.setComment(comment);
        return this;
    }

    public void setComment(Integer comment) {
        this.comment = comment;
    }

    public Float getRating() {
        return this.rating;
    }

    public ProductStatistics rating(Float rating) {
        this.setRating(rating);
        return this;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Integer getAddCart() {
        return this.addCart;
    }

    public ProductStatistics addCart(Integer addCart) {
        this.setAddCart(addCart);
        return this;
    }

    public void setAddCart(Integer addCart) {
        this.addCart = addCart;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductStatistics product(Product product) {
        this.setProduct(product);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductStatistics)) {
            return false;
        }
        return id != null && id.equals(((ProductStatistics) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductStatistics{" +
            "id=" + getId() +
            ", order=" + getOrder() +
            ", click=" + getClick() +
            ", comment=" + getComment() +
            ", rating=" + getRating() +
            ", addCart=" + getAddCart() +
            "}";
    }
}
