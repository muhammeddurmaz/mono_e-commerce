package com.fastshop.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.fastshop.domain.PropertyDes} entity. This class is used
 * in {@link com.fastshop.web.rest.PropertyDesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /property-des?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PropertyDesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter detail;

    private LongFilter propertyId;

    private LongFilter productId;

    private Boolean distinct;

    public PropertyDesCriteria() {}

    public PropertyDesCriteria(PropertyDesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.detail = other.detail == null ? null : other.detail.copy();
        this.propertyId = other.propertyId == null ? null : other.propertyId.copy();
        this.productId = other.productId == null ? null : other.productId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PropertyDesCriteria copy() {
        return new PropertyDesCriteria(this);
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

    public StringFilter getDetail() {
        return detail;
    }

    public StringFilter detail() {
        if (detail == null) {
            detail = new StringFilter();
        }
        return detail;
    }

    public void setDetail(StringFilter detail) {
        this.detail = detail;
    }

    public LongFilter getPropertyId() {
        return propertyId;
    }

    public LongFilter propertyId() {
        if (propertyId == null) {
            propertyId = new LongFilter();
        }
        return propertyId;
    }

    public void setPropertyId(LongFilter propertyId) {
        this.propertyId = propertyId;
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
        final PropertyDesCriteria that = (PropertyDesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(detail, that.detail) &&
            Objects.equals(propertyId, that.propertyId) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, detail, propertyId, productId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PropertyDesCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (detail != null ? "detail=" + detail + ", " : "") +
            (propertyId != null ? "propertyId=" + propertyId + ", " : "") +
            (productId != null ? "productId=" + productId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
