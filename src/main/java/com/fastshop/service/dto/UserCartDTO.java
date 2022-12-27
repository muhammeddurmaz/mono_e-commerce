package com.fastshop.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.fastshop.domain.UserCart} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserCartDTO implements Serializable {

    private Long id;

    @NotNull
    private String cartName;

    @NotNull
    private String cartNumber;

    @NotNull
    private String name;

    @NotNull
    private String lastName;

    @NotNull
    private String sktAy;

    @NotNull
    private String sktYil;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCartName() {
        return cartName;
    }

    public void setCartName(String cartName) {
        this.cartName = cartName;
    }

    public String getCartNumber() {
        return cartNumber;
    }

    public void setCartNumber(String cartNumber) {
        this.cartNumber = cartNumber;
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

    public String getSktAy() {
        return sktAy;
    }

    public void setSktAy(String sktAy) {
        this.sktAy = sktAy;
    }

    public String getSktYil() {
        return sktYil;
    }

    public void setSktYil(String sktYil) {
        this.sktYil = sktYil;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserCartDTO)) {
            return false;
        }

        UserCartDTO userCartDTO = (UserCartDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userCartDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserCartDTO{" +
            "id=" + getId() +
            ", cartName='" + getCartName() + "'" +
            ", cartNumber='" + getCartNumber() + "'" +
            ", name='" + getName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", sktAy='" + getSktAy() + "'" +
            ", sktYil='" + getSktYil() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
