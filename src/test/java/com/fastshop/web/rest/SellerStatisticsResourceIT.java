package com.fastshop.web.rest;

import static com.fastshop.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fastshop.IntegrationTest;
import com.fastshop.domain.Seller;
import com.fastshop.domain.SellerStatistics;
import com.fastshop.repository.SellerStatisticsRepository;
import com.fastshop.repository.search.SellerStatisticsSearchRepository;
import com.fastshop.service.criteria.SellerStatisticsCriteria;
import com.fastshop.service.dto.SellerStatisticsDTO;
import com.fastshop.service.mapper.SellerStatisticsMapper;
import java.math.BigDecimal;
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
 * Integration tests for the {@link SellerStatisticsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SellerStatisticsResourceIT {

    private static final Integer DEFAULT_PRODUCT = 1;
    private static final Integer UPDATED_PRODUCT = 2;
    private static final Integer SMALLER_PRODUCT = 1 - 1;

    private static final Integer DEFAULT_TOTAL_ORDER = 1;
    private static final Integer UPDATED_TOTAL_ORDER = 2;
    private static final Integer SMALLER_TOTAL_ORDER = 1 - 1;

    private static final BigDecimal DEFAULT_TOTAL_EARNING = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_EARNING = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_EARNING = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/seller-statistics";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/seller-statistics";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SellerStatisticsRepository sellerStatisticsRepository;

    @Autowired
    private SellerStatisticsMapper sellerStatisticsMapper;

    @Autowired
    private SellerStatisticsSearchRepository sellerStatisticsSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSellerStatisticsMockMvc;

    private SellerStatistics sellerStatistics;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SellerStatistics createEntity(EntityManager em) {
        SellerStatistics sellerStatistics = new SellerStatistics()
            .product(DEFAULT_PRODUCT)
            .totalOrder(DEFAULT_TOTAL_ORDER)
            .totalEarning(DEFAULT_TOTAL_EARNING);
        return sellerStatistics;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SellerStatistics createUpdatedEntity(EntityManager em) {
        SellerStatistics sellerStatistics = new SellerStatistics()
            .product(UPDATED_PRODUCT)
            .totalOrder(UPDATED_TOTAL_ORDER)
            .totalEarning(UPDATED_TOTAL_EARNING);
        return sellerStatistics;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        sellerStatisticsSearchRepository.deleteAll();
        assertThat(sellerStatisticsSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        sellerStatistics = createEntity(em);
    }

    @Test
    @Transactional
    void getAllSellerStatistics() throws Exception {
        // Initialize the database
        sellerStatisticsRepository.saveAndFlush(sellerStatistics);

        // Get all the sellerStatisticsList
        restSellerStatisticsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sellerStatistics.getId().intValue())))
            .andExpect(jsonPath("$.[*].product").value(hasItem(DEFAULT_PRODUCT)))
            .andExpect(jsonPath("$.[*].totalOrder").value(hasItem(DEFAULT_TOTAL_ORDER)))
            .andExpect(jsonPath("$.[*].totalEarning").value(hasItem(sameNumber(DEFAULT_TOTAL_EARNING))));
    }

    @Test
    @Transactional
    void getSellerStatistics() throws Exception {
        // Initialize the database
        sellerStatisticsRepository.saveAndFlush(sellerStatistics);

        // Get the sellerStatistics
        restSellerStatisticsMockMvc
            .perform(get(ENTITY_API_URL_ID, sellerStatistics.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sellerStatistics.getId().intValue()))
            .andExpect(jsonPath("$.product").value(DEFAULT_PRODUCT))
            .andExpect(jsonPath("$.totalOrder").value(DEFAULT_TOTAL_ORDER))
            .andExpect(jsonPath("$.totalEarning").value(sameNumber(DEFAULT_TOTAL_EARNING)));
    }

    @Test
    @Transactional
    void getSellerStatisticsByIdFiltering() throws Exception {
        // Initialize the database
        sellerStatisticsRepository.saveAndFlush(sellerStatistics);

        Long id = sellerStatistics.getId();

        defaultSellerStatisticsShouldBeFound("id.equals=" + id);
        defaultSellerStatisticsShouldNotBeFound("id.notEquals=" + id);

        defaultSellerStatisticsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSellerStatisticsShouldNotBeFound("id.greaterThan=" + id);

        defaultSellerStatisticsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSellerStatisticsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSellerStatisticsByProductIsEqualToSomething() throws Exception {
        // Initialize the database
        sellerStatisticsRepository.saveAndFlush(sellerStatistics);

        // Get all the sellerStatisticsList where product equals to DEFAULT_PRODUCT
        defaultSellerStatisticsShouldBeFound("product.equals=" + DEFAULT_PRODUCT);

        // Get all the sellerStatisticsList where product equals to UPDATED_PRODUCT
        defaultSellerStatisticsShouldNotBeFound("product.equals=" + UPDATED_PRODUCT);
    }

    @Test
    @Transactional
    void getAllSellerStatisticsByProductIsInShouldWork() throws Exception {
        // Initialize the database
        sellerStatisticsRepository.saveAndFlush(sellerStatistics);

        // Get all the sellerStatisticsList where product in DEFAULT_PRODUCT or UPDATED_PRODUCT
        defaultSellerStatisticsShouldBeFound("product.in=" + DEFAULT_PRODUCT + "," + UPDATED_PRODUCT);

        // Get all the sellerStatisticsList where product equals to UPDATED_PRODUCT
        defaultSellerStatisticsShouldNotBeFound("product.in=" + UPDATED_PRODUCT);
    }

    @Test
    @Transactional
    void getAllSellerStatisticsByProductIsNullOrNotNull() throws Exception {
        // Initialize the database
        sellerStatisticsRepository.saveAndFlush(sellerStatistics);

        // Get all the sellerStatisticsList where product is not null
        defaultSellerStatisticsShouldBeFound("product.specified=true");

        // Get all the sellerStatisticsList where product is null
        defaultSellerStatisticsShouldNotBeFound("product.specified=false");
    }

    @Test
    @Transactional
    void getAllSellerStatisticsByProductIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sellerStatisticsRepository.saveAndFlush(sellerStatistics);

        // Get all the sellerStatisticsList where product is greater than or equal to DEFAULT_PRODUCT
        defaultSellerStatisticsShouldBeFound("product.greaterThanOrEqual=" + DEFAULT_PRODUCT);

        // Get all the sellerStatisticsList where product is greater than or equal to UPDATED_PRODUCT
        defaultSellerStatisticsShouldNotBeFound("product.greaterThanOrEqual=" + UPDATED_PRODUCT);
    }

    @Test
    @Transactional
    void getAllSellerStatisticsByProductIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sellerStatisticsRepository.saveAndFlush(sellerStatistics);

        // Get all the sellerStatisticsList where product is less than or equal to DEFAULT_PRODUCT
        defaultSellerStatisticsShouldBeFound("product.lessThanOrEqual=" + DEFAULT_PRODUCT);

        // Get all the sellerStatisticsList where product is less than or equal to SMALLER_PRODUCT
        defaultSellerStatisticsShouldNotBeFound("product.lessThanOrEqual=" + SMALLER_PRODUCT);
    }

    @Test
    @Transactional
    void getAllSellerStatisticsByProductIsLessThanSomething() throws Exception {
        // Initialize the database
        sellerStatisticsRepository.saveAndFlush(sellerStatistics);

        // Get all the sellerStatisticsList where product is less than DEFAULT_PRODUCT
        defaultSellerStatisticsShouldNotBeFound("product.lessThan=" + DEFAULT_PRODUCT);

        // Get all the sellerStatisticsList where product is less than UPDATED_PRODUCT
        defaultSellerStatisticsShouldBeFound("product.lessThan=" + UPDATED_PRODUCT);
    }

    @Test
    @Transactional
    void getAllSellerStatisticsByProductIsGreaterThanSomething() throws Exception {
        // Initialize the database
        sellerStatisticsRepository.saveAndFlush(sellerStatistics);

        // Get all the sellerStatisticsList where product is greater than DEFAULT_PRODUCT
        defaultSellerStatisticsShouldNotBeFound("product.greaterThan=" + DEFAULT_PRODUCT);

        // Get all the sellerStatisticsList where product is greater than SMALLER_PRODUCT
        defaultSellerStatisticsShouldBeFound("product.greaterThan=" + SMALLER_PRODUCT);
    }

    @Test
    @Transactional
    void getAllSellerStatisticsByTotalOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        sellerStatisticsRepository.saveAndFlush(sellerStatistics);

        // Get all the sellerStatisticsList where totalOrder equals to DEFAULT_TOTAL_ORDER
        defaultSellerStatisticsShouldBeFound("totalOrder.equals=" + DEFAULT_TOTAL_ORDER);

        // Get all the sellerStatisticsList where totalOrder equals to UPDATED_TOTAL_ORDER
        defaultSellerStatisticsShouldNotBeFound("totalOrder.equals=" + UPDATED_TOTAL_ORDER);
    }

    @Test
    @Transactional
    void getAllSellerStatisticsByTotalOrderIsInShouldWork() throws Exception {
        // Initialize the database
        sellerStatisticsRepository.saveAndFlush(sellerStatistics);

        // Get all the sellerStatisticsList where totalOrder in DEFAULT_TOTAL_ORDER or UPDATED_TOTAL_ORDER
        defaultSellerStatisticsShouldBeFound("totalOrder.in=" + DEFAULT_TOTAL_ORDER + "," + UPDATED_TOTAL_ORDER);

        // Get all the sellerStatisticsList where totalOrder equals to UPDATED_TOTAL_ORDER
        defaultSellerStatisticsShouldNotBeFound("totalOrder.in=" + UPDATED_TOTAL_ORDER);
    }

    @Test
    @Transactional
    void getAllSellerStatisticsByTotalOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        sellerStatisticsRepository.saveAndFlush(sellerStatistics);

        // Get all the sellerStatisticsList where totalOrder is not null
        defaultSellerStatisticsShouldBeFound("totalOrder.specified=true");

        // Get all the sellerStatisticsList where totalOrder is null
        defaultSellerStatisticsShouldNotBeFound("totalOrder.specified=false");
    }

    @Test
    @Transactional
    void getAllSellerStatisticsByTotalOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sellerStatisticsRepository.saveAndFlush(sellerStatistics);

        // Get all the sellerStatisticsList where totalOrder is greater than or equal to DEFAULT_TOTAL_ORDER
        defaultSellerStatisticsShouldBeFound("totalOrder.greaterThanOrEqual=" + DEFAULT_TOTAL_ORDER);

        // Get all the sellerStatisticsList where totalOrder is greater than or equal to UPDATED_TOTAL_ORDER
        defaultSellerStatisticsShouldNotBeFound("totalOrder.greaterThanOrEqual=" + UPDATED_TOTAL_ORDER);
    }

    @Test
    @Transactional
    void getAllSellerStatisticsByTotalOrderIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sellerStatisticsRepository.saveAndFlush(sellerStatistics);

        // Get all the sellerStatisticsList where totalOrder is less than or equal to DEFAULT_TOTAL_ORDER
        defaultSellerStatisticsShouldBeFound("totalOrder.lessThanOrEqual=" + DEFAULT_TOTAL_ORDER);

        // Get all the sellerStatisticsList where totalOrder is less than or equal to SMALLER_TOTAL_ORDER
        defaultSellerStatisticsShouldNotBeFound("totalOrder.lessThanOrEqual=" + SMALLER_TOTAL_ORDER);
    }

    @Test
    @Transactional
    void getAllSellerStatisticsByTotalOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        sellerStatisticsRepository.saveAndFlush(sellerStatistics);

        // Get all the sellerStatisticsList where totalOrder is less than DEFAULT_TOTAL_ORDER
        defaultSellerStatisticsShouldNotBeFound("totalOrder.lessThan=" + DEFAULT_TOTAL_ORDER);

        // Get all the sellerStatisticsList where totalOrder is less than UPDATED_TOTAL_ORDER
        defaultSellerStatisticsShouldBeFound("totalOrder.lessThan=" + UPDATED_TOTAL_ORDER);
    }

    @Test
    @Transactional
    void getAllSellerStatisticsByTotalOrderIsGreaterThanSomething() throws Exception {
        // Initialize the database
        sellerStatisticsRepository.saveAndFlush(sellerStatistics);

        // Get all the sellerStatisticsList where totalOrder is greater than DEFAULT_TOTAL_ORDER
        defaultSellerStatisticsShouldNotBeFound("totalOrder.greaterThan=" + DEFAULT_TOTAL_ORDER);

        // Get all the sellerStatisticsList where totalOrder is greater than SMALLER_TOTAL_ORDER
        defaultSellerStatisticsShouldBeFound("totalOrder.greaterThan=" + SMALLER_TOTAL_ORDER);
    }

    @Test
    @Transactional
    void getAllSellerStatisticsByTotalEarningIsEqualToSomething() throws Exception {
        // Initialize the database
        sellerStatisticsRepository.saveAndFlush(sellerStatistics);

        // Get all the sellerStatisticsList where totalEarning equals to DEFAULT_TOTAL_EARNING
        defaultSellerStatisticsShouldBeFound("totalEarning.equals=" + DEFAULT_TOTAL_EARNING);

        // Get all the sellerStatisticsList where totalEarning equals to UPDATED_TOTAL_EARNING
        defaultSellerStatisticsShouldNotBeFound("totalEarning.equals=" + UPDATED_TOTAL_EARNING);
    }

    @Test
    @Transactional
    void getAllSellerStatisticsByTotalEarningIsInShouldWork() throws Exception {
        // Initialize the database
        sellerStatisticsRepository.saveAndFlush(sellerStatistics);

        // Get all the sellerStatisticsList where totalEarning in DEFAULT_TOTAL_EARNING or UPDATED_TOTAL_EARNING
        defaultSellerStatisticsShouldBeFound("totalEarning.in=" + DEFAULT_TOTAL_EARNING + "," + UPDATED_TOTAL_EARNING);

        // Get all the sellerStatisticsList where totalEarning equals to UPDATED_TOTAL_EARNING
        defaultSellerStatisticsShouldNotBeFound("totalEarning.in=" + UPDATED_TOTAL_EARNING);
    }

    @Test
    @Transactional
    void getAllSellerStatisticsByTotalEarningIsNullOrNotNull() throws Exception {
        // Initialize the database
        sellerStatisticsRepository.saveAndFlush(sellerStatistics);

        // Get all the sellerStatisticsList where totalEarning is not null
        defaultSellerStatisticsShouldBeFound("totalEarning.specified=true");

        // Get all the sellerStatisticsList where totalEarning is null
        defaultSellerStatisticsShouldNotBeFound("totalEarning.specified=false");
    }

    @Test
    @Transactional
    void getAllSellerStatisticsByTotalEarningIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sellerStatisticsRepository.saveAndFlush(sellerStatistics);

        // Get all the sellerStatisticsList where totalEarning is greater than or equal to DEFAULT_TOTAL_EARNING
        defaultSellerStatisticsShouldBeFound("totalEarning.greaterThanOrEqual=" + DEFAULT_TOTAL_EARNING);

        // Get all the sellerStatisticsList where totalEarning is greater than or equal to UPDATED_TOTAL_EARNING
        defaultSellerStatisticsShouldNotBeFound("totalEarning.greaterThanOrEqual=" + UPDATED_TOTAL_EARNING);
    }

    @Test
    @Transactional
    void getAllSellerStatisticsByTotalEarningIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sellerStatisticsRepository.saveAndFlush(sellerStatistics);

        // Get all the sellerStatisticsList where totalEarning is less than or equal to DEFAULT_TOTAL_EARNING
        defaultSellerStatisticsShouldBeFound("totalEarning.lessThanOrEqual=" + DEFAULT_TOTAL_EARNING);

        // Get all the sellerStatisticsList where totalEarning is less than or equal to SMALLER_TOTAL_EARNING
        defaultSellerStatisticsShouldNotBeFound("totalEarning.lessThanOrEqual=" + SMALLER_TOTAL_EARNING);
    }

    @Test
    @Transactional
    void getAllSellerStatisticsByTotalEarningIsLessThanSomething() throws Exception {
        // Initialize the database
        sellerStatisticsRepository.saveAndFlush(sellerStatistics);

        // Get all the sellerStatisticsList where totalEarning is less than DEFAULT_TOTAL_EARNING
        defaultSellerStatisticsShouldNotBeFound("totalEarning.lessThan=" + DEFAULT_TOTAL_EARNING);

        // Get all the sellerStatisticsList where totalEarning is less than UPDATED_TOTAL_EARNING
        defaultSellerStatisticsShouldBeFound("totalEarning.lessThan=" + UPDATED_TOTAL_EARNING);
    }

    @Test
    @Transactional
    void getAllSellerStatisticsByTotalEarningIsGreaterThanSomething() throws Exception {
        // Initialize the database
        sellerStatisticsRepository.saveAndFlush(sellerStatistics);

        // Get all the sellerStatisticsList where totalEarning is greater than DEFAULT_TOTAL_EARNING
        defaultSellerStatisticsShouldNotBeFound("totalEarning.greaterThan=" + DEFAULT_TOTAL_EARNING);

        // Get all the sellerStatisticsList where totalEarning is greater than SMALLER_TOTAL_EARNING
        defaultSellerStatisticsShouldBeFound("totalEarning.greaterThan=" + SMALLER_TOTAL_EARNING);
    }

    @Test
    @Transactional
    void getAllSellerStatisticsBySellerIsEqualToSomething() throws Exception {
        Seller seller;
        if (TestUtil.findAll(em, Seller.class).isEmpty()) {
            sellerStatisticsRepository.saveAndFlush(sellerStatistics);
            seller = SellerResourceIT.createEntity(em);
        } else {
            seller = TestUtil.findAll(em, Seller.class).get(0);
        }
        em.persist(seller);
        em.flush();
        sellerStatistics.setSeller(seller);
        sellerStatisticsRepository.saveAndFlush(sellerStatistics);
        Long sellerId = seller.getId();

        // Get all the sellerStatisticsList where seller equals to sellerId
        defaultSellerStatisticsShouldBeFound("sellerId.equals=" + sellerId);

        // Get all the sellerStatisticsList where seller equals to (sellerId + 1)
        defaultSellerStatisticsShouldNotBeFound("sellerId.equals=" + (sellerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSellerStatisticsShouldBeFound(String filter) throws Exception {
        restSellerStatisticsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sellerStatistics.getId().intValue())))
            .andExpect(jsonPath("$.[*].product").value(hasItem(DEFAULT_PRODUCT)))
            .andExpect(jsonPath("$.[*].totalOrder").value(hasItem(DEFAULT_TOTAL_ORDER)))
            .andExpect(jsonPath("$.[*].totalEarning").value(hasItem(sameNumber(DEFAULT_TOTAL_EARNING))));

        // Check, that the count call also returns 1
        restSellerStatisticsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSellerStatisticsShouldNotBeFound(String filter) throws Exception {
        restSellerStatisticsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSellerStatisticsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSellerStatistics() throws Exception {
        // Get the sellerStatistics
        restSellerStatisticsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void searchSellerStatistics() throws Exception {
        // Initialize the database
        sellerStatistics = sellerStatisticsRepository.saveAndFlush(sellerStatistics);
        sellerStatisticsSearchRepository.save(sellerStatistics);

        // Search the sellerStatistics
        restSellerStatisticsMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + sellerStatistics.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sellerStatistics.getId().intValue())))
            .andExpect(jsonPath("$.[*].product").value(hasItem(DEFAULT_PRODUCT)))
            .andExpect(jsonPath("$.[*].totalOrder").value(hasItem(DEFAULT_TOTAL_ORDER)))
            .andExpect(jsonPath("$.[*].totalEarning").value(hasItem(sameNumber(DEFAULT_TOTAL_EARNING))));
    }
}
