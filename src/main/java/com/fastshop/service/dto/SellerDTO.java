package com.fastshop.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.fastshop.domain.Seller} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SellerDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 3)
    private String name;

    @NotNull
    @Size(min = 3)
    private String lastName;

    @NotNull
    @Size(min = 3)
    private String shopName;

    @NotNull
    private String mail;

    private Boolean activated;

    @Lob
    private byte[] image;

    private String imageContentType;

    @NotNull
    @Size(min = 11)
    private String tckn;

    @NotNull
    private String phone;

    @NotNull
    private String city;

    private Instant placedDate;

    private UserDTO user;

    private ProductTypeDTO sellerProductType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getTckn() {
        return tckn;
    }

    public void setTckn(String tckn) {
        this.tckn = tckn;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Instant getPlacedDate() {
        return placedDate;
    }

    public void setPlacedDate(Instant placedDate) {
        this.placedDate = placedDate;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public ProductTypeDTO getSellerProductType() {
        return sellerProductType;
    }

    public void setSellerProductType(ProductTypeDTO sellerProductType) {
        this.sellerProductType = sellerProductType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SellerDTO)) {
            return false;
        }

        SellerDTO sellerDTO = (SellerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sellerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SellerDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", shopName='" + getShopName() + "'" +
            ", mail='" + getMail() + "'" +
            ", activated='" + getActivated() + "'" +
            ", image='" + getImage() + "'" +
            ", tckn='" + getTckn() + "'" +
            ", phone='" + getPhone() + "'" +
            ", city='" + getCity() + "'" +
            ", placedDate='" + getPlacedDate() + "'" +
            ", user=" + getUser() +
            ", sellerProductType=" + getSellerProductType() +
            "}";
    }
}
