package com.fastshop.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.fastshop.domain.SellerStatistics} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SellerStatisticsDTO implements Serializable {

    private Long id;

    private Integer product;

    private Integer totalOrder;

    private BigDecimal totalEarning;

    private SellerDTO seller;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getProduct() {
        return product;
    }

    public void setProduct(Integer product) {
        this.product = product;
    }

    public Integer getTotalOrder() {
        return totalOrder;
    }

    public void setTotalOrder(Integer totalOrder) {
        this.totalOrder = totalOrder;
    }

    public BigDecimal getTotalEarning() {
        return totalEarning;
    }

    public void setTotalEarning(BigDecimal totalEarning) {
        this.totalEarning = totalEarning;
    }

    public SellerDTO getSeller() {
        return seller;
    }

    public void setSeller(SellerDTO seller) {
        this.seller = seller;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SellerStatisticsDTO)) {
            return false;
        }

        SellerStatisticsDTO sellerStatisticsDTO = (SellerStatisticsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sellerStatisticsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SellerStatisticsDTO{" +
            "id=" + getId() +
            ", product=" + getProduct() +
            ", totalOrder=" + getTotalOrder() +
            ", totalEarning=" + getTotalEarning() +
            ", seller=" + getSeller() +
            "}";
    }
}
