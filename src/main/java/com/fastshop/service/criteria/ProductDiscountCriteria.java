package com.fastshop.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.fastshop.domain.ProductDiscount} entity. This class is used
 * in {@link com.fastshop.web.rest.ProductDiscountResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /product-discounts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductDiscountCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter addedDate;

    private InstantFilter dueDate;

    private StringFilter description;

    private LongFilter discountId;

    private LongFilter productId;

    private Boolean distinct;

    public ProductDiscountCriteria() {}

    public ProductDiscountCriteria(ProductDiscountCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.addedDate = other.addedDate == null ? null : other.addedDate.copy();
        this.dueDate = other.dueDate == null ? null : other.dueDate.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.discountId = other.discountId == null ? null : other.discountId.copy();
        this.productId = other.productId == null ? null : other.productId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ProductDiscountCriteria copy() {
        return new ProductDiscountCriteria(this);
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

    public InstantFilter getAddedDate() {
        return addedDate;
    }

    public InstantFilter addedDate() {
        if (addedDate == null) {
            addedDate = new InstantFilter();
        }
        return addedDate;
    }

    public void setAddedDate(InstantFilter addedDate) {
        this.addedDate = addedDate;
    }

    public InstantFilter getDueDate() {
        return dueDate;
    }

    public InstantFilter dueDate() {
        if (dueDate == null) {
            dueDate = new InstantFilter();
        }
        return dueDate;
    }

    public void setDueDate(InstantFilter dueDate) {
        this.dueDate = dueDate;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LongFilter getDiscountId() {
        return discountId;
    }

    public LongFilter discountId() {
        if (discountId == null) {
            discountId = new LongFilter();
        }
        return discountId;
    }

    public void setDiscountId(LongFilter discountId) {
        this.discountId = discountId;
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
        final ProductDiscountCriteria that = (ProductDiscountCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(addedDate, that.addedDate) &&
            Objects.equals(dueDate, that.dueDate) &&
            Objects.equals(description, that.description) &&
            Objects.equals(discountId, that.discountId) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, addedDate, dueDate, description, discountId, productId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductDiscountCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (addedDate != null ? "addedDate=" + addedDate + ", " : "") +
            (dueDate != null ? "dueDate=" + dueDate + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (discountId != null ? "discountId=" + discountId + ", " : "") +
            (productId != null ? "productId=" + productId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
