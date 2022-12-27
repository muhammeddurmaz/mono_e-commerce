package com.fastshop.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "product")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "barcode", nullable = false)
    private String barcode;

    @NotNull
    @Column(name = "model_code", nullable = false)
    private String modelCode;

    @NotNull
    @Size(min = 5)
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "price", precision = 21, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "discount_price", precision = 21, scale = 2)
    private BigDecimal discountPrice;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "image", nullable = false)
    private byte[] image;

    @NotNull
    @Column(name = "image_content_type", nullable = false)
    private String imageContentType;

    @NotNull
    @Column(name = "added_date", nullable = false)
    private Instant addedDate;

    @Column(name = "rating")
    private Float rating;

    @Column(name = "sizee")
    private String sizee;

    @NotNull
    @Min(value = 0)
    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "active")
    private Boolean active;

    @OneToMany(mappedBy = "product")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "discount", "product" }, allowSetters = true)
    private Set<ProductDiscount> productDiscounts = new HashSet<>();

    @OneToMany(mappedBy = "product")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "property", "product" }, allowSetters = true)
    private Set<PropertyDes> propertyDetails = new HashSet<>();

    @OneToMany(mappedBy = "product")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "product" }, allowSetters = true)
    private Set<ProductInventory> productInventories = new HashSet<>();

    @OneToMany(mappedBy = "product")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "user", "product" }, allowSetters = true)
    private Set<Comment> comments = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "subCategories", "type" }, allowSetters = true)
    private Category category;

    @ManyToOne
    @JsonIgnoreProperties(value = { "category", "products" }, allowSetters = true)
    private SubCategory subCategory;

    @ManyToOne
    @JsonIgnoreProperties(value = { "products" }, allowSetters = true)
    private Color color;

    @JsonIgnoreProperties(value = { "product" }, allowSetters = true)
    @OneToOne(mappedBy = "product")
    @org.springframework.data.annotation.Transient
    private ProductStatistics productStatistics;

    @ManyToOne
    @JsonIgnoreProperties(value = { "products" }, allowSetters = true)
    private ProductModel productModel;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "products", "sellerProductType", "brand", "sellerStatistics" }, allowSetters = true)
    private Seller seller;

    @ManyToOne
    @JsonIgnoreProperties(value = { "seller", "products" }, allowSetters = true)
    private Brand brand;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Product id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBarcode() {
        return this.barcode;
    }

    public Product barcode(String barcode) {
        this.setBarcode(barcode);
        return this;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getModelCode() {
        return this.modelCode;
    }

    public Product modelCode(String modelCode) {
        this.setModelCode(modelCode);
        return this;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public String getName() {
        return this.name;
    }

    public Product name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public Product price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDiscountPrice() {
        return this.discountPrice;
    }

    public Product discountPrice(BigDecimal discountPrice) {
        this.setDiscountPrice(discountPrice);
        return this;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getDescription() {
        return this.description;
    }

    public Product description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImage() {
        return this.image;
    }

    public Product image(byte[] image) {
        this.setImage(image);
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public Product imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Instant getAddedDate() {
        return this.addedDate;
    }

    public Product addedDate(Instant addedDate) {
        this.setAddedDate(addedDate);
        return this;
    }

    public void setAddedDate(Instant addedDate) {
        this.addedDate = addedDate;
    }

    public Float getRating() {
        return this.rating;
    }

    public Product rating(Float rating) {
        this.setRating(rating);
        return this;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getSizee() {
        return this.sizee;
    }

    public Product sizee(String sizee) {
        this.setSizee(sizee);
        return this;
    }

    public void setSizee(String sizee) {
        this.sizee = sizee;
    }

    public Integer getStock() {
        return this.stock;
    }

    public Product stock(Integer stock) {
        this.setStock(stock);
        return this;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Product active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<ProductDiscount> getProductDiscounts() {
        return this.productDiscounts;
    }

    public void setProductDiscounts(Set<ProductDiscount> productDiscounts) {
        if (this.productDiscounts != null) {
            this.productDiscounts.forEach(i -> i.setProduct(null));
        }
        if (productDiscounts != null) {
            productDiscounts.forEach(i -> i.setProduct(this));
        }
        this.productDiscounts = productDiscounts;
    }

    public Product productDiscounts(Set<ProductDiscount> productDiscounts) {
        this.setProductDiscounts(productDiscounts);
        return this;
    }

    public Product addProductDiscount(ProductDiscount productDiscount) {
        this.productDiscounts.add(productDiscount);
        productDiscount.setProduct(this);
        return this;
    }

    public Product removeProductDiscount(ProductDiscount productDiscount) {
        this.productDiscounts.remove(productDiscount);
        productDiscount.setProduct(null);
        return this;
    }

    public Set<PropertyDes> getPropertyDetails() {
        return this.propertyDetails;
    }

    public void setPropertyDetails(Set<PropertyDes> propertyDes) {
        if (this.propertyDetails != null) {
            this.propertyDetails.forEach(i -> i.setProduct(null));
        }
        if (propertyDes != null) {
            propertyDes.forEach(i -> i.setProduct(this));
        }
        this.propertyDetails = propertyDes;
    }

    public Product propertyDetails(Set<PropertyDes> propertyDes) {
        this.setPropertyDetails(propertyDes);
        return this;
    }

    public Product addPropertyDetails(PropertyDes propertyDes) {
        this.propertyDetails.add(propertyDes);
        propertyDes.setProduct(this);
        return this;
    }

    public Product removePropertyDetails(PropertyDes propertyDes) {
        this.propertyDetails.remove(propertyDes);
        propertyDes.setProduct(null);
        return this;
    }

    public Set<ProductInventory> getProductInventories() {
        return this.productInventories;
    }

    public void setProductInventories(Set<ProductInventory> productInventories) {
        if (this.productInventories != null) {
            this.productInventories.forEach(i -> i.setProduct(null));
        }
        if (productInventories != null) {
            productInventories.forEach(i -> i.setProduct(this));
        }
        this.productInventories = productInventories;
    }

    public Product productInventories(Set<ProductInventory> productInventories) {
        this.setProductInventories(productInventories);
        return this;
    }

    public Product addProductInventory(ProductInventory productInventory) {
        this.productInventories.add(productInventory);
        productInventory.setProduct(this);
        return this;
    }

    public Product removeProductInventory(ProductInventory productInventory) {
        this.productInventories.remove(productInventory);
        productInventory.setProduct(null);
        return this;
    }

    public Set<Comment> getComments() {
        return this.comments;
    }

    public void setComments(Set<Comment> comments) {
        if (this.comments != null) {
            this.comments.forEach(i -> i.setProduct(null));
        }
        if (comments != null) {
            comments.forEach(i -> i.setProduct(this));
        }
        this.comments = comments;
    }

    public Product comments(Set<Comment> comments) {
        this.setComments(comments);
        return this;
    }

    public Product addComment(Comment comment) {
        this.comments.add(comment);
        comment.setProduct(this);
        return this;
    }

    public Product removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.setProduct(null);
        return this;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Product category(Category category) {
        this.setCategory(category);
        return this;
    }

    public SubCategory getSubCategory() {
        return this.subCategory;
    }

    public void setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
    }

    public Product subCategory(SubCategory subCategory) {
        this.setSubCategory(subCategory);
        return this;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Product color(Color color) {
        this.setColor(color);
        return this;
    }

    public ProductStatistics getProductStatistics() {
        return this.productStatistics;
    }

    public void setProductStatistics(ProductStatistics productStatistics) {
        if (this.productStatistics != null) {
            this.productStatistics.setProduct(null);
        }
        if (productStatistics != null) {
            productStatistics.setProduct(this);
        }
        this.productStatistics = productStatistics;
    }

    public Product productStatistics(ProductStatistics productStatistics) {
        this.setProductStatistics(productStatistics);
        return this;
    }

    public ProductModel getProductModel() {
        return this.productModel;
    }

    public void setProductModel(ProductModel productModel) {
        this.productModel = productModel;
    }

    public Product productModel(ProductModel productModel) {
        this.setProductModel(productModel);
        return this;
    }

    public Seller getSeller() {
        return this.seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public Product seller(Seller seller) {
        this.setSeller(seller);
        return this;
    }

    public Brand getBrand() {
        return this.brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Product brand(Brand brand) {
        this.setBrand(brand);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", barcode='" + getBarcode() + "'" +
            ", modelCode='" + getModelCode() + "'" +
            ", name='" + getName() + "'" +
            ", price=" + getPrice() +
            ", discountPrice=" + getDiscountPrice() +
            ", description='" + getDescription() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", addedDate='" + getAddedDate() + "'" +
            ", rating=" + getRating() +
            ", sizee='" + getSizee() + "'" +
            ", stock=" + getStock() +
            ", active='" + getActive() + "'" +
            "}";
    }
}
