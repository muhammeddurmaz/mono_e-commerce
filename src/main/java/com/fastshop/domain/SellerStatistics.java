package com.fastshop.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SellerStatistics.
 */
@Entity
@Table(name = "seller_statistics")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "sellerstatistics")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SellerStatistics implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "product")
    private Integer product;

    @Column(name = "total_order")
    private Integer totalOrder;

    @Column(name = "total_earning", precision = 21, scale = 2)
    private BigDecimal totalEarning;

    @JsonIgnoreProperties(value = { "user", "products", "sellerProductType", "brand", "sellerStatistics" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Seller seller;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SellerStatistics id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getProduct() {
        return this.product;
    }

    public SellerStatistics product(Integer product) {
        this.setProduct(product);
        return this;
    }

    public void setProduct(Integer product) {
        this.product = product;
    }

    public Integer getTotalOrder() {
        return this.totalOrder;
    }

    public SellerStatistics totalOrder(Integer totalOrder) {
        this.setTotalOrder(totalOrder);
        return this;
    }

    public void setTotalOrder(Integer totalOrder) {
        this.totalOrder = totalOrder;
    }

    public BigDecimal getTotalEarning() {
        return this.totalEarning;
    }

    public SellerStatistics totalEarning(BigDecimal totalEarning) {
        this.setTotalEarning(totalEarning);
        return this;
    }

    public void setTotalEarning(BigDecimal totalEarning) {
        this.totalEarning = totalEarning;
    }

    public Seller getSeller() {
        return this.seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public SellerStatistics seller(Seller seller) {
        this.setSeller(seller);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SellerStatistics)) {
            return false;
        }
        return id != null && id.equals(((SellerStatistics) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SellerStatistics{" +
            "id=" + getId() +
            ", product=" + getProduct() +
            ", totalOrder=" + getTotalOrder() +
            ", totalEarning=" + getTotalEarning() +
            "}";
    }
}
