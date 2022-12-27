package com.fastshop.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Seller.
 */
@Entity
@Table(name = "seller")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "seller")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Seller implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3)
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Size(min = 3)
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Size(min = 3)
    @Column(name = "shop_name", nullable = false)
    private String shopName;

    @NotNull
    @Column(name = "mail", nullable = false)
    private String mail;

    @Column(name = "activated")
    private Boolean activated;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @NotNull
    @Size(min = 11)
    @Column(name = "tckn", nullable = false)
    private String tckn;

    @NotNull
    @Column(name = "phone", nullable = false)
    private String phone;

    @NotNull
    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "placed_date")
    private Instant placedDate;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "seller")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(
        value = {
            "productDiscounts",
            "propertyDetails",
            "productInventories",
            "comments",
            "category",
            "subCategory",
            "color",
            "productStatistics",
            "productModel",
            "seller",
            "brand",
        },
        allowSetters = true
    )
    private Set<Product> products = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "categories" }, allowSetters = true)
    private ProductType sellerProductType;

    @JsonIgnoreProperties(value = { "seller", "products" }, allowSetters = true)
    @OneToOne(mappedBy = "seller")
    @org.springframework.data.annotation.Transient
    private Brand brand;

    @JsonIgnoreProperties(value = { "seller" }, allowSetters = true)
    @OneToOne(mappedBy = "seller")
    @org.springframework.data.annotation.Transient
    private SellerStatistics sellerStatistics;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Seller id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Seller name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Seller lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getShopName() {
        return this.shopName;
    }

    public Seller shopName(String shopName) {
        this.setShopName(shopName);
        return this;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getMail() {
        return this.mail;
    }

    public Seller mail(String mail) {
        this.setMail(mail);
        return this;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Boolean getActivated() {
        return this.activated;
    }

    public Seller activated(Boolean activated) {
        this.setActivated(activated);
        return this;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public byte[] getImage() {
        return this.image;
    }

    public Seller image(byte[] image) {
        this.setImage(image);
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public Seller imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getTckn() {
        return this.tckn;
    }

    public Seller tckn(String tckn) {
        this.setTckn(tckn);
        return this;
    }

    public void setTckn(String tckn) {
        this.tckn = tckn;
    }

    public String getPhone() {
        return this.phone;
    }

    public Seller phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return this.city;
    }

    public Seller city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Instant getPlacedDate() {
        return this.placedDate;
    }

    public Seller placedDate(Instant placedDate) {
        this.setPlacedDate(placedDate);
        return this;
    }

    public void setPlacedDate(Instant placedDate) {
        this.placedDate = placedDate;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Seller user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.setSeller(null));
        }
        if (products != null) {
            products.forEach(i -> i.setSeller(this));
        }
        this.products = products;
    }

    public Seller products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public Seller addProducts(Product product) {
        this.products.add(product);
        product.setSeller(this);
        return this;
    }

    public Seller removeProducts(Product product) {
        this.products.remove(product);
        product.setSeller(null);
        return this;
    }

    public ProductType getSellerProductType() {
        return this.sellerProductType;
    }

    public void setSellerProductType(ProductType productType) {
        this.sellerProductType = productType;
    }

    public Seller sellerProductType(ProductType productType) {
        this.setSellerProductType(productType);
        return this;
    }

    public Brand getBrand() {
        return this.brand;
    }

    public void setBrand(Brand brand) {
        if (this.brand != null) {
            this.brand.setSeller(null);
        }
        if (brand != null) {
            brand.setSeller(this);
        }
        this.brand = brand;
    }

    public Seller brand(Brand brand) {
        this.setBrand(brand);
        return this;
    }

    public SellerStatistics getSellerStatistics() {
        return this.sellerStatistics;
    }

    public void setSellerStatistics(SellerStatistics sellerStatistics) {
        if (this.sellerStatistics != null) {
            this.sellerStatistics.setSeller(null);
        }
        if (sellerStatistics != null) {
            sellerStatistics.setSeller(this);
        }
        this.sellerStatistics = sellerStatistics;
    }

    public Seller sellerStatistics(SellerStatistics sellerStatistics) {
        this.setSellerStatistics(sellerStatistics);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Seller)) {
            return false;
        }
        return id != null && id.equals(((Seller) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Seller{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", shopName='" + getShopName() + "'" +
            ", mail='" + getMail() + "'" +
            ", activated='" + getActivated() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", tckn='" + getTckn() + "'" +
            ", phone='" + getPhone() + "'" +
            ", city='" + getCity() + "'" +
            ", placedDate='" + getPlacedDate() + "'" +
            "}";
    }
}
