package com.fastshop.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.fastshop.domain.UserAdress} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserAdressDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String lastName;

    @NotNull
    private String telephone;

    @NotNull
    private String city;

    @Lob
    private String adress;

    @NotNull
    private String adressTitle;

    private UserDTO user;

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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getAdressTitle() {
        return adressTitle;
    }

    public void setAdressTitle(String adressTitle) {
        this.adressTitle = adressTitle;
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
        if (!(o instanceof UserAdressDTO)) {
            return false;
        }

        UserAdressDTO userAdressDTO = (UserAdressDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userAdressDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserAdressDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", city='" + getCity() + "'" +
            ", adress='" + getAdress() + "'" +
            ", adressTitle='" + getAdressTitle() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
