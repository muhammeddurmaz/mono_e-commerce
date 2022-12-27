package com.fastshop.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fastshop.IntegrationTest;
import com.fastshop.domain.Discount;
import com.fastshop.repository.DiscountRepository;
import com.fastshop.repository.search.DiscountSearchRepository;
import com.fastshop.service.criteria.DiscountCriteria;
import com.fastshop.service.dto.DiscountDTO;
import com.fastshop.service.mapper.DiscountMapper;
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
 * Integration tests for the {@link DiscountResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DiscountResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/discounts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/discounts";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private DiscountMapper discountMapper;

    @Autowired
    private DiscountSearchRepository discountSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDiscountMockMvc;

    private Discount discount;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Discount createEntity(EntityManager em) {
        Discount discount = new Discount()
            .title(DEFAULT_TITLE)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .description(DEFAULT_DESCRIPTION);
        return discount;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Discount createUpdatedEntity(EntityManager em) {
        Discount discount = new Discount()
            .title(UPDATED_TITLE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .description(UPDATED_DESCRIPTION);
        return discount;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        discountSearchRepository.deleteAll();
        assertThat(discountSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        discount = createEntity(em);
    }

    @Test
    @Transactional
    void createDiscount() throws Exception {
        int databaseSizeBeforeCreate = discountRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(discountSearchRepository.findAll());
        // Create the Discount
        DiscountDTO discountDTO = discountMapper.toDto(discount);
        restDiscountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(discountDTO)))
            .andExpect(status().isCreated());

        // Validate the Discount in the database
        List<Discount> discountList = discountRepository.findAll();
        assertThat(discountList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(discountSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Discount testDiscount = discountList.get(discountList.size() - 1);
        assertThat(testDiscount.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testDiscount.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testDiscount.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testDiscount.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createDiscountWithExistingId() throws Exception {
        // Create the Discount with an existing ID
        discount.setId(1L);
        DiscountDTO discountDTO = discountMapper.toDto(discount);

        int databaseSizeBeforeCreate = discountRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(discountSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restDiscountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(discountDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Discount in the database
        List<Discount> discountList = discountRepository.findAll();
        assertThat(discountList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(discountSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = discountRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(discountSearchRepository.findAll());
        // set the field null
        discount.setTitle(null);

        // Create the Discount, which fails.
        DiscountDTO discountDTO = discountMapper.toDto(discount);

        restDiscountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(discountDTO)))
            .andExpect(status().isBadRequest());

        List<Discount> discountList = discountRepository.findAll();
        assertThat(discountList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(discountSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllDiscounts() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);

        // Get all the discountList
        restDiscountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(discount.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getDiscount() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);

        // Get the discount
        restDiscountMockMvc
            .perform(get(ENTITY_API_URL_ID, discount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(discount.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getDiscountsByIdFiltering() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);

        Long id = discount.getId();

        defaultDiscountShouldBeFound("id.equals=" + id);
        defaultDiscountShouldNotBeFound("id.notEquals=" + id);

        defaultDiscountShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDiscountShouldNotBeFound("id.greaterThan=" + id);

        defaultDiscountShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDiscountShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDiscountsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);

        // Get all the discountList where title equals to DEFAULT_TITLE
        defaultDiscountShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the discountList where title equals to UPDATED_TITLE
        defaultDiscountShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllDiscountsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);

        // Get all the discountList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultDiscountShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the discountList where title equals to UPDATED_TITLE
        defaultDiscountShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllDiscountsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);

        // Get all the discountList where title is not null
        defaultDiscountShouldBeFound("title.specified=true");

        // Get all the discountList where title is null
        defaultDiscountShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllDiscountsByTitleContainsSomething() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);

        // Get all the discountList where title contains DEFAULT_TITLE
        defaultDiscountShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the discountList where title contains UPDATED_TITLE
        defaultDiscountShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllDiscountsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);

        // Get all the discountList where title does not contain DEFAULT_TITLE
        defaultDiscountShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the discountList where title does not contain UPDATED_TITLE
        defaultDiscountShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllDiscountsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);

        // Get all the discountList where description equals to DEFAULT_DESCRIPTION
        defaultDiscountShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the discountList where description equals to UPDATED_DESCRIPTION
        defaultDiscountShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDiscountsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);

        // Get all the discountList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultDiscountShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the discountList where description equals to UPDATED_DESCRIPTION
        defaultDiscountShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDiscountsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);

        // Get all the discountList where description is not null
        defaultDiscountShouldBeFound("description.specified=true");

        // Get all the discountList where description is null
        defaultDiscountShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllDiscountsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);

        // Get all the discountList where description contains DEFAULT_DESCRIPTION
        defaultDiscountShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the discountList where description contains UPDATED_DESCRIPTION
        defaultDiscountShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDiscountsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);

        // Get all the discountList where description does not contain DEFAULT_DESCRIPTION
        defaultDiscountShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the discountList where description does not contain UPDATED_DESCRIPTION
        defaultDiscountShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDiscountShouldBeFound(String filter) throws Exception {
        restDiscountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(discount.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restDiscountMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDiscountShouldNotBeFound(String filter) throws Exception {
        restDiscountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDiscountMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDiscount() throws Exception {
        // Get the discount
        restDiscountMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDiscount() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);

        int databaseSizeBeforeUpdate = discountRepository.findAll().size();
        discountSearchRepository.save(discount);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(discountSearchRepository.findAll());

        // Update the discount
        Discount updatedDiscount = discountRepository.findById(discount.getId()).get();
        // Disconnect from session so that the updates on updatedDiscount are not directly saved in db
        em.detach(updatedDiscount);
        updatedDiscount
            .title(UPDATED_TITLE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .description(UPDATED_DESCRIPTION);
        DiscountDTO discountDTO = discountMapper.toDto(updatedDiscount);

        restDiscountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, discountDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(discountDTO))
            )
            .andExpect(status().isOk());

        // Validate the Discount in the database
        List<Discount> discountList = discountRepository.findAll();
        assertThat(discountList).hasSize(databaseSizeBeforeUpdate);
        Discount testDiscount = discountList.get(discountList.size() - 1);
        assertThat(testDiscount.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testDiscount.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testDiscount.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testDiscount.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(discountSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Discount> discountSearchList = IterableUtils.toList(discountSearchRepository.findAll());
                Discount testDiscountSearch = discountSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testDiscountSearch.getTitle()).isEqualTo(UPDATED_TITLE);
                assertThat(testDiscountSearch.getImage()).isEqualTo(UPDATED_IMAGE);
                assertThat(testDiscountSearch.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
                assertThat(testDiscountSearch.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
            });
    }

    @Test
    @Transactional
    void putNonExistingDiscount() throws Exception {
        int databaseSizeBeforeUpdate = discountRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(discountSearchRepository.findAll());
        discount.setId(count.incrementAndGet());

        // Create the Discount
        DiscountDTO discountDTO = discountMapper.toDto(discount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDiscountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, discountDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(discountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Discount in the database
        List<Discount> discountList = discountRepository.findAll();
        assertThat(discountList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(discountSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchDiscount() throws Exception {
        int databaseSizeBeforeUpdate = discountRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(discountSearchRepository.findAll());
        discount.setId(count.incrementAndGet());

        // Create the Discount
        DiscountDTO discountDTO = discountMapper.toDto(discount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiscountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(discountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Discount in the database
        List<Discount> discountList = discountRepository.findAll();
        assertThat(discountList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(discountSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDiscount() throws Exception {
        int databaseSizeBeforeUpdate = discountRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(discountSearchRepository.findAll());
        discount.setId(count.incrementAndGet());

        // Create the Discount
        DiscountDTO discountDTO = discountMapper.toDto(discount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiscountMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(discountDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Discount in the database
        List<Discount> discountList = discountRepository.findAll();
        assertThat(discountList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(discountSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateDiscountWithPatch() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);

        int databaseSizeBeforeUpdate = discountRepository.findAll().size();

        // Update the discount using partial update
        Discount partialUpdatedDiscount = new Discount();
        partialUpdatedDiscount.setId(discount.getId());

        partialUpdatedDiscount.title(UPDATED_TITLE);

        restDiscountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDiscount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDiscount))
            )
            .andExpect(status().isOk());

        // Validate the Discount in the database
        List<Discount> discountList = discountRepository.findAll();
        assertThat(discountList).hasSize(databaseSizeBeforeUpdate);
        Discount testDiscount = discountList.get(discountList.size() - 1);
        assertThat(testDiscount.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testDiscount.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testDiscount.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testDiscount.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateDiscountWithPatch() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);

        int databaseSizeBeforeUpdate = discountRepository.findAll().size();

        // Update the discount using partial update
        Discount partialUpdatedDiscount = new Discount();
        partialUpdatedDiscount.setId(discount.getId());

        partialUpdatedDiscount
            .title(UPDATED_TITLE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .description(UPDATED_DESCRIPTION);

        restDiscountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDiscount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDiscount))
            )
            .andExpect(status().isOk());

        // Validate the Discount in the database
        List<Discount> discountList = discountRepository.findAll();
        assertThat(discountList).hasSize(databaseSizeBeforeUpdate);
        Discount testDiscount = discountList.get(discountList.size() - 1);
        assertThat(testDiscount.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testDiscount.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testDiscount.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testDiscount.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingDiscount() throws Exception {
        int databaseSizeBeforeUpdate = discountRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(discountSearchRepository.findAll());
        discount.setId(count.incrementAndGet());

        // Create the Discount
        DiscountDTO discountDTO = discountMapper.toDto(discount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDiscountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, discountDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(discountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Discount in the database
        List<Discount> discountList = discountRepository.findAll();
        assertThat(discountList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(discountSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDiscount() throws Exception {
        int databaseSizeBeforeUpdate = discountRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(discountSearchRepository.findAll());
        discount.setId(count.incrementAndGet());

        // Create the Discount
        DiscountDTO discountDTO = discountMapper.toDto(discount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiscountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(discountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Discount in the database
        List<Discount> discountList = discountRepository.findAll();
        assertThat(discountList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(discountSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDiscount() throws Exception {
        int databaseSizeBeforeUpdate = discountRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(discountSearchRepository.findAll());
        discount.setId(count.incrementAndGet());

        // Create the Discount
        DiscountDTO discountDTO = discountMapper.toDto(discount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiscountMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(discountDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Discount in the database
        List<Discount> discountList = discountRepository.findAll();
        assertThat(discountList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(discountSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteDiscount() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);
        discountRepository.save(discount);
        discountSearchRepository.save(discount);

        int databaseSizeBeforeDelete = discountRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(discountSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the discount
        restDiscountMockMvc
            .perform(delete(ENTITY_API_URL_ID, discount.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Discount> discountList = discountRepository.findAll();
        assertThat(discountList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(discountSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchDiscount() throws Exception {
        // Initialize the database
        discount = discountRepository.saveAndFlush(discount);
        discountSearchRepository.save(discount);

        // Search the discount
        restDiscountMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + discount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(discount.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
}
