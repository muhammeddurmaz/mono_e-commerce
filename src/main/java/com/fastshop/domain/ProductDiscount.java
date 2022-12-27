package com.fastshop.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProductDiscount.
 */
@Entity
@Table(name = "product_discount")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "productdiscount")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductDiscount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "added_date", nullable = false)
    private Instant addedDate;

    @NotNull
    @Column(name = "due_date", nullable = false)
    private Instant dueDate;

    @Column(name = "description")
    private String description;

    @ManyToOne(optional = false)
    @NotNull
    private Discount discount;

    @ManyToOne(optional = false)
    @NotNull
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
    private Product product;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductDiscount id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getAddedDate() {
        return this.addedDate;
    }

    public ProductDiscount addedDate(Instant addedDate) {
        this.setAddedDate(addedDate);
        return this;
    }

    public void setAddedDate(Instant addedDate) {
        this.addedDate = addedDate;
    }

    public Instant getDueDate() {
        return this.dueDate;
    }

    public ProductDiscount dueDate(Instant dueDate) {
        this.setDueDate(dueDate);
        return this;
    }

    public void setDueDate(Instant dueDate) {
        this.dueDate = dueDate;
    }

    public String getDescription() {
        return this.description;
    }

    public ProductDiscount description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Discount getDiscount() {
        return this.discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public ProductDiscount discount(Discount discount) {
        this.setDiscount(discount);
        return this;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductDiscount product(Product product) {
        this.setProduct(product);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductDiscount)) {
            return false;
        }
        return id != null && id.equals(((ProductDiscount) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductDiscount{" +
            "id=" + getId() +
            ", addedDate='" + getAddedDate() + "'" +
            ", dueDate='" + getDueDate() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
