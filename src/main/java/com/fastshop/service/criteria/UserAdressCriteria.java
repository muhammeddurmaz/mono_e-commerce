package com.fastshop.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.fastshop.domain.UserAdress} entity. This class is used
 * in {@link com.fastshop.web.rest.UserAdressResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-adresses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserAdressCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter lastName;

    private StringFilter telephone;

    private StringFilter city;

    private StringFilter adressTitle;

    private LongFilter userId;

    private Boolean distinct;

    public UserAdressCriteria() {}

    public UserAdressCriteria(UserAdressCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.telephone = other.telephone == null ? null : other.telephone.copy();
        this.city = other.city == null ? null : other.city.copy();
        this.adressTitle = other.adressTitle == null ? null : other.adressTitle.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public UserAdressCriteria copy() {
        return new UserAdressCriteria(this);
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

    public StringFilter getTelephone() {
        return telephone;
    }

    public StringFilter telephone() {
        if (telephone == null) {
            telephone = new StringFilter();
        }
        return telephone;
    }

    public void setTelephone(StringFilter telephone) {
        this.telephone = telephone;
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

    public StringFilter getAdressTitle() {
        return adressTitle;
    }

    public StringFilter adressTitle() {
        if (adressTitle == null) {
            adressTitle = new StringFilter();
        }
        return adressTitle;
    }

    public void setAdressTitle(StringFilter adressTitle) {
        this.adressTitle = adressTitle;
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
        final UserAdressCriteria that = (UserAdressCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(telephone, that.telephone) &&
            Objects.equals(city, that.city) &&
            Objects.equals(adressTitle, that.adressTitle) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lastName, telephone, city, adressTitle, userId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserAdressCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (lastName != null ? "lastName=" + lastName + ", " : "") +
            (telephone != null ? "telephone=" + telephone + ", " : "") +
            (city != null ? "city=" + city + ", " : "") +
            (adressTitle != null ? "adressTitle=" + adressTitle + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
