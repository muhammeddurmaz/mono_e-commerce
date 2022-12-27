package com.fastshop.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProductModel.
 */
@Entity
@Table(name = "product_model")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "model_code")
    private String modelCode;

    @OneToMany(mappedBy = "productModel")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
    private Set<Product> products = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductModel id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModelCode() {
        return this.modelCode;
    }

    public ProductModel modelCode(String modelCode) {
        this.setModelCode(modelCode);
        return this;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.setProductModel(null));
        }
        if (products != null) {
            products.forEach(i -> i.setProductModel(this));
        }
        this.products = products;
    }

    public ProductModel products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public ProductModel addProduct(Product product) {
        this.products.add(product);
        product.setProductModel(this);
        return this;
    }

    public ProductModel removeProduct(Product product) {
        this.products.remove(product);
        product.setProductModel(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductModel)) {
            return false;
        }
        return id != null && id.equals(((ProductModel) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductModel{" +
            "id=" + getId() +
            ", modelCode='" + getModelCode() + "'" +
            "}";
    }
}
