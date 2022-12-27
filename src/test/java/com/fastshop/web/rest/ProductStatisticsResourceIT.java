package com.fastshop.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fastshop.IntegrationTest;
import com.fastshop.domain.ProductStatistics;
import com.fastshop.repository.ProductStatisticsRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProductStatisticsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductStatisticsResourceIT {

    private static final Integer DEFAULT_ORDER = 1;
    private static final Integer UPDATED_ORDER = 2;

    private static final Integer DEFAULT_CLICK = 1;
    private static final Integer UPDATED_CLICK = 2;

    private static final Integer DEFAULT_COMMENT = 1;
    private static final Integer UPDATED_COMMENT = 2;

    private static final Float DEFAULT_RATING = 1F;
    private static final Float UPDATED_RATING = 2F;

    private static final Integer DEFAULT_ADD_CART = 1;
    private static final Integer UPDATED_ADD_CART = 2;

    private static final String ENTITY_API_URL = "/api/product-statistics";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductStatisticsRepository productStatisticsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductStatisticsMockMvc;

    private ProductStatistics productStatistics;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductStatistics createEntity(EntityManager em) {
        ProductStatistics productStatistics = new ProductStatistics()
            .order(DEFAULT_ORDER)
            .click(DEFAULT_CLICK)
            .comment(DEFAULT_COMMENT)
            .rating(DEFAULT_RATING)
            .addCart(DEFAULT_ADD_CART);
        return productStatistics;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductStatistics createUpdatedEntity(EntityManager em) {
        ProductStatistics productStatistics = new ProductStatistics()
            .order(UPDATED_ORDER)
            .click(UPDATED_CLICK)
            .comment(UPDATED_COMMENT)
            .rating(UPDATED_RATING)
            .addCart(UPDATED_ADD_CART);
        return productStatistics;
    }

    @BeforeEach
    public void initTest() {
        productStatistics = createEntity(em);
    }

    @Test
    @Transactional
    void getAllProductStatistics() throws Exception {
        // Initialize the database
        productStatisticsRepository.saveAndFlush(productStatistics);

        // Get all the productStatisticsList
        restProductStatisticsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productStatistics.getId().intValue())))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)))
            .andExpect(jsonPath("$.[*].click").value(hasItem(DEFAULT_CLICK)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].addCart").value(hasItem(DEFAULT_ADD_CART)));
    }

    @Test
    @Transactional
    void getProductStatistics() throws Exception {
        // Initialize the database
        productStatisticsRepository.saveAndFlush(productStatistics);

        // Get the productStatistics
        restProductStatisticsMockMvc
            .perform(get(ENTITY_API_URL_ID, productStatistics.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productStatistics.getId().intValue()))
            .andExpect(jsonPath("$.order").value(DEFAULT_ORDER))
            .andExpect(jsonPath("$.click").value(DEFAULT_CLICK))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING.doubleValue()))
            .andExpect(jsonPath("$.addCart").value(DEFAULT_ADD_CART));
    }

    @Test
    @Transactional
    void getNonExistingProductStatistics() throws Exception {
        // Get the productStatistics
        restProductStatisticsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }
}
