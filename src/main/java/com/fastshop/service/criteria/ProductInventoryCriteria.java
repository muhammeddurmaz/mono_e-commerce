package com.fastshop.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.fastshop.domain.ProductInventory} entity. This class is used
 * in {@link com.fastshop.web.rest.ProductInventoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /product-inventories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductInventoryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter total;

    private LongFilter productId;

    private Boolean distinct;

    public ProductInventoryCriteria() {}

    public ProductInventoryCriteria(ProductInventoryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.total = other.total == null ? null : other.total.copy();
        this.productId = other.productId == null ? null : other.productId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ProductInventoryCriteria copy() {
        return new ProductInventoryCriteria(this);
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

    public IntegerFilter getTotal() {
        return total;
    }

    public IntegerFilter total() {
        if (total == null) {
            total = new IntegerFilter();
        }
        return total;
    }

    public void setTotal(IntegerFilter total) {
        this.total = total;
    }

    public LongFilter getProductId() {
        return productId;
    }

    public LongFilter productId() {
        if (productId == null) {
            productId = new LongFilter();
        }
        return productId;
    }

    public void setProductId(LongFilter productId) {
        this.productId = productId;
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
        final ProductInventoryCriteria that = (ProductInventoryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(total, that.total) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, total, productId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductInventoryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (total != null ? "total=" + total + ", " : "") +
            (productId != null ? "productId=" + productId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
