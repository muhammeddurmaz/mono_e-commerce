package com.fastshop.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.fastshop.domain.Seller} entity. This class is used
 * in {@link com.fastshop.web.rest.SellerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sellers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SellerCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter lastName;

    private StringFilter shopName;

    private StringFilter mail;

    private BooleanFilter activated;

    private StringFilter tckn;

    private StringFilter phone;

    private StringFilter city;

    private InstantFilter placedDate;

    private LongFilter userId;

    private LongFilter productsId;

    private LongFilter sellerProductTypeId;

    private LongFilter brandId;

    private LongFilter sellerStatisticsId;

    private Boolean distinct;

    public SellerCriteria() {}

    public SellerCriteria(SellerCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.shopName = other.shopName == null ? null : other.shopName.copy();
        this.mail = other.mail == null ? null : other.mail.copy();
        this.activated = other.activated == null ? null : other.activated.copy();
        this.tckn = other.tckn == null ? null : other.tckn.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.city = other.city == null ? null : other.city.copy();
        this.placedDate = other.placedDate == null ? null : other.placedDate.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.productsId = other.productsId == null ? null : other.productsId.copy();
        this.sellerProductTypeId = other.sellerProductTypeId == null ? null : other.sellerProductTypeId.copy();
        this.brandId = other.brandId == null ? null : other.brandId.copy();
        this.sellerStatisticsId = other.sellerStatisticsId == null ? null : other.sellerStatisticsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SellerCriteria copy() {
        return new SellerCriteria(this);
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

    public StringFilter getLastName() {
        return lastName;
    }

    public StringFilter lastName() {
        if (lastName == null) {
            lastName = new StringFilter();
        }
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getShopName() {
        return shopName;
    }

    public StringFilter shopName() {
        if (shopName == null) {
            shopName = new StringFilter();
        }
        return shopName;
    }

    public void setShopName(StringFilter shopName) {
        this.shopName = shopName;
    }

    public StringFilter getMail() {
        return mail;
    }

    public StringFilter mail() {
        if (mail == null) {
            mail = new StringFilter();
        }
        return mail;
    }

    public void setMail(StringFilter mail) {
        this.mail = mail;
    }

    public BooleanFilter getActivated() {
        return activated;
    }

    public BooleanFilter activated() {
        if (activated == null) {
            activated = new BooleanFilter();
        }
        return activated;
    }

    public void setActivated(BooleanFilter activated) {
        this.activated = activated;
    }

    public StringFilter getTckn() {
        return tckn;
    }

    public StringFilter tckn() {
        if (tckn == null) {
            tckn = new StringFilter();
        }
        return tckn;
    }

    public void setTckn(StringFilter tckn) {
        this.tckn = tckn;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public StringFilter phone() {
        if (phone == null) {
            phone = new StringFilter();
        }
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getCity() {
        return city;
    }

    public StringFilter city() {
        if (city == null) {
            city = new StringFilter();
        }
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public InstantFilter getPlacedDate() {
        return placedDate;
    }

    public InstantFilter placedDate() {
        if (placedDate == null) {
            placedDate = new InstantFilter();
        }
        return placedDate;
    }

    public void setPlacedDate(InstantFilter placedDate) {
        this.placedDate = placedDate;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getProductsId() {
        return productsId;
    }

    public LongFilter productsId() {
        if (productsId == null) {
            productsId = new LongFilter();
        }
        return productsId;
    }

    public void setProductsId(LongFilter productsId) {
        this.productsId = productsId;
    }

    public LongFilter getSellerProductTypeId() {
        return sellerProductTypeId;
    }

    public LongFilter sellerProductTypeId() {
        if (sellerProductTypeId == null) {
            sellerProductTypeId = new LongFilter();
        }
        return sellerProductTypeId;
    }

    public void setSellerProductTypeId(LongFilter sellerProductTypeId) {
        this.sellerProductTypeId = sellerProductTypeId;
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

    public LongFilter getSellerStatisticsId() {
        return sellerStatisticsId;
    }

    public LongFilter sellerStatisticsId() {
        if (sellerStatisticsId == null) {
            sellerStatisticsId = new LongFilter();
        }
        return sellerStatisticsId;
    }

    public void setSellerStatisticsId(LongFilter sellerStatisticsId) {
        this.sellerStatisticsId = sellerStatisticsId;
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
        final SellerCriteria that = (SellerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(shopName, that.shopName) &&
            Objects.equals(mail, that.mail) &&
            Objects.equals(activated, that.activated) &&
            Objects.equals(tckn, that.tckn) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(city, that.city) &&
            Objects.equals(placedDate, that.placedDate) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(productsId, that.productsId) &&
            Objects.equals(sellerProductTypeId, that.sellerProductTypeId) &&
            Objects.equals(brandId, that.brandId) &&
            Objects.equals(sellerStatisticsId, that.sellerStatisticsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            lastName,
            shopName,
            mail,
            activated,
            tckn,
            phone,
            city,
            placedDate,
            userId,
            productsId,
            sellerProductTypeId,
            brandId,
            sellerStatisticsId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SellerCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (lastName != null ? "lastName=" + lastName + ", " : "") +
            (shopName != null ? "shopName=" + shopName + ", " : "") +
            (mail != null ? "mail=" + mail + ", " : "") +
            (activated != null ? "activated=" + activated + ", " : "") +
            (tckn != null ? "tckn=" + tckn + ", " : "") +
            (phone != null ? "phone=" + phone + ", " : "") +
            (city != null ? "city=" + city + ", " : "") +
            (placedDate != null ? "placedDate=" + placedDate + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (productsId != null ? "productsId=" + productsId + ", " : "") +
            (sellerProductTypeId != null ? "sellerProductTypeId=" + sellerProductTypeId + ", " : "") +
            (brandId != null ? "brandId=" + brandId + ", " : "") +
            (sellerStatisticsId != null ? "sellerStatisticsId=" + sellerStatisticsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
