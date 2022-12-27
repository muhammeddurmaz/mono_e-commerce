package com.fastshop.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fastshop.IntegrationTest;
import com.fastshop.domain.Discount;
import com.fastshop.domain.Product;
import com.fastshop.domain.ProductDiscount;
import com.fastshop.repository.ProductDiscountRepository;
import com.fastshop.repository.search.ProductDiscountSearchRepository;
import com.fastshop.service.criteria.ProductDiscountCriteria;
import com.fastshop.service.dto.ProductDiscountDTO;
import com.fastshop.service.mapper.ProductDiscountMapper;
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

/**
 * Integration tests for the {@link ProductDiscountResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductDiscountResourceIT {

    private static final Instant DEFAULT_ADDED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ADDED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-discounts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/product-discounts";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductDiscountRepository productDiscountRepository;

    @Autowired
    private ProductDiscountMapper productDiscountMapper;

    @Autowired
    private ProductDiscountSearchRepository productDiscountSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductDiscountMockMvc;

    private ProductDiscount productDiscount;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductDiscount createEntity(EntityManager em) {
        ProductDiscount productDiscount = new ProductDiscount()
            .addedDate(DEFAULT_ADDED_DATE)
            .dueDate(DEFAULT_DUE_DATE)
            .description(DEFAULT_DESCRIPTION);
        // Add required entity
        Discount discount;
        if (TestUtil.findAll(em, Discount.class).isEmpty()) {
            discount = DiscountResourceIT.createEntity(em);
            em.persist(discount);
            em.flush();
        } else {
            discount = TestUtil.findAll(em, Discount.class).get(0);
        }
        productDiscount.setDiscount(discount);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        productDiscount.setProduct(product);
        return productDiscount;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductDiscount createUpdatedEntity(EntityManager em) {
        ProductDiscount productDiscount = new ProductDiscount()
            .addedDate(UPDATED_ADDED_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .description(UPDATED_DESCRIPTION);
        // Add required entity
        Discount discount;
        if (TestUtil.findAll(em, Discount.class).isEmpty()) {
            discount = DiscountResourceIT.createUpdatedEntity(em);
            em.persist(discount);
            em.flush();
        } else {
            discount = TestUtil.findAll(em, Discount.class).get(0);
        }
        productDiscount.setDiscount(discount);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        productDiscount.setProduct(product);
        return productDiscount;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        productDiscountSearchRepository.deleteAll();
        assertThat(productDiscountSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        productDiscount = createEntity(em);
    }

    @Test
    @Transactional
    void createProductDiscount() throws Exception {
        int databaseSizeBeforeCreate = productDiscountRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productDiscountSearchRepository.findAll());
        // Create the ProductDiscount
        ProductDiscountDTO productDiscountDTO = productDiscountMapper.toDto(productDiscount);
        restProductDiscountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDiscountDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ProductDiscount in the database
        List<ProductDiscount> productDiscountList = productDiscountRepository.findAll();
        assertThat(productDiscountList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(productDiscountSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        ProductDiscount testProductDiscount = productDiscountList.get(productDiscountList.size() - 1);
        assertThat(testProductDiscount.getAddedDate()).isEqualTo(DEFAULT_ADDED_DATE);
        assertThat(testProductDiscount.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
        assertThat(testProductDiscount.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createProductDiscountWithExistingId() throws Exception {
        // Create the ProductDiscount with an existing ID
        productDiscount.setId(1L);
        ProductDiscountDTO productDiscountDTO = productDiscountMapper.toDto(productDiscount);

        int databaseSizeBeforeCreate = productDiscountRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productDiscountSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductDiscountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDiscountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductDiscount in the database
        List<ProductDiscount> productDiscountList = productDiscountRepository.findAll();
        assertThat(productDiscountList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productDiscountSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkAddedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = productDiscountRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productDiscountSearchRepository.findAll());
        // set the field null
        productDiscount.setAddedDate(null);

        // Create the ProductDiscount, which fails.
        ProductDiscountDTO productDiscountDTO = productDiscountMapper.toDto(productDiscount);

        restProductDiscountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDiscountDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductDiscount> productDiscountList = productDiscountRepository.findAll();
        assertThat(productDiscountList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productDiscountSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDueDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = productDiscountRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productDiscountSearchRepository.findAll());
        // set the field null
        productDiscount.setDueDate(null);

        // Create the ProductDiscount, which fails.
        ProductDiscountDTO productDiscountDTO = productDiscountMapper.toDto(productDiscount);

        restProductDiscountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDiscountDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductDiscount> productDiscountList = productDiscountRepository.findAll();
        assertThat(productDiscountList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productDiscountSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllProductDiscounts() throws Exception {
        // Initialize the database
        productDiscountRepository.saveAndFlush(productDiscount);

        // Get all the productDiscountList
        restProductDiscountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productDiscount.getId().intValue())))
            .andExpect(jsonPath("$.[*].addedDate").value(hasItem(DEFAULT_ADDED_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getProductDiscount() throws Exception {
        // Initialize the database
        productDiscountRepository.saveAndFlush(productDiscount);

        // Get the productDiscount
        restProductDiscountMockMvc
            .perform(get(ENTITY_API_URL_ID, productDiscount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productDiscount.getId().intValue()))
            .andExpect(jsonPath("$.addedDate").value(DEFAULT_ADDED_DATE.toString()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getProductDiscountsByIdFiltering() throws Exception {
        // Initialize the database
        productDiscountRepository.saveAndFlush(productDiscount);

        Long id = productDiscount.getId();

        defaultProductDiscountShouldBeFound("id.equals=" + id);
        defaultProductDiscountShouldNotBeFound("id.notEquals=" + id);

        defaultProductDiscountShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProductDiscountShouldNotBeFound("id.greaterThan=" + id);

        defaultProductDiscountShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProductDiscountShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductDiscountsByAddedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        productDiscountRepository.saveAndFlush(productDiscount);

        // Get all the productDiscountList where addedDate equals to DEFAULT_ADDED_DATE
        defaultProductDiscountShouldBeFound("addedDate.equals=" + DEFAULT_ADDED_DATE);

        // Get all the productDiscountList where addedDate equals to UPDATED_ADDED_DATE
        defaultProductDiscountShouldNotBeFound("addedDate.equals=" + UPDATED_ADDED_DATE);
    }

    @Test
    @Transactional
    void getAllProductDiscountsByAddedDateIsInShouldWork() throws Exception {
        // Initialize the database
        productDiscountRepository.saveAndFlush(productDiscount);

        // Get all the productDiscountList where addedDate in DEFAULT_ADDED_DATE or UPDATED_ADDED_DATE
        defaultProductDiscountShouldBeFound("addedDate.in=" + DEFAULT_ADDED_DATE + "," + UPDATED_ADDED_DATE);

        // Get all the productDiscountList where addedDate equals to UPDATED_ADDED_DATE
        defaultProductDiscountShouldNotBeFound("addedDate.in=" + UPDATED_ADDED_DATE);
    }

    @Test
    @Transactional
    void getAllProductDiscountsByAddedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        productDiscountRepository.saveAndFlush(productDiscount);

        // Get all the productDiscountList where addedDate is not null
        defaultProductDiscountShouldBeFound("addedDate.specified=true");

        // Get all the productDiscountList where addedDate is null
        defaultProductDiscountShouldNotBeFound("addedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProductDiscountsByDueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        productDiscountRepository.saveAndFlush(productDiscount);

        // Get all the productDiscountList where dueDate equals to DEFAULT_DUE_DATE
        defaultProductDiscountShouldBeFound("dueDate.equals=" + DEFAULT_DUE_DATE);

        // Get all the productDiscountList where dueDate equals to UPDATED_DUE_DATE
        defaultProductDiscountShouldNotBeFound("dueDate.equals=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllProductDiscountsByDueDateIsInShouldWork() throws Exception {
        // Initialize the database
        productDiscountRepository.saveAndFlush(productDiscount);

        // Get all the productDiscountList where dueDate in DEFAULT_DUE_DATE or UPDATED_DUE_DATE
        defaultProductDiscountShouldBeFound("dueDate.in=" + DEFAULT_DUE_DATE + "," + UPDATED_DUE_DATE);

        // Get all the productDiscountList where dueDate equals to UPDATED_DUE_DATE
        defaultProductDiscountShouldNotBeFound("dueDate.in=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllProductDiscountsByDueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        productDiscountRepository.saveAndFlush(productDiscount);

        // Get all the productDiscountList where dueDate is not null
        defaultProductDiscountShouldBeFound("dueDate.specified=true");

        // Get all the productDiscountList where dueDate is null
        defaultProductDiscountShouldNotBeFound("dueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProductDiscountsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        productDiscountRepository.saveAndFlush(productDiscount);

        // Get all the productDiscountList where description equals to DEFAULT_DESCRIPTION
        defaultProductDiscountShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the productDiscountList where description equals to UPDATED_DESCRIPTION
        defaultProductDiscountShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductDiscountsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        productDiscountRepository.saveAndFlush(productDiscount);

        // Get all the productDiscountList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultProductDiscountShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the productDiscountList where description equals to UPDATED_DESCRIPTION
        defaultProductDiscountShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductDiscountsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        productDiscountRepository.saveAndFlush(productDiscount);

        // Get all the productDiscountList where description is not null
        defaultProductDiscountShouldBeFound("description.specified=true");

        // Get all the productDiscountList where description is null
        defaultProductDiscountShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllProductDiscountsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        productDiscountRepository.saveAndFlush(productDiscount);

        // Get all the productDiscountList where description contains DEFAULT_DESCRIPTION
        defaultProductDiscountShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the productDiscountList where description contains UPDATED_DESCRIPTION
        defaultProductDiscountShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductDiscountsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        productDiscountRepository.saveAndFlush(productDiscount);

        // Get all the productDiscountList where description does not contain DEFAULT_DESCRIPTION
        defaultProductDiscountShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the productDiscountList where description does not contain UPDATED_DESCRIPTION
        defaultProductDiscountShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductDiscountsByDiscountIsEqualToSomething() throws Exception {
        Discount discount;
        if (TestUtil.findAll(em, Discount.class).isEmpty()) {
            productDiscountRepository.saveAndFlush(productDiscount);
            discount = DiscountResourceIT.createEntity(em);
        } else {
            discount = TestUtil.findAll(em, Discount.class).get(0);
        }
        em.persist(discount);
        em.flush();
        productDiscount.setDiscount(discount);
        productDiscountRepository.saveAndFlush(productDiscount);
        Long discountId = discount.getId();

        // Get all the productDiscountList where discount equals to discountId
        defaultProductDiscountShouldBeFound("discountId.equals=" + discountId);

        // Get all the productDiscountList where discount equals to (discountId + 1)
        defaultProductDiscountShouldNotBeFound("discountId.equals=" + (discountId + 1));
    }

    @Test
    @Transactional
    void getAllProductDiscountsByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            productDiscountRepository.saveAndFlush(productDiscount);
            product = ProductResourceIT.createEntity(em);
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(product);
        em.flush();
        productDiscount.setProduct(product);
        productDiscountRepository.saveAndFlush(productDiscount);
        Long productId = product.getId();

        // Get all the productDiscountList where product equals to productId
        defaultProductDiscountShouldBeFound("productId.equals=" + productId);

        // Get all the productDiscountList where product equals to (productId + 1)
        defaultProductDiscountShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductDiscountShouldBeFound(String filter) throws Exception {
        restProductDiscountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productDiscount.getId().intValue())))
            .andExpect(jsonPath("$.[*].addedDate").value(hasItem(DEFAULT_ADDED_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restProductDiscountMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductDiscountShouldNotBeFound(String filter) throws Exception {
        restProductDiscountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductDiscountMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductDiscount() throws Exception {
        // Get the productDiscount
        restProductDiscountMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductDiscount() throws Exception {
        // Initialize the database
        productDiscountRepository.saveAndFlush(productDiscount);

        int databaseSizeBeforeUpdate = productDiscountRepository.findAll().size();
        productDiscountSearchRepository.save(productDiscount);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productDiscountSearchRepository.findAll());

        // Update the productDiscount
        ProductDiscount updatedProductDiscount = productDiscountRepository.findById(productDiscount.getId()).get();
        // Disconnect from session so that the updates on updatedProductDiscount are not directly saved in db
        em.detach(updatedProductDiscount);
        updatedProductDiscount.addedDate(UPDATED_ADDED_DATE).dueDate(UPDATED_DUE_DATE).description(UPDATED_DESCRIPTION);
        ProductDiscountDTO productDiscountDTO = productDiscountMapper.toDto(updatedProductDiscount);

        restProductDiscountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productDiscountDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productDiscountDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProductDiscount in the database
        List<ProductDiscount> productDiscountList = productDiscountRepository.findAll();
        assertThat(productDiscountList).hasSize(databaseSizeBeforeUpdate);
        ProductDiscount testProductDiscount = productDiscountList.get(productDiscountList.size() - 1);
        assertThat(testProductDiscount.getAddedDate()).isEqualTo(UPDATED_ADDED_DATE);
        assertThat(testProductDiscount.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testProductDiscount.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(productDiscountSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ProductDiscount> productDiscountSearchList = IterableUtils.toList(productDiscountSearchRepository.findAll());
                ProductDiscount testProductDiscountSearch = productDiscountSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testProductDiscountSearch.getAddedDate()).isEqualTo(UPDATED_ADDED_DATE);
                assertThat(testProductDiscountSearch.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
                assertThat(testProductDiscountSearch.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
            });
    }

    @Test
    @Transactional
    void putNonExistingProductDiscount() throws Exception {
        int databaseSizeBeforeUpdate = productDiscountRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productDiscountSearchRepository.findAll());
        productDiscount.setId(count.incrementAndGet());

        // Create the ProductDiscount
        ProductDiscountDTO productDiscountDTO = productDiscountMapper.toDto(productDiscount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductDiscountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productDiscountDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productDiscountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductDiscount in the database
        List<ProductDiscount> productDiscountList = productDiscountRepository.findAll();
        assertThat(productDiscountList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productDiscountSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductDiscount() throws Exception {
        int databaseSizeBeforeUpdate = productDiscountRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productDiscountSearchRepository.findAll());
        productDiscount.setId(count.incrementAndGet());

        // Create the ProductDiscount
        ProductDiscountDTO productDiscountDTO = productDiscountMapper.toDto(productDiscount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductDiscountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productDiscountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductDiscount in the database
        List<ProductDiscount> productDiscountList = productDiscountRepository.findAll();
        assertThat(productDiscountList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productDiscountSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductDiscount() throws Exception {
        int databaseSizeBeforeUpdate = productDiscountRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productDiscountSearchRepository.findAll());
        productDiscount.setId(count.incrementAndGet());

        // Create the ProductDiscount
        ProductDiscountDTO productDiscountDTO = productDiscountMapper.toDto(productDiscount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductDiscountMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productDiscountDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductDiscount in the database
        List<ProductDiscount> productDiscountList = productDiscountRepository.findAll();
        assertThat(productDiscountList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productDiscountSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateProductDiscountWithPatch() throws Exception {
        // Initialize the database
        productDiscountRepository.saveAndFlush(productDiscount);

        int databaseSizeBeforeUpdate = productDiscountRepository.findAll().size();

        // Update the productDiscount using partial update
        ProductDiscount partialUpdatedProductDiscount = new ProductDiscount();
        partialUpdatedProductDiscount.setId(productDiscount.getId());

        partialUpdatedProductDiscount.addedDate(UPDATED_ADDED_DATE).dueDate(UPDATED_DUE_DATE);

        restProductDiscountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductDiscount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductDiscount))
            )
            .andExpect(status().isOk());

        // Validate the ProductDiscount in the database
        List<ProductDiscount> productDiscountList = productDiscountRepository.findAll();
        assertThat(productDiscountList).hasSize(databaseSizeBeforeUpdate);
        ProductDiscount testProductDiscount = productDiscountList.get(productDiscountList.size() - 1);
        assertThat(testProductDiscount.getAddedDate()).isEqualTo(UPDATED_ADDED_DATE);
        assertThat(testProductDiscount.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testProductDiscount.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateProductDiscountWithPatch() throws Exception {
        // Initialize the database
        productDiscountRepository.saveAndFlush(productDiscount);

        int databaseSizeBeforeUpdate = productDiscountRepository.findAll().size();

        // Update the productDiscount using partial update
        ProductDiscount partialUpdatedProductDiscount = new ProductDiscount();
        partialUpdatedProductDiscount.setId(productDiscount.getId());

        partialUpdatedProductDiscount.addedDate(UPDATED_ADDED_DATE).dueDate(UPDATED_DUE_DATE).description(UPDATED_DESCRIPTION);

        restProductDiscountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductDiscount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductDiscount))
            )
            .andExpect(status().isOk());

        // Validate the ProductDiscount in the database
        List<ProductDiscount> productDiscountList = productDiscountRepository.findAll();
        assertThat(productDiscountList).hasSize(databaseSizeBeforeUpdate);
        ProductDiscount testProductDiscount = productDiscountList.get(productDiscountList.size() - 1);
        assertThat(testProductDiscount.getAddedDate()).isEqualTo(UPDATED_ADDED_DATE);
        assertThat(testProductDiscount.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testProductDiscount.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingProductDiscount() throws Exception {
        int databaseSizeBeforeUpdate = productDiscountRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productDiscountSearchRepository.findAll());
        productDiscount.setId(count.incrementAndGet());

        // Create the ProductDiscount
        ProductDiscountDTO productDiscountDTO = productDiscountMapper.toDto(productDiscount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductDiscountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productDiscountDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productDiscountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductDiscount in the database
        List<ProductDiscount> productDiscountList = productDiscountRepository.findAll();
        assertThat(productDiscountList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productDiscountSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductDiscount() throws Exception {
        int databaseSizeBeforeUpdate = productDiscountRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productDiscountSearchRepository.findAll());
        productDiscount.setId(count.incrementAndGet());

        // Create the ProductDiscount
        ProductDiscountDTO productDiscountDTO = productDiscountMapper.toDto(productDiscount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductDiscountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productDiscountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductDiscount in the database
        List<ProductDiscount> productDiscountList = productDiscountRepository.findAll();
        assertThat(productDiscountList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productDiscountSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductDiscount() throws Exception {
        int databaseSizeBeforeUpdate = productDiscountRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productDiscountSearchRepository.findAll());
        productDiscount.setId(count.incrementAndGet());

        // Create the ProductDiscount
        ProductDiscountDTO productDiscountDTO = productDiscountMapper.toDto(productDiscount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductDiscountMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productDiscountDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductDiscount in the database
        List<ProductDiscount> productDiscountList = productDiscountRepository.findAll();
        assertThat(productDiscountList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productDiscountSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteProductDiscount() throws Exception {
        // Initialize the database
        productDiscountRepository.saveAndFlush(productDiscount);
        productDiscountRepository.save(productDiscount);
        productDiscountSearchRepository.save(productDiscount);

        int databaseSizeBeforeDelete = productDiscountRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productDiscountSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the productDiscount
        restProductDiscountMockMvc
            .perform(delete(ENTITY_API_URL_ID, productDiscount.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductDiscount> productDiscountList = productDiscountRepository.findAll();
        assertThat(productDiscountList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productDiscountSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchProductDiscount() throws Exception {
        // Initialize the database
        productDiscount = productDiscountRepository.saveAndFlush(productDiscount);
        productDiscountSearchRepository.save(productDiscount);

        // Search the productDiscount
        restProductDiscountMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + productDiscount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productDiscount.getId().intValue())))
            .andExpect(jsonPath("$.[*].addedDate").value(hasItem(DEFAULT_ADDED_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
}
