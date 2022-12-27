package com.fastshop.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fastshop.IntegrationTest;
import com.fastshop.domain.Brand;
import com.fastshop.domain.Product;
import com.fastshop.domain.ProductType;
import com.fastshop.domain.Seller;
import com.fastshop.domain.SellerStatistics;
import com.fastshop.domain.User;
import com.fastshop.repository.SellerRepository;
import com.fastshop.repository.search.SellerSearchRepository;
import com.fastshop.service.criteria.SellerCriteria;
import com.fastshop.service.dto.SellerDTO;
import com.fastshop.service.mapper.SellerMapper;
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
 * Integration tests for the {@link SellerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SellerResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SHOP_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SHOP_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MAIL = "AAAAAAAAAA";
    private static final String UPDATED_MAIL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVATED = false;
    private static final Boolean UPDATED_ACTIVATED = true;

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_TCKN = "AAAAAAAAAAA";
    private static final String UPDATED_TCKN = "BBBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final Instant DEFAULT_PLACED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PLACED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/sellers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/sellers";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private SellerMapper sellerMapper;

    @Autowired
    private SellerSearchRepository sellerSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSellerMockMvc;

    private Seller seller;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Seller createEntity(EntityManager em) {
        Seller seller = new Seller()
            .name(DEFAULT_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .shopName(DEFAULT_SHOP_NAME)
            .mail(DEFAULT_MAIL)
            .activated(DEFAULT_ACTIVATED)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .tckn(DEFAULT_TCKN)
            .phone(DEFAULT_PHONE)
            .city(DEFAULT_CITY)
            .placedDate(DEFAULT_PLACED_DATE);
        return seller;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Seller createUpdatedEntity(EntityManager em) {
        Seller seller = new Seller()
            .name(UPDATED_NAME)
            .lastName(UPDATED_LAST_NAME)
            .shopName(UPDATED_SHOP_NAME)
            .mail(UPDATED_MAIL)
            .activated(UPDATED_ACTIVATED)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .tckn(UPDATED_TCKN)
            .phone(UPDATED_PHONE)
            .city(UPDATED_CITY)
            .placedDate(UPDATED_PLACED_DATE);
        return seller;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        sellerSearchRepository.deleteAll();
        assertThat(sellerSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        seller = createEntity(em);
    }

    @Test
    @Transactional
    void createSeller() throws Exception {
        int databaseSizeBeforeCreate = sellerRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sellerSearchRepository.findAll());
        // Create the Seller
        SellerDTO sellerDTO = sellerMapper.toDto(seller);
        restSellerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sellerDTO)))
            .andExpect(status().isCreated());

        // Validate the Seller in the database
        List<Seller> sellerList = sellerRepository.findAll();
        assertThat(sellerList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(sellerSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Seller testSeller = sellerList.get(sellerList.size() - 1);
        assertThat(testSeller.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSeller.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testSeller.getShopName()).isEqualTo(DEFAULT_SHOP_NAME);
        assertThat(testSeller.getMail()).isEqualTo(DEFAULT_MAIL);
        assertThat(testSeller.getActivated()).isEqualTo(DEFAULT_ACTIVATED);
        assertThat(testSeller.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testSeller.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testSeller.getTckn()).isEqualTo(DEFAULT_TCKN);
        assertThat(testSeller.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testSeller.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testSeller.getPlacedDate()).isEqualTo(DEFAULT_PLACED_DATE);
    }

    @Test
    @Transactional
    void createSellerWithExistingId() throws Exception {
        // Create the Seller with an existing ID
        seller.setId(1L);
        SellerDTO sellerDTO = sellerMapper.toDto(seller);

        int databaseSizeBeforeCreate = sellerRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sellerSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restSellerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sellerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Seller in the database
        List<Seller> sellerList = sellerRepository.findAll();
        assertThat(sellerList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sellerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = sellerRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sellerSearchRepository.findAll());
        // set the field null
        seller.setName(null);

        // Create the Seller, which fails.
        SellerDTO sellerDTO = sellerMapper.toDto(seller);

        restSellerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sellerDTO)))
            .andExpect(status().isBadRequest());

        List<Seller> sellerList = sellerRepository.findAll();
        assertThat(sellerList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sellerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = sellerRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sellerSearchRepository.findAll());
        // set the field null
        seller.setLastName(null);

        // Create the Seller, which fails.
        SellerDTO sellerDTO = sellerMapper.toDto(seller);

        restSellerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sellerDTO)))
            .andExpect(status().isBadRequest());

        List<Seller> sellerList = sellerRepository.findAll();
        assertThat(sellerList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sellerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkShopNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = sellerRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sellerSearchRepository.findAll());
        // set the field null
        seller.setShopName(null);

        // Create the Seller, which fails.
        SellerDTO sellerDTO = sellerMapper.toDto(seller);

        restSellerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sellerDTO)))
            .andExpect(status().isBadRequest());

        List<Seller> sellerList = sellerRepository.findAll();
        assertThat(sellerList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sellerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkMailIsRequired() throws Exception {
        int databaseSizeBeforeTest = sellerRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sellerSearchRepository.findAll());
        // set the field null
        seller.setMail(null);

        // Create the Seller, which fails.
        SellerDTO sellerDTO = sellerMapper.toDto(seller);

        restSellerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sellerDTO)))
            .andExpect(status().isBadRequest());

        List<Seller> sellerList = sellerRepository.findAll();
        assertThat(sellerList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sellerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkTcknIsRequired() throws Exception {
        int databaseSizeBeforeTest = sellerRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sellerSearchRepository.findAll());
        // set the field null
        seller.setTckn(null);

        // Create the Seller, which fails.
        SellerDTO sellerDTO = sellerMapper.toDto(seller);

        restSellerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sellerDTO)))
            .andExpect(status().isBadRequest());

        List<Seller> sellerList = sellerRepository.findAll();
        assertThat(sellerList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sellerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = sellerRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sellerSearchRepository.findAll());
        // set the field null
        seller.setPhone(null);

        // Create the Seller, which fails.
        SellerDTO sellerDTO = sellerMapper.toDto(seller);

        restSellerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sellerDTO)))
            .andExpect(status().isBadRequest());

        List<Seller> sellerList = sellerRepository.findAll();
        assertThat(sellerList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sellerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = sellerRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sellerSearchRepository.findAll());
        // set the field null
        seller.setCity(null);

        // Create the Seller, which fails.
        SellerDTO sellerDTO = sellerMapper.toDto(seller);

        restSellerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sellerDTO)))
            .andExpect(status().isBadRequest());

        List<Seller> sellerList = sellerRepository.findAll();
        assertThat(sellerList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sellerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllSellers() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList
        restSellerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(seller.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].shopName").value(hasItem(DEFAULT_SHOP_NAME)))
            .andExpect(jsonPath("$.[*].mail").value(hasItem(DEFAULT_MAIL)))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].tckn").value(hasItem(DEFAULT_TCKN)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].placedDate").value(hasItem(DEFAULT_PLACED_DATE.toString())));
    }

    @Test
    @Transactional
    void getSeller() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get the seller
        restSellerMockMvc
            .perform(get(ENTITY_API_URL_ID, seller.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(seller.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.shopName").value(DEFAULT_SHOP_NAME))
            .andExpect(jsonPath("$.mail").value(DEFAULT_MAIL))
            .andExpect(jsonPath("$.activated").value(DEFAULT_ACTIVATED.booleanValue()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.tckn").value(DEFAULT_TCKN))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.placedDate").value(DEFAULT_PLACED_DATE.toString()));
    }

    @Test
    @Transactional
    void getSellersByIdFiltering() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        Long id = seller.getId();

        defaultSellerShouldBeFound("id.equals=" + id);
        defaultSellerShouldNotBeFound("id.notEquals=" + id);

        defaultSellerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSellerShouldNotBeFound("id.greaterThan=" + id);

        defaultSellerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSellerShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSellersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where name equals to DEFAULT_NAME
        defaultSellerShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the sellerList where name equals to UPDATED_NAME
        defaultSellerShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSellersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSellerShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the sellerList where name equals to UPDATED_NAME
        defaultSellerShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSellersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where name is not null
        defaultSellerShouldBeFound("name.specified=true");

        // Get all the sellerList where name is null
        defaultSellerShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllSellersByNameContainsSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where name contains DEFAULT_NAME
        defaultSellerShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the sellerList where name contains UPDATED_NAME
        defaultSellerShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSellersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where name does not contain DEFAULT_NAME
        defaultSellerShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the sellerList where name does not contain UPDATED_NAME
        defaultSellerShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSellersByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where lastName equals to DEFAULT_LAST_NAME
        defaultSellerShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the sellerList where lastName equals to UPDATED_LAST_NAME
        defaultSellerShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllSellersByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultSellerShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the sellerList where lastName equals to UPDATED_LAST_NAME
        defaultSellerShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllSellersByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where lastName is not null
        defaultSellerShouldBeFound("lastName.specified=true");

        // Get all the sellerList where lastName is null
        defaultSellerShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllSellersByLastNameContainsSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where lastName contains DEFAULT_LAST_NAME
        defaultSellerShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the sellerList where lastName contains UPDATED_LAST_NAME
        defaultSellerShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllSellersByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where lastName does not contain DEFAULT_LAST_NAME
        defaultSellerShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the sellerList where lastName does not contain UPDATED_LAST_NAME
        defaultSellerShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllSellersByShopNameIsEqualToSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where shopName equals to DEFAULT_SHOP_NAME
        defaultSellerShouldBeFound("shopName.equals=" + DEFAULT_SHOP_NAME);

        // Get all the sellerList where shopName equals to UPDATED_SHOP_NAME
        defaultSellerShouldNotBeFound("shopName.equals=" + UPDATED_SHOP_NAME);
    }

    @Test
    @Transactional
    void getAllSellersByShopNameIsInShouldWork() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where shopName in DEFAULT_SHOP_NAME or UPDATED_SHOP_NAME
        defaultSellerShouldBeFound("shopName.in=" + DEFAULT_SHOP_NAME + "," + UPDATED_SHOP_NAME);

        // Get all the sellerList where shopName equals to UPDATED_SHOP_NAME
        defaultSellerShouldNotBeFound("shopName.in=" + UPDATED_SHOP_NAME);
    }

    @Test
    @Transactional
    void getAllSellersByShopNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where shopName is not null
        defaultSellerShouldBeFound("shopName.specified=true");

        // Get all the sellerList where shopName is null
        defaultSellerShouldNotBeFound("shopName.specified=false");
    }

    @Test
    @Transactional
    void getAllSellersByShopNameContainsSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where shopName contains DEFAULT_SHOP_NAME
        defaultSellerShouldBeFound("shopName.contains=" + DEFAULT_SHOP_NAME);

        // Get all the sellerList where shopName contains UPDATED_SHOP_NAME
        defaultSellerShouldNotBeFound("shopName.contains=" + UPDATED_SHOP_NAME);
    }

    @Test
    @Transactional
    void getAllSellersByShopNameNotContainsSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where shopName does not contain DEFAULT_SHOP_NAME
        defaultSellerShouldNotBeFound("shopName.doesNotContain=" + DEFAULT_SHOP_NAME);

        // Get all the sellerList where shopName does not contain UPDATED_SHOP_NAME
        defaultSellerShouldBeFound("shopName.doesNotContain=" + UPDATED_SHOP_NAME);
    }

    @Test
    @Transactional
    void getAllSellersByMailIsEqualToSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where mail equals to DEFAULT_MAIL
        defaultSellerShouldBeFound("mail.equals=" + DEFAULT_MAIL);

        // Get all the sellerList where mail equals to UPDATED_MAIL
        defaultSellerShouldNotBeFound("mail.equals=" + UPDATED_MAIL);
    }

    @Test
    @Transactional
    void getAllSellersByMailIsInShouldWork() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where mail in DEFAULT_MAIL or UPDATED_MAIL
        defaultSellerShouldBeFound("mail.in=" + DEFAULT_MAIL + "," + UPDATED_MAIL);

        // Get all the sellerList where mail equals to UPDATED_MAIL
        defaultSellerShouldNotBeFound("mail.in=" + UPDATED_MAIL);
    }

    @Test
    @Transactional
    void getAllSellersByMailIsNullOrNotNull() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where mail is not null
        defaultSellerShouldBeFound("mail.specified=true");

        // Get all the sellerList where mail is null
        defaultSellerShouldNotBeFound("mail.specified=false");
    }

    @Test
    @Transactional
    void getAllSellersByMailContainsSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where mail contains DEFAULT_MAIL
        defaultSellerShouldBeFound("mail.contains=" + DEFAULT_MAIL);

        // Get all the sellerList where mail contains UPDATED_MAIL
        defaultSellerShouldNotBeFound("mail.contains=" + UPDATED_MAIL);
    }

    @Test
    @Transactional
    void getAllSellersByMailNotContainsSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where mail does not contain DEFAULT_MAIL
        defaultSellerShouldNotBeFound("mail.doesNotContain=" + DEFAULT_MAIL);

        // Get all the sellerList where mail does not contain UPDATED_MAIL
        defaultSellerShouldBeFound("mail.doesNotContain=" + UPDATED_MAIL);
    }

    @Test
    @Transactional
    void getAllSellersByActivatedIsEqualToSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where activated equals to DEFAULT_ACTIVATED
        defaultSellerShouldBeFound("activated.equals=" + DEFAULT_ACTIVATED);

        // Get all the sellerList where activated equals to UPDATED_ACTIVATED
        defaultSellerShouldNotBeFound("activated.equals=" + UPDATED_ACTIVATED);
    }

    @Test
    @Transactional
    void getAllSellersByActivatedIsInShouldWork() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where activated in DEFAULT_ACTIVATED or UPDATED_ACTIVATED
        defaultSellerShouldBeFound("activated.in=" + DEFAULT_ACTIVATED + "," + UPDATED_ACTIVATED);

        // Get all the sellerList where activated equals to UPDATED_ACTIVATED
        defaultSellerShouldNotBeFound("activated.in=" + UPDATED_ACTIVATED);
    }

    @Test
    @Transactional
    void getAllSellersByActivatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where activated is not null
        defaultSellerShouldBeFound("activated.specified=true");

        // Get all the sellerList where activated is null
        defaultSellerShouldNotBeFound("activated.specified=false");
    }

    @Test
    @Transactional
    void getAllSellersByTcknIsEqualToSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where tckn equals to DEFAULT_TCKN
        defaultSellerShouldBeFound("tckn.equals=" + DEFAULT_TCKN);

        // Get all the sellerList where tckn equals to UPDATED_TCKN
        defaultSellerShouldNotBeFound("tckn.equals=" + UPDATED_TCKN);
    }

    @Test
    @Transactional
    void getAllSellersByTcknIsInShouldWork() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where tckn in DEFAULT_TCKN or UPDATED_TCKN
        defaultSellerShouldBeFound("tckn.in=" + DEFAULT_TCKN + "," + UPDATED_TCKN);

        // Get all the sellerList where tckn equals to UPDATED_TCKN
        defaultSellerShouldNotBeFound("tckn.in=" + UPDATED_TCKN);
    }

    @Test
    @Transactional
    void getAllSellersByTcknIsNullOrNotNull() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where tckn is not null
        defaultSellerShouldBeFound("tckn.specified=true");

        // Get all the sellerList where tckn is null
        defaultSellerShouldNotBeFound("tckn.specified=false");
    }

    @Test
    @Transactional
    void getAllSellersByTcknContainsSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where tckn contains DEFAULT_TCKN
        defaultSellerShouldBeFound("tckn.contains=" + DEFAULT_TCKN);

        // Get all the sellerList where tckn contains UPDATED_TCKN
        defaultSellerShouldNotBeFound("tckn.contains=" + UPDATED_TCKN);
    }

    @Test
    @Transactional
    void getAllSellersByTcknNotContainsSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where tckn does not contain DEFAULT_TCKN
        defaultSellerShouldNotBeFound("tckn.doesNotContain=" + DEFAULT_TCKN);

        // Get all the sellerList where tckn does not contain UPDATED_TCKN
        defaultSellerShouldBeFound("tckn.doesNotContain=" + UPDATED_TCKN);
    }

    @Test
    @Transactional
    void getAllSellersByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where phone equals to DEFAULT_PHONE
        defaultSellerShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the sellerList where phone equals to UPDATED_PHONE
        defaultSellerShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllSellersByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultSellerShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the sellerList where phone equals to UPDATED_PHONE
        defaultSellerShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllSellersByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where phone is not null
        defaultSellerShouldBeFound("phone.specified=true");

        // Get all the sellerList where phone is null
        defaultSellerShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    void getAllSellersByPhoneContainsSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where phone contains DEFAULT_PHONE
        defaultSellerShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the sellerList where phone contains UPDATED_PHONE
        defaultSellerShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllSellersByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where phone does not contain DEFAULT_PHONE
        defaultSellerShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the sellerList where phone does not contain UPDATED_PHONE
        defaultSellerShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllSellersByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where city equals to DEFAULT_CITY
        defaultSellerShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the sellerList where city equals to UPDATED_CITY
        defaultSellerShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllSellersByCityIsInShouldWork() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where city in DEFAULT_CITY or UPDATED_CITY
        defaultSellerShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the sellerList where city equals to UPDATED_CITY
        defaultSellerShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllSellersByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where city is not null
        defaultSellerShouldBeFound("city.specified=true");

        // Get all the sellerList where city is null
        defaultSellerShouldNotBeFound("city.specified=false");
    }

    @Test
    @Transactional
    void getAllSellersByCityContainsSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where city contains DEFAULT_CITY
        defaultSellerShouldBeFound("city.contains=" + DEFAULT_CITY);

        // Get all the sellerList where city contains UPDATED_CITY
        defaultSellerShouldNotBeFound("city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllSellersByCityNotContainsSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where city does not contain DEFAULT_CITY
        defaultSellerShouldNotBeFound("city.doesNotContain=" + DEFAULT_CITY);

        // Get all the sellerList where city does not contain UPDATED_CITY
        defaultSellerShouldBeFound("city.doesNotContain=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllSellersByPlacedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where placedDate equals to DEFAULT_PLACED_DATE
        defaultSellerShouldBeFound("placedDate.equals=" + DEFAULT_PLACED_DATE);

        // Get all the sellerList where placedDate equals to UPDATED_PLACED_DATE
        defaultSellerShouldNotBeFound("placedDate.equals=" + UPDATED_PLACED_DATE);
    }

    @Test
    @Transactional
    void getAllSellersByPlacedDateIsInShouldWork() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where placedDate in DEFAULT_PLACED_DATE or UPDATED_PLACED_DATE
        defaultSellerShouldBeFound("placedDate.in=" + DEFAULT_PLACED_DATE + "," + UPDATED_PLACED_DATE);

        // Get all the sellerList where placedDate equals to UPDATED_PLACED_DATE
        defaultSellerShouldNotBeFound("placedDate.in=" + UPDATED_PLACED_DATE);
    }

    @Test
    @Transactional
    void getAllSellersByPlacedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where placedDate is not null
        defaultSellerShouldBeFound("placedDate.specified=true");

        // Get all the sellerList where placedDate is null
        defaultSellerShouldNotBeFound("placedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSellersByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            sellerRepository.saveAndFlush(seller);
            user = UserResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        seller.setUser(user);
        sellerRepository.saveAndFlush(seller);
        Long userId = user.getId();

        // Get all the sellerList where user equals to userId
        defaultSellerShouldBeFound("userId.equals=" + userId);

        // Get all the sellerList where user equals to (userId + 1)
        defaultSellerShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllSellersByProductsIsEqualToSomething() throws Exception {
        Product products;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            sellerRepository.saveAndFlush(seller);
            products = ProductResourceIT.createEntity(em);
        } else {
            products = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(products);
        em.flush();
        seller.addProducts(products);
        sellerRepository.saveAndFlush(seller);
        Long productsId = products.getId();

        // Get all the sellerList where products equals to productsId
        defaultSellerShouldBeFound("productsId.equals=" + productsId);

        // Get all the sellerList where products equals to (productsId + 1)
        defaultSellerShouldNotBeFound("productsId.equals=" + (productsId + 1));
    }

    @Test
    @Transactional
    void getAllSellersBySellerProductTypeIsEqualToSomething() throws Exception {
        ProductType sellerProductType;
        if (TestUtil.findAll(em, ProductType.class).isEmpty()) {
            sellerRepository.saveAndFlush(seller);
            sellerProductType = ProductTypeResourceIT.createEntity(em);
        } else {
            sellerProductType = TestUtil.findAll(em, ProductType.class).get(0);
        }
        em.persist(sellerProductType);
        em.flush();
        seller.setSellerProductType(sellerProductType);
        sellerRepository.saveAndFlush(seller);
        Long sellerProductTypeId = sellerProductType.getId();

        // Get all the sellerList where sellerProductType equals to sellerProductTypeId
        defaultSellerShouldBeFound("sellerProductTypeId.equals=" + sellerProductTypeId);

        // Get all the sellerList where sellerProductType equals to (sellerProductTypeId + 1)
        defaultSellerShouldNotBeFound("sellerProductTypeId.equals=" + (sellerProductTypeId + 1));
    }

    @Test
    @Transactional
    void getAllSellersByBrandIsEqualToSomething() throws Exception {
        Brand brand;
        if (TestUtil.findAll(em, Brand.class).isEmpty()) {
            sellerRepository.saveAndFlush(seller);
            brand = BrandResourceIT.createEntity(em);
        } else {
            brand = TestUtil.findAll(em, Brand.class).get(0);
        }
        em.persist(brand);
        em.flush();
        seller.setBrand(brand);
        brand.setSeller(seller);
        sellerRepository.saveAndFlush(seller);
        Long brandId = brand.getId();

        // Get all the sellerList where brand equals to brandId
        defaultSellerShouldBeFound("brandId.equals=" + brandId);

        // Get all the sellerList where brand equals to (brandId + 1)
        defaultSellerShouldNotBeFound("brandId.equals=" + (brandId + 1));
    }

    @Test
    @Transactional
    void getAllSellersBySellerStatisticsIsEqualToSomething() throws Exception {
        SellerStatistics sellerStatistics;
        if (TestUtil.findAll(em, SellerStatistics.class).isEmpty()) {
            sellerRepository.saveAndFlush(seller);
            sellerStatistics = SellerStatisticsResourceIT.createEntity(em);
        } else {
            sellerStatistics = TestUtil.findAll(em, SellerStatistics.class).get(0);
        }
        em.persist(sellerStatistics);
        em.flush();
        seller.setSellerStatistics(sellerStatistics);
        sellerStatistics.setSeller(seller);
        sellerRepository.saveAndFlush(seller);
        Long sellerStatisticsId = sellerStatistics.getId();

        // Get all the sellerList where sellerStatistics equals to sellerStatisticsId
        defaultSellerShouldBeFound("sellerStatisticsId.equals=" + sellerStatisticsId);

        // Get all the sellerList where sellerStatistics equals to (sellerStatisticsId + 1)
        defaultSellerShouldNotBeFound("sellerStatisticsId.equals=" + (sellerStatisticsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSellerShouldBeFound(String filter) throws Exception {
        restSellerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(seller.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].shopName").value(hasItem(DEFAULT_SHOP_NAME)))
            .andExpect(jsonPath("$.[*].mail").value(hasItem(DEFAULT_MAIL)))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].tckn").value(hasItem(DEFAULT_TCKN)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].placedDate").value(hasItem(DEFAULT_PLACED_DATE.toString())));

        // Check, that the count call also returns 1
        restSellerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSellerShouldNotBeFound(String filter) throws Exception {
        restSellerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSellerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSeller() throws Exception {
        // Get the seller
        restSellerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSeller() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        int databaseSizeBeforeUpdate = sellerRepository.findAll().size();
        sellerSearchRepository.save(seller);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sellerSearchRepository.findAll());

        // Update the seller
        Seller updatedSeller = sellerRepository.findById(seller.getId()).get();
        // Disconnect from session so that the updates on updatedSeller are not directly saved in db
        em.detach(updatedSeller);
        updatedSeller
            .name(UPDATED_NAME)
            .lastName(UPDATED_LAST_NAME)
            .shopName(UPDATED_SHOP_NAME)
            .mail(UPDATED_MAIL)
            .activated(UPDATED_ACTIVATED)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .tckn(UPDATED_TCKN)
            .phone(UPDATED_PHONE)
            .city(UPDATED_CITY)
            .placedDate(UPDATED_PLACED_DATE);
        SellerDTO sellerDTO = sellerMapper.toDto(updatedSeller);

        restSellerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sellerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sellerDTO))
            )
            .andExpect(status().isOk());

        // Validate the Seller in the database
        List<Seller> sellerList = sellerRepository.findAll();
        assertThat(sellerList).hasSize(databaseSizeBeforeUpdate);
        Seller testSeller = sellerList.get(sellerList.size() - 1);
        assertThat(testSeller.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSeller.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testSeller.getShopName()).isEqualTo(UPDATED_SHOP_NAME);
        assertThat(testSeller.getMail()).isEqualTo(UPDATED_MAIL);
        assertThat(testSeller.getActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testSeller.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testSeller.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testSeller.getTckn()).isEqualTo(UPDATED_TCKN);
        assertThat(testSeller.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testSeller.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testSeller.getPlacedDate()).isEqualTo(UPDATED_PLACED_DATE);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(sellerSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Seller> sellerSearchList = IterableUtils.toList(sellerSearchRepository.findAll());
                Seller testSellerSearch = sellerSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testSellerSearch.getName()).isEqualTo(UPDATED_NAME);
                assertThat(testSellerSearch.getLastName()).isEqualTo(UPDATED_LAST_NAME);
                assertThat(testSellerSearch.getShopName()).isEqualTo(UPDATED_SHOP_NAME);
                assertThat(testSellerSearch.getMail()).isEqualTo(UPDATED_MAIL);
                assertThat(testSellerSearch.getActivated()).isEqualTo(UPDATED_ACTIVATED);
                assertThat(testSellerSearch.getImage()).isEqualTo(UPDATED_IMAGE);
                assertThat(testSellerSearch.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
                assertThat(testSellerSearch.getTckn()).isEqualTo(UPDATED_TCKN);
                assertThat(testSellerSearch.getPhone()).isEqualTo(UPDATED_PHONE);
                assertThat(testSellerSearch.getCity()).isEqualTo(UPDATED_CITY);
                assertThat(testSellerSearch.getPlacedDate()).isEqualTo(UPDATED_PLACED_DATE);
            });
    }

    @Test
    @Transactional
    void putNonExistingSeller() throws Exception {
        int databaseSizeBeforeUpdate = sellerRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sellerSearchRepository.findAll());
        seller.setId(count.incrementAndGet());

        // Create the Seller
        SellerDTO sellerDTO = sellerMapper.toDto(seller);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSellerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sellerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sellerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Seller in the database
        List<Seller> sellerList = sellerRepository.findAll();
        assertThat(sellerList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sellerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchSeller() throws Exception {
        int databaseSizeBeforeUpdate = sellerRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sellerSearchRepository.findAll());
        seller.setId(count.incrementAndGet());

        // Create the Seller
        SellerDTO sellerDTO = sellerMapper.toDto(seller);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSellerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sellerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Seller in the database
        List<Seller> sellerList = sellerRepository.findAll();
        assertThat(sellerList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sellerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSeller() throws Exception {
        int databaseSizeBeforeUpdate = sellerRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sellerSearchRepository.findAll());
        seller.setId(count.incrementAndGet());

        // Create the Seller
        SellerDTO sellerDTO = sellerMapper.toDto(seller);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSellerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sellerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Seller in the database
        List<Seller> sellerList = sellerRepository.findAll();
        assertThat(sellerList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sellerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateSellerWithPatch() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        int databaseSizeBeforeUpdate = sellerRepository.findAll().size();

        // Update the seller using partial update
        Seller partialUpdatedSeller = new Seller();
        partialUpdatedSeller.setId(seller.getId());

        partialUpdatedSeller.mail(UPDATED_MAIL).phone(UPDATED_PHONE);

        restSellerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSeller.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSeller))
            )
            .andExpect(status().isOk());

        // Validate the Seller in the database
        List<Seller> sellerList = sellerRepository.findAll();
        assertThat(sellerList).hasSize(databaseSizeBeforeUpdate);
        Seller testSeller = sellerList.get(sellerList.size() - 1);
        assertThat(testSeller.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSeller.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testSeller.getShopName()).isEqualTo(DEFAULT_SHOP_NAME);
        assertThat(testSeller.getMail()).isEqualTo(UPDATED_MAIL);
        assertThat(testSeller.getActivated()).isEqualTo(DEFAULT_ACTIVATED);
        assertThat(testSeller.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testSeller.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testSeller.getTckn()).isEqualTo(DEFAULT_TCKN);
        assertThat(testSeller.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testSeller.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testSeller.getPlacedDate()).isEqualTo(DEFAULT_PLACED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateSellerWithPatch() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        int databaseSizeBeforeUpdate = sellerRepository.findAll().size();

        // Update the seller using partial update
        Seller partialUpdatedSeller = new Seller();
        partialUpdatedSeller.setId(seller.getId());

        partialUpdatedSeller
            .name(UPDATED_NAME)
            .lastName(UPDATED_LAST_NAME)
            .shopName(UPDATED_SHOP_NAME)
            .mail(UPDATED_MAIL)
            .activated(UPDATED_ACTIVATED)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .tckn(UPDATED_TCKN)
            .phone(UPDATED_PHONE)
            .city(UPDATED_CITY)
            .placedDate(UPDATED_PLACED_DATE);

        restSellerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSeller.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSeller))
            )
            .andExpect(status().isOk());

        // Validate the Seller in the database
        List<Seller> sellerList = sellerRepository.findAll();
        assertThat(sellerList).hasSize(databaseSizeBeforeUpdate);
        Seller testSeller = sellerList.get(sellerList.size() - 1);
        assertThat(testSeller.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSeller.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testSeller.getShopName()).isEqualTo(UPDATED_SHOP_NAME);
        assertThat(testSeller.getMail()).isEqualTo(UPDATED_MAIL);
        assertThat(testSeller.getActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testSeller.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testSeller.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testSeller.getTckn()).isEqualTo(UPDATED_TCKN);
        assertThat(testSeller.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testSeller.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testSeller.getPlacedDate()).isEqualTo(UPDATED_PLACED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingSeller() throws Exception {
        int databaseSizeBeforeUpdate = sellerRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sellerSearchRepository.findAll());
        seller.setId(count.incrementAndGet());

        // Create the Seller
        SellerDTO sellerDTO = sellerMapper.toDto(seller);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSellerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sellerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sellerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Seller in the database
        List<Seller> sellerList = sellerRepository.findAll();
        assertThat(sellerList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sellerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSeller() throws Exception {
        int databaseSizeBeforeUpdate = sellerRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sellerSearchRepository.findAll());
        seller.setId(count.incrementAndGet());

        // Create the Seller
        SellerDTO sellerDTO = sellerMapper.toDto(seller);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSellerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sellerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Seller in the database
        List<Seller> sellerList = sellerRepository.findAll();
        assertThat(sellerList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sellerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSeller() throws Exception {
        int databaseSizeBeforeUpdate = sellerRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sellerSearchRepository.findAll());
        seller.setId(count.incrementAndGet());

        // Create the Seller
        SellerDTO sellerDTO = sellerMapper.toDto(seller);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSellerMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sellerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Seller in the database
        List<Seller> sellerList = sellerRepository.findAll();
        assertThat(sellerList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sellerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteSeller() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);
        sellerRepository.save(seller);
        sellerSearchRepository.save(seller);

        int databaseSizeBeforeDelete = sellerRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(sellerSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the seller
        restSellerMockMvc
            .perform(delete(ENTITY_API_URL_ID, seller.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Seller> sellerList = sellerRepository.findAll();
        assertThat(sellerList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(sellerSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchSeller() throws Exception {
        // Initialize the database
        seller = sellerRepository.saveAndFlush(seller);
        sellerSearchRepository.save(seller);

        // Search the seller
        restSellerMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + seller.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(seller.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].shopName").value(hasItem(DEFAULT_SHOP_NAME)))
            .andExpect(jsonPath("$.[*].mail").value(hasItem(DEFAULT_MAIL)))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].tckn").value(hasItem(DEFAULT_TCKN)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].placedDate").value(hasItem(DEFAULT_PLACED_DATE.toString())));
    }
}
