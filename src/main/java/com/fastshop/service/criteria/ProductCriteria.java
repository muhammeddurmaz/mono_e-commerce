package com.fastshop.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.fastshop.domain.Product} entity. This class is used
 * in {@link com.fastshop.web.rest.ProductResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /products?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter barcode;

    private StringFilter modelCode;

    private StringFilter name;

    private BigDecimalFilter price;

    private BigDecimalFilter discountPrice;

    private InstantFilter addedDate;

    private FloatFilter rating;

    private StringFilter sizee;

    private IntegerFilter stock;

    private BooleanFilter active;

    private LongFilter productDiscountId;

    private LongFilter propertyDetailsId;

    private LongFilter productInventoryId;

    private LongFilter commentId;

    private LongFilter categoryId;

    private LongFilter subCategoryId;

    private LongFilter colorId;

    private LongFilter productStatisticsId;

    private LongFilter productModelId;

    private LongFilter sellerId;

    private LongFilter brandId;

    private Boolean distinct;

    public ProductCriteria() {}

    public ProductCriteria(ProductCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.barcode = other.barcode == null ? null : other.barcode.copy();
        this.modelCode = other.modelCode == null ? null : other.modelCode.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.discountPrice = other.discountPrice == null ? null : other.discountPrice.copy();
        this.addedDate = other.addedDate == null ? null : other.addedDate.copy();
        this.rating = other.rating == null ? null : other.rating.copy();
        this.sizee = other.sizee == null ? null : other.sizee.copy();
        this.stock = other.stock == null ? null : other.stock.copy();
        this.active = other.active == null ? null : other.active.copy();
        this.productDiscountId = other.productDiscountId == null ? null : other.productDiscountId.copy();
        this.propertyDetailsId = other.propertyDetailsId == null ? null : other.propertyDetailsId.copy();
        this.productInventoryId = other.productInventoryId == null ? null : other.productInventoryId.copy();
        this.commentId = other.commentId == null ? null : other.commentId.copy();
        this.categoryId = other.categoryId == null ? null : other.categoryId.copy();
        this.subCategoryId = other.subCategoryId == null ? null : other.subCategoryId.copy();
        this.colorId = other.colorId == null ? null : other.colorId.copy();
        this.productStatisticsId = other.productStatisticsId == null ? null : other.productStatisticsId.copy();
        this.productModelId = other.productModelId == null ? null : other.productModelId.copy();
        this.sellerId = other.sellerId == null ? null : other.sellerId.copy();
        this.brandId = other.brandId == null ? null : other.brandId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ProductCriteria copy() {
        return new ProductCriteria(this);
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

    public StringFilter getBarcode() {
        return barcode;
    }

    public StringFilter barcode() {
        if (barcode == null) {
            barcode = new StringFilter();
        }
        return barcode;
    }

    public void setBarcode(StringFilter barcode) {
        this.barcode = barcode;
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public BigDecimalFilter getPrice() {
        return price;
    }

    public BigDecimalFilter price() {
        if (price == null) {
            price = new BigDecimalFilter();
        }
        return price;
    }

    public void setPrice(BigDecimalFilter price) {
        this.price = price;
    }

    public BigDecimalFilter getDiscountPrice() {
        return discountPrice;
    }

    public BigDecimalFilter discountPrice() {
        if (discountPrice == null) {
            discountPrice = new BigDecimalFilter();
        }
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimalFilter discountPrice) {
        this.discountPrice = discountPrice;
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

    public FloatFilter getRating() {
        return rating;
    }

    public FloatFilter rating() {
        if (rating == null) {
            rating = new FloatFilter();
        }
        return rating;
    }

    public void setRating(FloatFilter rating) {
        this.rating = rating;
    }

    public StringFilter getSizee() {
        return sizee;
    }

    public StringFilter sizee() {
        if (sizee == null) {
            sizee = new StringFilter();
        }
        return sizee;
    }

    public void setSizee(StringFilter sizee) {
        this.sizee = sizee;
    }

    public IntegerFilter getStock() {
        return stock;
    }

    public IntegerFilter stock() {
        if (stock == null) {
            stock = new IntegerFilter();
        }
        return stock;
    }

    public void setStock(IntegerFilter stock) {
        this.stock = stock;
    }

    public BooleanFilter getActive() {
        return active;
    }

    public BooleanFilter active() {
        if (active == null) {
            active = new BooleanFilter();
        }
        return active;
    }

    public void setActive(BooleanFilter active) {
        this.active = active;
    }

    public LongFilter getProductDiscountId() {
        return productDiscountId;
    }

    public LongFilter productDiscountId() {
        if (productDiscountId == null) {
            productDiscountId = new LongFilter();
        }
        return productDiscountId;
    }

    public void setProductDiscountId(LongFilter productDiscountId) {
        this.productDiscountId = productDiscountId;
    }

    public LongFilter getPropertyDetailsId() {
        return propertyDetailsId;
    }

    public LongFilter propertyDetailsId() {
        if (propertyDetailsId == null) {
            propertyDetailsId = new LongFilter();
        }
        return propertyDetailsId;
    }

    public void setPropertyDetailsId(LongFilter propertyDetailsId) {
        this.propertyDetailsId = propertyDetailsId;
    }

    public LongFilter getProductInventoryId() {
        return productInventoryId;
    }

    public LongFilter productInventoryId() {
        if (productInventoryId == null) {
            productInventoryId = new LongFilter();
        }
        return productInventoryId;
    }

    public void setProductInventoryId(LongFilter productInventoryId) {
        this.productInventoryId = productInventoryId;
    }

    public LongFilter getCommentId() {
        return commentId;
    }

    public LongFilter commentId() {
        if (commentId == null) {
            commentId = new LongFilter();
        }
        return commentId;
    }

    public void setCommentId(LongFilter commentId) {
        this.commentId = commentId;
    }

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public LongFilter categoryId() {
        if (categoryId == null) {
            categoryId = new LongFilter();
        }
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
    }

    public LongFilter getSubCategoryId() {
        return subCategoryId;
    }

    public LongFilter subCategoryId() {
        if (subCategoryId == null) {
            subCategoryId = new LongFilter();
        }
        return subCategoryId;
    }

    public void setSubCategoryId(LongFilter subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public LongFilter getColorId() {
        return colorId;
    }

    public LongFilter colorId() {
        if (colorId == null) {
            colorId = new LongFilter();
        }
        return colorId;
    }

    public void setColorId(LongFilter colorId) {
        this.colorId = colorId;
    }

    public LongFilter getProductStatisticsId() {
        return productStatisticsId;
    }

    public LongFilter productStatisticsId() {
        if (productStatisticsId == null) {
            productStatisticsId = new LongFilter();
        }
        return productStatisticsId;
    }

    public void setProductStatisticsId(LongFilter productStatisticsId) {
        this.productStatisticsId = productStatisticsId;
    }

    public LongFilter getProductModelId() {
        return productModelId;
    }

    public LongFilter productModelId() {
        if (productModelId == null) {
            productModelId = new LongFilter();
        }
        return productModelId;
    }

    public void setProductModelId(LongFilter productModelId) {
        this.productModelId = productModelId;
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

    public LongFilter getBrandId() {
        return brandId;
    }

    public LongFilter brandId() {
        if (brandId == null) {
            brandId = new LongFilter();
        }
        return brandId;
    }

    public void setBrandId(LongFilter brandId) {
        this.brandId = brandId;
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
        final ProductCriteria that = (ProductCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(barcode, that.barcode) &&
            Objects.equals(modelCode, that.modelCode) &&
            Objects.equals(name, that.name) &&
            Objects.equals(price, that.price) &&
            Objects.equals(discountPrice, that.discountPrice) &&
            Objects.equals(addedDate, that.addedDate) &&
            Objects.equals(rating, that.rating) &&
            Objects.equals(sizee, that.sizee) &&
            Objects.equals(stock, that.stock) &&
            Objects.equals(active, that.active) &&
            Objects.equals(productDiscountId, that.productDiscountId) &&
            Objects.equals(propertyDetailsId, that.propertyDetailsId) &&
            Objects.equals(productInventoryId, that.productInventoryId) &&
            Objects.equals(commentId, that.commentId) &&
            Objects.equals(categoryId, that.categoryId) &&
            Objects.equals(subCategoryId, that.subCategoryId) &&
            Objects.equals(colorId, that.colorId) &&
            Objects.equals(productStatisticsId, that.productStatisticsId) &&
            Objects.equals(productModelId, that.productModelId) &&
            Objects.equals(sellerId, that.sellerId) &&
            Objects.equals(brandId, that.brandId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            barcode,
            modelCode,
            name,
            price,
            discountPrice,
            addedDate,
            rating,
            sizee,
            stock,
            active,
            productDiscountId,
            propertyDetailsId,
            productInventoryId,
            commentId,
            categoryId,
            subCategoryId,
            colorId,
            productStatisticsId,
            productModelId,
            sellerId,
            brandId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (barcode != null ? "barcode=" + barcode + ", " : "") +
            (modelCode != null ? "modelCode=" + modelCode + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (price != null ? "price=" + price + ", " : "") +
            (discountPrice != null ? "discountPrice=" + discountPrice + ", " : "") +
            (addedDate != null ? "addedDate=" + addedDate + ", " : "") +
            (rating != null ? "rating=" + rating + ", " : "") +
            (sizee != null ? "sizee=" + sizee + ", " : "") +
            (stock != null ? "stock=" + stock + ", " : "") +
            (active != null ? "active=" + active + ", " : "") +
            (productDiscountId != null ? "productDiscountId=" + productDiscountId + ", " : "") +
            (propertyDetailsId != null ? "propertyDetailsId=" + propertyDetailsId + ", " : "") +
            (productInventoryId != null ? "productInventoryId=" + productInventoryId + ", " : "") +
            (commentId != null ? "commentId=" + commentId + ", " : "") +
            (categoryId != null ? "categoryId=" + categoryId + ", " : "") +
            (subCategoryId != null ? "subCategoryId=" + subCategoryId + ", " : "") +
            (colorId != null ? "colorId=" + colorId + ", " : "") +
            (productStatisticsId != null ? "productStatisticsId=" + productStatisticsId + ", " : "") +
            (productModelId != null ? "productModelId=" + productModelId + ", " : "") +
            (sellerId != null ? "sellerId=" + sellerId + ", " : "") +
            (brandId != null ? "brandId=" + brandId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
