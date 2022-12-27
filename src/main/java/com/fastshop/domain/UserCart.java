package com.fastshop.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserCart.
 */
@Entity
@Table(name = "user_cart")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserCart implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "cart_name", nullable = false)
    private String cartName;

    @NotNull
    @Column(name = "cart_number", nullable = false)
    private String cartNumber;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Column(name = "skt_ay", nullable = false)
    private String sktAy;

    @NotNull
    @Column(name = "skt_yil", nullable = false)
    private String sktYil;

    @ManyToOne
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserCart id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCartName() {
        return this.cartName;
    }

    public UserCart cartName(String cartName) {
        this.setCartName(cartName);
        return this;
    }

    public void setCartName(String cartName) {
        this.cartName = cartName;
    }

    public String getCartNumber() {
        return this.cartNumber;
    }

    public UserCart cartNumber(String cartNumber) {
        this.setCartNumber(cartNumber);
        return this;
    }

    public void setCartNumber(String cartNumber) {
        this.cartNumber = cartNumber;
    }

    public String getName() {
        return this.name;
    }

    public UserCart name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return this.lastName;
    }

    public UserCart lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSktAy() {
        return this.sktAy;
    }

    public UserCart sktAy(String sktAy) {
        this.setSktAy(sktAy);
        return this;
    }

    public void setSktAy(String sktAy) {
        this.sktAy = sktAy;
    }

    public String getSktYil() {
        return this.sktYil;
    }

    public UserCart sktYil(String sktYil) {
        this.setSktYil(sktYil);
        return this;
    }

    public void setSktYil(String sktYil) {
        this.sktYil = sktYil;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserCart user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserCart)) {
            return false;
        }
        return id != null && id.equals(((UserCart) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserCart{" +
            "id=" + getId() +
            ", cartName='" + getCartName() + "'" +
            ", cartNumber='" + getCartNumber() + "'" +
            ", name='" + getName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", sktAy='" + getSktAy() + "'" +
            ", sktYil='" + getSktYil() + "'" +
            "}";
    }
}
