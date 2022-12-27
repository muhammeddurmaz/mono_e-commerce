package com.fastshop.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.fastshop.domain.ProductModel} entity. This class is used
 * in {@link com.fastshop.web.rest.ProductModelResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /product-models?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductModelCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter modelCode;

    private LongFilter productId;

    private Boolean distinct;

    public ProductModelCriteria() {}

    public ProductModelCriteria(ProductModelCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.modelCode = other.modelCode == null ? null : other.modelCode.copy();
        this.productId = other.productId == null ? null : other.productId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ProductModelCriteria copy() {
        return new ProductModelCriteria(this);
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

    public StringFilter getModelCode() {
        return modelCode;
    }

    public StringFilter modelCode() {
        if (modelCode == null) {
            modelCode = new StringFilter();
        }
        return modelCode;
    }

    public void setModelCode(StringFilter modelCode) {
        this.modelCode = modelCode;
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
        final ProductModelCriteria that = (ProductModelCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(modelCode, that.modelCode) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, modelCode, productId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductModelCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (modelCode != null ? "modelCode=" + modelCode + ", " : "") +
            (productId != null ? "productId=" + productId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
