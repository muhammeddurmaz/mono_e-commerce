package com.fastshop.web.rest;

import static com.fastshop.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fastshop.IntegrationTest;
import com.fastshop.domain.Brand;
import com.fastshop.domain.Category;
import com.fastshop.domain.Color;
import com.fastshop.domain.Comment;
import com.fastshop.domain.Product;
import com.fastshop.domain.ProductDiscount;
import com.fastshop.domain.ProductInventory;
import com.fastshop.domain.ProductModel;
import com.fastshop.domain.ProductStatistics;
import com.fastshop.domain.PropertyDes;
import com.fastshop.domain.Seller;
import com.fastshop.domain.SubCategory;
import com.fastshop.repository.ProductRepository;
import com.fastshop.repository.search.ProductSearchRepository;
import com.fastshop.service.criteria.ProductCriteria;
import com.fastshop.service.dto.ProductDTO;
import com.fastshop.service.mapper.ProductMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.apache.commons.collections4.IterableUtils;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ProductResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductResourceIT {

    private static final String DEFAULT_BARCODE = "AAAAAAAAAA";
    private static final String UPDATED_BARCODE = "BBBBBBBBBB";

    private static final String DEFAULT_MODEL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_MODEL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(1);
    private static final BigDecimal SMALLER_PRICE = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_DISCOUNT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_DISCOUNT_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_DISCOUNT_PRICE = new BigDecimal(1 - 1);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final Instant DEFAULT_ADDED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ADDED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Float DEFAULT_RATING = 1F;
    private static final Float UPDATED_RATING = 2F;
    private static final Float SMALLER_RATING = 1F - 1F;

    private static final String DEFAULT_SIZEE = "AAAAAAAAAA";
    private static final String UPDATED_SIZEE = "BBBBBBBBBB";

    private static final Integer DEFAULT_STOCK = 0;
    private static final Integer UPDATED_STOCK = 1;
    private static final Integer SMALLER_STOCK = 0 - 1;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/products";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductSearchRepository productSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductMockMvc;

    private Product product;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createEntity(EntityManager em) {
        Product product = new Product()
            .barcode(DEFAULT_BARCODE)
            .modelCode(DEFAULT_MODEL_CODE)
            .name(DEFAULT_NAME)
            .price(DEFAULT_PRICE)
            .discountPrice(DEFAULT_DISCOUNT_PRICE)
            .description(DEFAULT_DESCRIPTION)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .addedDate(DEFAULT_ADDED_DATE)
            .rating(DEFAULT_RATING)
            .sizee(DEFAULT_SIZEE)
            .stock(DEFAULT_STOCK)
            .active(DEFAULT_ACTIVE);
        // Add required entity
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            category = CategoryResourceIT.createEntity(em);
            em.persist(category);
            em.flush();
        } else {
            category = TestUtil.findAll(em, Category.class).get(0);
        }
        product.setCategory(category);
        return product;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createUpdatedEntity(EntityManager em) {
        Product product = new Product()
            .barcode(UPDATED_BARCODE)
            .modelCode(UPDATED_MODEL_CODE)
            .name(UPDATED_NAME)
            .price(UPDATED_PRICE)
            .discountPrice(UPDATED_DISCOUNT_PRICE)
            .description(UPDATED_DESCRIPTION)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .addedDate(UPDATED_ADDED_DATE)
            .rating(UPDATED_RATING)
            .sizee(UPDATED_SIZEE)
            .stock(UPDATED_STOCK)
            .active(UPDATED_ACTIVE);
        // Add required entity
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            category = CategoryResourceIT.createUpdatedEntity(em);
            em.persist(category);
            em.flush();
        } else {
            category = TestUtil.findAll(em, Category.class).get(0);
        }
        product.setCategory(category);
        return product;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        productSearchRepository.deleteAll();
        assertThat(productSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        product = createEntity(em);
    }

    @Test
    @Transactional
    void createProduct() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productSearchRepository.findAll());
        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);
        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isCreated());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(productSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getBarcode()).isEqualTo(DEFAULT_BARCODE);
        assertThat(testProduct.getModelCode()).isEqualTo(DEFAULT_MODEL_CODE);
        assertThat(testProduct.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProduct.getPrice()).isEqualByComparingTo(DEFAULT_PRICE);
        assertThat(testProduct.getDiscountPrice()).isEqualByComparingTo(DEFAULT_DISCOUNT_PRICE);
        assertThat(testProduct.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProduct.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testProduct.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testProduct.getAddedDate()).isEqualTo(DEFAULT_ADDED_DATE);
        assertThat(testProduct.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testProduct.getSizee()).isEqualTo(DEFAULT_SIZEE);
        assertThat(testProduct.getStock()).isEqualTo(DEFAULT_STOCK);
        assertThat(testProduct.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    void createProductWithExistingId() throws Exception {
        // Create the Product with an existing ID
        product.setId(1L);
        ProductDTO productDTO = productMapper.toDto(product);

        int databaseSizeBeforeCreate = productRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkBarcodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productSearchRepository.findAll());
        // set the field null
        product.setBarcode(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkModelCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productSearchRepository.findAll());
        // set the field null
        product.setModelCode(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productSearchRepository.findAll());
        // set the field null
        product.setName(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productSearchRepository.findAll());
        // set the field null
        product.setPrice(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkAddedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productSearchRepository.findAll());
        // set the field null
        product.setAddedDate(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkStockIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productSearchRepository.findAll());
        // set the field null
        product.setStock(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllProducts() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE)))
            .andExpect(jsonPath("$.[*].modelCode").value(hasItem(DEFAULT_MODEL_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].discountPrice").value(hasItem(sameNumber(DEFAULT_DISCOUNT_PRICE))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].addedDate").value(hasItem(DEFAULT_ADDED_DATE.toString())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].sizee").value(hasItem(DEFAULT_SIZEE)))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get the product
        restProductMockMvc
            .perform(get(ENTITY_API_URL_ID, product.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(product.getId().intValue()))
            .andExpect(jsonPath("$.barcode").value(DEFAULT_BARCODE))
            .andExpect(jsonPath("$.modelCode").value(DEFAULT_MODEL_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.discountPrice").value(sameNumber(DEFAULT_DISCOUNT_PRICE)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.addedDate").value(DEFAULT_ADDED_DATE.toString()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING.doubleValue()))
            .andExpect(jsonPath("$.sizee").value(DEFAULT_SIZEE))
            .andExpect(jsonPath("$.stock").value(DEFAULT_STOCK))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getProductsByIdFiltering() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        Long id = product.getId();

        defaultProductShouldBeFound("id.equals=" + id);
        defaultProductShouldNotBeFound("id.notEquals=" + id);

        defaultProductShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProductShouldNotBeFound("id.greaterThan=" + id);

        defaultProductShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProductShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductsByBarcodeIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where barcode equals to DEFAULT_BARCODE
        defaultProductShouldBeFound("barcode.equals=" + DEFAULT_BARCODE);

        // Get all the productList where barcode equals to UPDATED_BARCODE
        defaultProductShouldNotBeFound("barcode.equals=" + UPDATED_BARCODE);
    }

    @Test
    @Transactional
    void getAllProductsByBarcodeIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where barcode in DEFAULT_BARCODE or UPDATED_BARCODE
        defaultProductShouldBeFound("barcode.in=" + DEFAULT_BARCODE + "," + UPDATED_BARCODE);

        // Get all the productList where barcode equals to UPDATED_BARCODE
        defaultProductShouldNotBeFound("barcode.in=" + UPDATED_BARCODE);
    }

    @Test
    @Transactional
    void getAllProductsByBarcodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where barcode is not null
        defaultProductShouldBeFound("barcode.specified=true");

        // Get all the productList where barcode is null
        defaultProductShouldNotBeFound("barcode.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByBarcodeContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where barcode contains DEFAULT_BARCODE
        defaultProductShouldBeFound("barcode.contains=" + DEFAULT_BARCODE);

        // Get all the productList where barcode contains UPDATED_BARCODE
        defaultProductShouldNotBeFound("barcode.contains=" + UPDATED_BARCODE);
    }

    @Test
    @Transactional
    void getAllProductsByBarcodeNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where barcode does not contain DEFAULT_BARCODE
        defaultProductShouldNotBeFound("barcode.doesNotContain=" + DEFAULT_BARCODE);

        // Get all the productList where barcode does not contain UPDATED_BARCODE
        defaultProductShouldBeFound("barcode.doesNotContain=" + UPDATED_BARCODE);
    }

    @Test
    @Transactional
    void getAllProductsByModelCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where modelCode equals to DEFAULT_MODEL_CODE
        defaultProductShouldBeFound("modelCode.equals=" + DEFAULT_MODEL_CODE);

        // Get all the productList where modelCode equals to UPDATED_MODEL_CODE
        defaultProductShouldNotBeFound("modelCode.equals=" + UPDATED_MODEL_CODE);
    }

    @Test
    @Transactional
    void getAllProductsByModelCodeIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where modelCode in DEFAULT_MODEL_CODE or UPDATED_MODEL_CODE
        defaultProductShouldBeFound("modelCode.in=" + DEFAULT_MODEL_CODE + "," + UPDATED_MODEL_CODE);

        // Get all the productList where modelCode equals to UPDATED_MODEL_CODE
        defaultProductShouldNotBeFound("modelCode.in=" + UPDATED_MODEL_CODE);
    }

    @Test
    @Transactional
    void getAllProductsByModelCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where modelCode is not null
        defaultProductShouldBeFound("modelCode.specified=true");

        // Get all the productList where modelCode is null
        defaultProductShouldNotBeFound("modelCode.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByModelCodeContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where modelCode contains DEFAULT_MODEL_CODE
        defaultProductShouldBeFound("modelCode.contains=" + DEFAULT_MODEL_CODE);

        // Get all the productList where modelCode contains UPDATED_MODEL_CODE
        defaultProductShouldNotBeFound("modelCode.contains=" + UPDATED_MODEL_CODE);
    }

    @Test
    @Transactional
    void getAllProductsByModelCodeNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where modelCode does not contain DEFAULT_MODEL_CODE
        defaultProductShouldNotBeFound("modelCode.doesNotContain=" + DEFAULT_MODEL_CODE);

        // Get all the productList where modelCode does not contain UPDATED_MODEL_CODE
        defaultProductShouldBeFound("modelCode.doesNotContain=" + UPDATED_MODEL_CODE);
    }

    @Test
    @Transactional
    void getAllProductsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name equals to DEFAULT_NAME
        defaultProductShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the productList where name equals to UPDATED_NAME
        defaultProductShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name in DEFAULT_NAME or UPDATED_NAME
        defaultProductShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the productList where name equals to UPDATED_NAME
        defaultProductShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name is not null
        defaultProductShouldBeFound("name.specified=true");

        // Get all the productList where name is null
        defaultProductShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByNameContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name contains DEFAULT_NAME
        defaultProductShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the productList where name contains UPDATED_NAME
        defaultProductShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name does not contain DEFAULT_NAME
        defaultProductShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the productList where name does not contain UPDATED_NAME
        defaultProductShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price equals to DEFAULT_PRICE
        defaultProductShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the productList where price equals to UPDATED_PRICE
        defaultProductShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultProductShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the productList where price equals to UPDATED_PRICE
        defaultProductShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price is not null
        defaultProductShouldBeFound("price.specified=true");

        // Get all the productList where price is null
        defaultProductShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price is greater than or equal to DEFAULT_PRICE
        defaultProductShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the productList where price is greater than or equal to UPDATED_PRICE
        defaultProductShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price is less than or equal to DEFAULT_PRICE
        defaultProductShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the productList where price is less than or equal to SMALLER_PRICE
        defaultProductShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price is less than DEFAULT_PRICE
        defaultProductShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the productList where price is less than UPDATED_PRICE
        defaultProductShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price is greater than DEFAULT_PRICE
        defaultProductShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the productList where price is greater than SMALLER_PRICE
        defaultProductShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByDiscountPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where discountPrice equals to DEFAULT_DISCOUNT_PRICE
        defaultProductShouldBeFound("discountPrice.equals=" + DEFAULT_DISCOUNT_PRICE);

        // Get all the productList where discountPrice equals to UPDATED_DISCOUNT_PRICE
        defaultProductShouldNotBeFound("discountPrice.equals=" + UPDATED_DISCOUNT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByDiscountPriceIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where discountPrice in DEFAULT_DISCOUNT_PRICE or UPDATED_DISCOUNT_PRICE
        defaultProductShouldBeFound("discountPrice.in=" + DEFAULT_DISCOUNT_PRICE + "," + UPDATED_DISCOUNT_PRICE);

        // Get all the productList where discountPrice equals to UPDATED_DISCOUNT_PRICE
        defaultProductShouldNotBeFound("discountPrice.in=" + UPDATED_DISCOUNT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByDiscountPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where discountPrice is not null
        defaultProductShouldBeFound("discountPrice.specified=true");

        // Get all the productList where discountPrice is null
        defaultProductShouldNotBeFound("discountPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByDiscountPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where discountPrice is greater than or equal to DEFAULT_DISCOUNT_PRICE
        defaultProductShouldBeFound("discountPrice.greaterThanOrEqual=" + DEFAULT_DISCOUNT_PRICE);

        // Get all the productList where discountPrice is greater than or equal to UPDATED_DISCOUNT_PRICE
        defaultProductShouldNotBeFound("discountPrice.greaterThanOrEqual=" + UPDATED_DISCOUNT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByDiscountPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where discountPrice is less than or equal to DEFAULT_DISCOUNT_PRICE
        defaultProductShouldBeFound("discountPrice.lessThanOrEqual=" + DEFAULT_DISCOUNT_PRICE);

        // Get all the productList where discountPrice is less than or equal to SMALLER_DISCOUNT_PRICE
        defaultProductShouldNotBeFound("discountPrice.lessThanOrEqual=" + SMALLER_DISCOUNT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByDiscountPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where discountPrice is less than DEFAULT_DISCOUNT_PRICE
        defaultProductShouldNotBeFound("discountPrice.lessThan=" + DEFAULT_DISCOUNT_PRICE);

        // Get all the productList where discountPrice is less than UPDATED_DISCOUNT_PRICE
        defaultProductShouldBeFound("discountPrice.lessThan=" + UPDATED_DISCOUNT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByDiscountPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where discountPrice is greater than DEFAULT_DISCOUNT_PRICE
        defaultProductShouldNotBeFound("discountPrice.greaterThan=" + DEFAULT_DISCOUNT_PRICE);

        // Get all the productList where discountPrice is greater than SMALLER_DISCOUNT_PRICE
        defaultProductShouldBeFound("discountPrice.greaterThan=" + SMALLER_DISCOUNT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByAddedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where addedDate equals to DEFAULT_ADDED_DATE
        defaultProductShouldBeFound("addedDate.equals=" + DEFAULT_ADDED_DATE);

        // Get all the productList where addedDate equals to UPDATED_ADDED_DATE
        defaultProductShouldNotBeFound("addedDate.equals=" + UPDATED_ADDED_DATE);
    }

    @Test
    @Transactional
    void getAllProductsByAddedDateIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where addedDate in DEFAULT_ADDED_DATE or UPDATED_ADDED_DATE
        defaultProductShouldBeFound("addedDate.in=" + DEFAULT_ADDED_DATE + "," + UPDATED_ADDED_DATE);

        // Get all the productList where addedDate equals to UPDATED_ADDED_DATE
        defaultProductShouldNotBeFound("addedDate.in=" + UPDATED_ADDED_DATE);
    }

    @Test
    @Transactional
    void getAllProductsByAddedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where addedDate is not null
        defaultProductShouldBeFound("addedDate.specified=true");

        // Get all the productList where addedDate is null
        defaultProductShouldNotBeFound("addedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where rating equals to DEFAULT_RATING
        defaultProductShouldBeFound("rating.equals=" + DEFAULT_RATING);

        // Get all the productList where rating equals to UPDATED_RATING
        defaultProductShouldNotBeFound("rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllProductsByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where rating in DEFAULT_RATING or UPDATED_RATING
        defaultProductShouldBeFound("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING);

        // Get all the productList where rating equals to UPDATED_RATING
        defaultProductShouldNotBeFound("rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllProductsByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where rating is not null
        defaultProductShouldBeFound("rating.specified=true");

        // Get all the productList where rating is null
        defaultProductShouldNotBeFound("rating.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where rating is greater than or equal to DEFAULT_RATING
        defaultProductShouldBeFound("rating.greaterThanOrEqual=" + DEFAULT_RATING);

        // Get all the productList where rating is greater than or equal to UPDATED_RATING
        defaultProductShouldNotBeFound("rating.greaterThanOrEqual=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllProductsByRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where rating is less than or equal to DEFAULT_RATING
        defaultProductShouldBeFound("rating.lessThanOrEqual=" + DEFAULT_RATING);

        // Get all the productList where rating is less than or equal to SMALLER_RATING
        defaultProductShouldNotBeFound("rating.lessThanOrEqual=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    void getAllProductsByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where rating is less than DEFAULT_RATING
        defaultProductShouldNotBeFound("rating.lessThan=" + DEFAULT_RATING);

        // Get all the productList where rating is less than UPDATED_RATING
        defaultProductShouldBeFound("rating.lessThan=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllProductsByRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where rating is greater than DEFAULT_RATING
        defaultProductShouldNotBeFound("rating.greaterThan=" + DEFAULT_RATING);

        // Get all the productList where rating is greater than SMALLER_RATING
        defaultProductShouldBeFound("rating.greaterThan=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    void getAllProductsBySizeeIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where sizee equals to DEFAULT_SIZEE
        defaultProductShouldBeFound("sizee.equals=" + DEFAULT_SIZEE);

        // Get all the productList where sizee equals to UPDATED_SIZEE
        defaultProductShouldNotBeFound("sizee.equals=" + UPDATED_SIZEE);
    }

    @Test
    @Transactional
    void getAllProductsBySizeeIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where sizee in DEFAULT_SIZEE or UPDATED_SIZEE
        defaultProductShouldBeFound("sizee.in=" + DEFAULT_SIZEE + "," + UPDATED_SIZEE);

        // Get all the productList where sizee equals to UPDATED_SIZEE
        defaultProductShouldNotBeFound("sizee.in=" + UPDATED_SIZEE);
    }

    @Test
    @Transactional
    void getAllProductsBySizeeIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where sizee is not null
        defaultProductShouldBeFound("sizee.specified=true");

        // Get all the productList where sizee is null
        defaultProductShouldNotBeFound("sizee.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsBySizeeContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where sizee contains DEFAULT_SIZEE
        defaultProductShouldBeFound("sizee.contains=" + DEFAULT_SIZEE);

        // Get all the productList where sizee contains UPDATED_SIZEE
        defaultProductShouldNotBeFound("sizee.contains=" + UPDATED_SIZEE);
    }

    @Test
    @Transactional
    void getAllProductsBySizeeNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where sizee does not contain DEFAULT_SIZEE
        defaultProductShouldNotBeFound("sizee.doesNotContain=" + DEFAULT_SIZEE);

        // Get all the productList where sizee does not contain UPDATED_SIZEE
        defaultProductShouldBeFound("sizee.doesNotContain=" + UPDATED_SIZEE);
    }

    @Test
    @Transactional
    void getAllProductsByStockIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where stock equals to DEFAULT_STOCK
        defaultProductShouldBeFound("stock.equals=" + DEFAULT_STOCK);

        // Get all the productList where stock equals to UPDATED_STOCK
        defaultProductShouldNotBeFound("stock.equals=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllProductsByStockIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where stock in DEFAULT_STOCK or UPDATED_STOCK
        defaultProductShouldBeFound("stock.in=" + DEFAULT_STOCK + "," + UPDATED_STOCK);

        // Get all the productList where stock equals to UPDATED_STOCK
        defaultProductShouldNotBeFound("stock.in=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllProductsByStockIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where stock is not null
        defaultProductShouldBeFound("stock.specified=true");

        // Get all the productList where stock is null
        defaultProductShouldNotBeFound("stock.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByStockIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where stock is greater than or equal to DEFAULT_STOCK
        defaultProductShouldBeFound("stock.greaterThanOrEqual=" + DEFAULT_STOCK);

        // Get all the productList where stock is greater than or equal to UPDATED_STOCK
        defaultProductShouldNotBeFound("stock.greaterThanOrEqual=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllProductsByStockIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where stock is less than or equal to DEFAULT_STOCK
        defaultProductShouldBeFound("stock.lessThanOrEqual=" + DEFAULT_STOCK);

        // Get all the productList where stock is less than or equal to SMALLER_STOCK
        defaultProductShouldNotBeFound("stock.lessThanOrEqual=" + SMALLER_STOCK);
    }

    @Test
    @Transactional
    void getAllProductsByStockIsLessThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where stock is less than DEFAULT_STOCK
        defaultProductShouldNotBeFound("stock.lessThan=" + DEFAULT_STOCK);

        // Get all the productList where stock is less than UPDATED_STOCK
        defaultProductShouldBeFound("stock.lessThan=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllProductsByStockIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where stock is greater than DEFAULT_STOCK
        defaultProductShouldNotBeFound("stock.greaterThan=" + DEFAULT_STOCK);

        // Get all the productList where stock is greater than SMALLER_STOCK
        defaultProductShouldBeFound("stock.greaterThan=" + SMALLER_STOCK);
    }

    @Test
    @Transactional
    void getAllProductsByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where active equals to DEFAULT_ACTIVE
        defaultProductShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the productList where active equals to UPDATED_ACTIVE
        defaultProductShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllProductsByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultProductShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the productList where active equals to UPDATED_ACTIVE
        defaultProductShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllProductsByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where active is not null
        defaultProductShouldBeFound("active.specified=true");

        // Get all the productList where active is null
        defaultProductShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByProductDiscountIsEqualToSomething() throws Exception {
        ProductDiscount productDiscount;
        if (TestUtil.findAll(em, ProductDiscount.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            productDiscount = ProductDiscountResourceIT.createEntity(em);
        } else {
            productDiscount = TestUtil.findAll(em, ProductDiscount.class).get(0);
        }
        em.persist(productDiscount);
        em.flush();
        product.addProductDiscount(productDiscount);
        productRepository.saveAndFlush(product);
        Long productDiscountId = productDiscount.getId();

        // Get all the productList where productDiscount equals to productDiscountId
        defaultProductShouldBeFound("productDiscountId.equals=" + productDiscountId);

        // Get all the productList where productDiscount equals to (productDiscountId + 1)
        defaultProductShouldNotBeFound("productDiscountId.equals=" + (productDiscountId + 1));
    }

    @Test
    @Transactional
    void getAllProductsByPropertyDetailsIsEqualToSomething() throws Exception {
        PropertyDes propertyDetails;
        if (TestUtil.findAll(em, PropertyDes.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            propertyDetails = PropertyDesResourceIT.createEntity(em);
        } else {
            propertyDetails = TestUtil.findAll(em, PropertyDes.class).get(0);
        }
        em.persist(propertyDetails);
        em.flush();
        product.addPropertyDetails(propertyDetails);
        productRepository.saveAndFlush(product);
        Long propertyDetailsId = propertyDetails.getId();

        // Get all the productList where propertyDetails equals to propertyDetailsId
        defaultProductShouldBeFound("propertyDetailsId.equals=" + propertyDetailsId);

        // Get all the productList where propertyDetails equals to (propertyDetailsId + 1)
        defaultProductShouldNotBeFound("propertyDetailsId.equals=" + (propertyDetailsId + 1));
    }

    @Test
    @Transactional
    void getAllProductsByProductInventoryIsEqualToSomething() throws Exception {
        ProductInventory productInventory;
        if (TestUtil.findAll(em, ProductInventory.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            productInventory = ProductInventoryResourceIT.createEntity(em);
        } else {
            productInventory = TestUtil.findAll(em, ProductInventory.class).get(0);
        }
        em.persist(productInventory);
        em.flush();
        product.addProductInventory(productInventory);
        productRepository.saveAndFlush(product);
        Long productInventoryId = productInventory.getId();

        // Get all the productList where productInventory equals to productInventoryId
        defaultProductShouldBeFound("productInventoryId.equals=" + productInventoryId);

        // Get all the productList where productInventory equals to (productInventoryId + 1)
        defaultProductShouldNotBeFound("productInventoryId.equals=" + (productInventoryId + 1));
    }

    @Test
    @Transactional
    void getAllProductsByCommentIsEqualToSomething() throws Exception {
        Comment comment;
        if (TestUtil.findAll(em, Comment.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            comment = CommentResourceIT.createEntity(em);
        } else {
            comment = TestUtil.findAll(em, Comment.class).get(0);
        }
        em.persist(comment);
        em.flush();
        product.addComment(comment);
        productRepository.saveAndFlush(product);
        Long commentId = comment.getId();

        // Get all the productList where comment equals to commentId
        defaultProductShouldBeFound("commentId.equals=" + commentId);

        // Get all the productList where comment equals to (commentId + 1)
        defaultProductShouldNotBeFound("commentId.equals=" + (commentId + 1));
    }

    @Test
    @Transactional
    void getAllProductsByCategoryIsEqualToSomething() throws Exception {
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            category = CategoryResourceIT.createEntity(em);
        } else {
            category = TestUtil.findAll(em, Category.class).get(0);
        }
        em.persist(category);
        em.flush();
        product.setCategory(category);
        productRepository.saveAndFlush(product);
        Long categoryId = category.getId();

        // Get all the productList where category equals to categoryId
        defaultProductShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the productList where category equals to (categoryId + 1)
        defaultProductShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    @Test
    @Transactional
    void getAllProductsBySubCategoryIsEqualToSomething() throws Exception {
        SubCategory subCategory;
        if (TestUtil.findAll(em, SubCategory.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            subCategory = SubCategoryResourceIT.createEntity(em);
        } else {
            subCategory = TestUtil.findAll(em, SubCategory.class).get(0);
        }
        em.persist(subCategory);
        em.flush();
        product.setSubCategory(subCategory);
        productRepository.saveAndFlush(product);
        Long subCategoryId = subCategory.getId();

        // Get all the productList where subCategory equals to subCategoryId
        defaultProductShouldBeFound("subCategoryId.equals=" + subCategoryId);

        // Get all the productList where subCategory equals to (subCategoryId + 1)
        defaultProductShouldNotBeFound("subCategoryId.equals=" + (subCategoryId + 1));
    }

    @Test
    @Transactional
    void getAllProductsByColorIsEqualToSomething() throws Exception {
        Color color;
        if (TestUtil.findAll(em, Color.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            color = ColorResourceIT.createEntity(em);
        } else {
            color = TestUtil.findAll(em, Color.class).get(0);
        }
        em.persist(color);
        em.flush();
        product.setColor(color);
        productRepository.saveAndFlush(product);
        Long colorId = color.getId();

        // Get all the productList where color equals to colorId
        defaultProductShouldBeFound("colorId.equals=" + colorId);

        // Get all the productList where color equals to (colorId + 1)
        defaultProductShouldNotBeFound("colorId.equals=" + (colorId + 1));
    }

    @Test
    @Transactional
    void getAllProductsByProductStatisticsIsEqualToSomething() throws Exception {
        ProductStatistics productStatistics;
        if (TestUtil.findAll(em, ProductStatistics.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            productStatistics = ProductStatisticsResourceIT.createEntity(em);
        } else {
            productStatistics = TestUtil.findAll(em, ProductStatistics.class).get(0);
        }
        em.persist(productStatistics);
        em.flush();
        product.setProductStatistics(productStatistics);
        productStatistics.setProduct(product);
        productRepository.saveAndFlush(product);
        Long productStatisticsId = productStatistics.getId();

        // Get all the productList where productStatistics equals to productStatisticsId
        defaultProductShouldBeFound("productStatisticsId.equals=" + productStatisticsId);

        // Get all the productList where productStatistics equals to (productStatisticsId + 1)
        defaultProductShouldNotBeFound("productStatisticsId.equals=" + (productStatisticsId + 1));
    }

    @Test
    @Transactional
    void getAllProductsByProductModelIsEqualToSomething() throws Exception {
        ProductModel productModel;
        if (TestUtil.findAll(em, ProductModel.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            productModel = ProductModelResourceIT.createEntity(em);
        } else {
            productModel = TestUtil.findAll(em, ProductModel.class).get(0);
        }
        em.persist(productModel);
        em.flush();
        product.setProductModel(productModel);
        productRepository.saveAndFlush(product);
        Long productModelId = productModel.getId();

        // Get all the productList where productModel equals to productModelId
        defaultProductShouldBeFound("productModelId.equals=" + productModelId);

        // Get all the productList where productModel equals to (productModelId + 1)
        defaultProductShouldNotBeFound("productModelId.equals=" + (productModelId + 1));
    }

    @Test
    @Transactional
    void getAllProductsBySellerIsEqualToSomething() throws Exception {
        Seller seller;
        if (TestUtil.findAll(em, Seller.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            seller = SellerResourceIT.createEntity(em);
        } else {
            seller = TestUtil.findAll(em, Seller.class).get(0);
        }
        em.persist(seller);
        em.flush();
        product.setSeller(seller);
        productRepository.saveAndFlush(product);
        Long sellerId = seller.getId();

        // Get all the productList where seller equals to sellerId
        defaultProductShouldBeFound("sellerId.equals=" + sellerId);

        // Get all the productList where seller equals to (sellerId + 1)
        defaultProductShouldNotBeFound("sellerId.equals=" + (sellerId + 1));
    }

    @Test
    @Transactional
    void getAllProductsByBrandIsEqualToSomething() throws Exception {
        Brand brand;
        if (TestUtil.findAll(em, Brand.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            brand = BrandResourceIT.createEntity(em);
        } else {
            brand = TestUtil.findAll(em, Brand.class).get(0);
        }
        em.persist(brand);
        em.flush();
        product.setBrand(brand);
        productRepository.saveAndFlush(product);
        Long brandId = brand.getId();

        // Get all the productList where brand equals to brandId
        defaultProductShouldBeFound("brandId.equals=" + brandId);

        // Get all the productList where brand equals to (brandId + 1)
        defaultProductShouldNotBeFound("brandId.equals=" + (brandId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductShouldBeFound(String filter) throws Exception {
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE)))
            .andExpect(jsonPath("$.[*].modelCode").value(hasItem(DEFAULT_MODEL_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].discountPrice").value(hasItem(sameNumber(DEFAULT_DISCOUNT_PRICE))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].addedDate").value(hasItem(DEFAULT_ADDED_DATE.toString())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].sizee").value(hasItem(DEFAULT_SIZEE)))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductShouldNotBeFound(String filter) throws Exception {
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProduct() throws Exception {
        // Get the product
        restProductMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        productSearchRepository.save(product);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productSearchRepository.findAll());

        // Update the product
        Product updatedProduct = productRepository.findById(product.getId()).get();
        // Disconnect from session so that the updates on updatedProduct are not directly saved in db
        em.detach(updatedProduct);
        updatedProduct
            .barcode(UPDATED_BARCODE)
            .modelCode(UPDATED_MODEL_CODE)
            .name(UPDATED_NAME)
            .price(UPDATED_PRICE)
            .discountPrice(UPDATED_DISCOUNT_PRICE)
            .description(UPDATED_DESCRIPTION)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .addedDate(UPDATED_ADDED_DATE)
            .rating(UPDATED_RATING)
            .sizee(UPDATED_SIZEE)
            .stock(UPDATED_STOCK)
            .active(UPDATED_ACTIVE);
        ProductDTO productDTO = productMapper.toDto(updatedProduct);

        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getBarcode()).isEqualTo(UPDATED_BARCODE);
        assertThat(testProduct.getModelCode()).isEqualTo(UPDATED_MODEL_CODE);
        assertThat(testProduct.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProduct.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
        assertThat(testProduct.getDiscountPrice()).isEqualByComparingTo(UPDATED_DISCOUNT_PRICE);
        assertThat(testProduct.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProduct.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testProduct.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testProduct.getAddedDate()).isEqualTo(UPDATED_ADDED_DATE);
        assertThat(testProduct.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testProduct.getSizee()).isEqualTo(UPDATED_SIZEE);
        assertThat(testProduct.getStock()).isEqualTo(UPDATED_STOCK);
        assertThat(testProduct.getActive()).isEqualTo(UPDATED_ACTIVE);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(productSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Product> productSearchList = IterableUtils.toList(productSearchRepository.findAll());
                Product testProductSearch = productSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testProductSearch.getBarcode()).isEqualTo(UPDATED_BARCODE);
                assertThat(testProductSearch.getModelCode()).isEqualTo(UPDATED_MODEL_CODE);
                assertThat(testProductSearch.getName()).isEqualTo(UPDATED_NAME);
                assertThat(testProductSearch.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
                assertThat(testProductSearch.getDiscountPrice()).isEqualByComparingTo(UPDATED_DISCOUNT_PRICE);
                assertThat(testProductSearch.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
                assertThat(testProductSearch.getImage()).isEqualTo(UPDATED_IMAGE);
                assertThat(testProductSearch.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
                assertThat(testProductSearch.getAddedDate()).isEqualTo(UPDATED_ADDED_DATE);
                assertThat(testProductSearch.getRating()).isEqualTo(UPDATED_RATING);
                assertThat(testProductSearch.getSizee()).isEqualTo(UPDATED_SIZEE);
                assertThat(testProductSearch.getStock()).isEqualTo(UPDATED_STOCK);
                assertThat(testProductSearch.getActive()).isEqualTo(UPDATED_ACTIVE);
            });
    }

    @Test
    @Transactional
    void putNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productSearchRepository.findAll());
        product.setId(count.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productSearchRepository.findAll());
        product.setId(count.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productSearchRepository.findAll());
        product.setId(count.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateProductWithPatch() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product using partial update
        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setId(product.getId());

        partialUpdatedProduct.discountPrice(UPDATED_DISCOUNT_PRICE).description(UPDATED_DESCRIPTION).stock(UPDATED_STOCK);

        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProduct))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getBarcode()).isEqualTo(DEFAULT_BARCODE);
        assertThat(testProduct.getModelCode()).isEqualTo(DEFAULT_MODEL_CODE);
        assertThat(testProduct.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProduct.getPrice()).isEqualByComparingTo(DEFAULT_PRICE);
        assertThat(testProduct.getDiscountPrice()).isEqualByComparingTo(UPDATED_DISCOUNT_PRICE);
        assertThat(testProduct.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProduct.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testProduct.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testProduct.getAddedDate()).isEqualTo(DEFAULT_ADDED_DATE);
        assertThat(testProduct.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testProduct.getSizee()).isEqualTo(DEFAULT_SIZEE);
        assertThat(testProduct.getStock()).isEqualTo(UPDATED_STOCK);
        assertThat(testProduct.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateProductWithPatch() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product using partial update
        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setId(product.getId());

        partialUpdatedProduct
            .barcode(UPDATED_BARCODE)
            .modelCode(UPDATED_MODEL_CODE)
            .name(UPDATED_NAME)
            .price(UPDATED_PRICE)
            .discountPrice(UPDATED_DISCOUNT_PRICE)
            .description(UPDATED_DESCRIPTION)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .addedDate(UPDATED_ADDED_DATE)
            .rating(UPDATED_RATING)
            .sizee(UPDATED_SIZEE)
            .stock(UPDATED_STOCK)
            .active(UPDATED_ACTIVE);

        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProduct))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getBarcode()).isEqualTo(UPDATED_BARCODE);
        assertThat(testProduct.getModelCode()).isEqualTo(UPDATED_MODEL_CODE);
        assertThat(testProduct.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProduct.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
        assertThat(testProduct.getDiscountPrice()).isEqualByComparingTo(UPDATED_DISCOUNT_PRICE);
        assertThat(testProduct.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProduct.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testProduct.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testProduct.getAddedDate()).isEqualTo(UPDATED_ADDED_DATE);
        assertThat(testProduct.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testProduct.getSizee()).isEqualTo(UPDATED_SIZEE);
        assertThat(testProduct.getStock()).isEqualTo(UPDATED_STOCK);
        assertThat(testProduct.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productSearchRepository.findAll());
        product.setId(count.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productSearchRepository.findAll());
        product.setId(count.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productSearchRepository.findAll());
        product.setId(count.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(productDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);
        productRepository.save(product);
        productSearchRepository.save(product);

        int databaseSizeBeforeDelete = productRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the product
        restProductMockMvc
            .perform(delete(ENTITY_API_URL_ID, product.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchProduct() throws Exception {
        // Initialize the database
        product = productRepository.saveAndFlush(product);
        productSearchRepository.save(product);

        // Search the product
        restProductMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + product.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE)))
            .andExpect(jsonPath("$.[*].modelCode").value(hasItem(DEFAULT_MODEL_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].discountPrice").value(hasItem(sameNumber(DEFAULT_DISCOUNT_PRICE))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].addedDate").value(hasItem(DEFAULT_ADDED_DATE.toString())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].sizee").value(hasItem(DEFAULT_SIZEE)))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }
}
