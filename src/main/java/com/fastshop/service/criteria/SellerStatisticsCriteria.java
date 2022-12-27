package com.fastshop.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.fastshop.domain.SellerStatistics} entity. This class is used
 * in {@link com.fastshop.web.rest.SellerStatisticsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /seller-statistics?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SellerStatisticsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter product;

    private IntegerFilter totalOrder;

    private BigDecimalFilter totalEarning;

    private LongFilter sellerId;

    private Boolean distinct;

    public SellerStatisticsCriteria() {}

    public SellerStatisticsCriteria(SellerStatisticsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.product = other.product == null ? null : other.product.copy();
        this.totalOrder = other.totalOrder == null ? null : other.totalOrder.copy();
        this.totalEarning = other.totalEarning == null ? null : other.totalEarning.copy();
        this.sellerId = other.sellerId == null ? null : other.sellerId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SellerStatisticsCriteria copy() {
        return new SellerStatisticsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getProduct() {
        return product;
    }

    public IntegerFilter product() {
        if (product == null) {
            product = new IntegerFilter();
        }
        return product;
    }

    public void setProduct(IntegerFilter product) {
        this.product = product;
    }

    public IntegerFilter getTotalOrder() {
        return totalOrder;
    }

    public IntegerFilter totalOrder() {
        if (totalOrder == null) {
            totalOrder = new IntegerFilter();
        }
        return totalOrder;
    }

    public void setTotalOrder(IntegerFilter totalOrder) {
        this.totalOrder = totalOrder;
    }

    public BigDecimalFilter getTotalEarning() {
        return totalEarning;
    }

    public BigDecimalFilter totalEarning() {
        if (totalEarning == null) {
            totalEarning = new BigDecimalFilter();
        }
        return totalEarning;
    }

    public void setTotalEarning(BigDecimalFilter totalEarning) {
        this.totalEarning = totalEarning;
    }

    public LongFilter getSellerId() {
        return sellerId;
    }

    public LongFilter sellerId() {
        if (sellerId == null) {
            sellerId = new LongFilter();
        }
        return sellerId;
    }

    public void setSellerId(LongFilter sellerId) {
        this.sellerId = sellerId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SellerStatisticsCriteria that = (SellerStatisticsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(product, that.product) &&
            Objects.equals(totalOrder, that.totalOrder) &&
            Objects.equals(totalEarning, that.totalEarning) &&
            Objects.equals(sellerId, that.sellerId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, product, totalOrder, totalEarning, sellerId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SellerStatisticsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (product != null ? "product=" + product + ", " : "") +
            (totalOrder != null ? "totalOrder=" + totalOrder + ", " : "") +
            (totalEarning != null ? "totalEarning=" + totalEarning + ", " : "") +
            (sellerId != null ? "sellerId=" + sellerId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
