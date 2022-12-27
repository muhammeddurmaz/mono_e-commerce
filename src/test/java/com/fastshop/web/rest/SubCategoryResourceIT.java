package com.fastshop.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fastshop.IntegrationTest;
import com.fastshop.domain.Category;
import com.fastshop.domain.Product;
import com.fastshop.domain.SubCategory;
import com.fastshop.repository.SubCategoryRepository;
import com.fastshop.repository.search.SubCategorySearchRepository;
import com.fastshop.service.criteria.SubCategoryCriteria;
import com.fastshop.service.dto.SubCategoryDTO;
import com.fastshop.service.mapper.SubCategoryMapper;
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
 * Integration tests for the {@link SubCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SubCategoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/sub-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/sub-categories";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private SubCategoryMapper subCategoryMapper;

    @Autowired
    private SubCategorySearchRepository subCategorySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubCategoryMockMvc;

    private SubCategory subCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubCategory createEntity(EntityManager em) {
        SubCategory subCategory = new SubCategory().name(DEFAULT_NAME).image(DEFAULT_IMAGE).imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        // Add required entity
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            category = CategoryResourceIT.createEntity(em);
            em.persist(category);
            em.flush();
        } else {
            category = TestUtil.findAll(em, Category.class).get(0);
        }
        subCategory.setCategory(category);
        return subCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubCategory createUpdatedEntity(EntityManager em) {
        SubCategory subCategory = new SubCategory().name(UPDATED_NAME).image(UPDATED_IMAGE).imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        // Add required entity
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            category = CategoryResourceIT.createUpdatedEntity(em);
            em.persist(category);
            em.flush();
        } else {
            category = TestUtil.findAll(em, Category.class).get(0);
        }
        subCategory.setCategory(category);
        return subCategory;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        subCategorySearchRepository.deleteAll();
        assertThat(subCategorySearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        subCategory = createEntity(em);
    }

    @Test
    @Transactional
    void createSubCategory() throws Exception {
        int databaseSizeBeforeCreate = subCategoryRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subCategorySearchRepository.findAll());
        // Create the SubCategory
        SubCategoryDTO subCategoryDTO = subCategoryMapper.toDto(subCategory);
        restSubCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subCategoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SubCategory in the database
        List<SubCategory> subCategoryList = subCategoryRepository.findAll();
        assertThat(subCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(subCategorySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        SubCategory testSubCategory = subCategoryList.get(subCategoryList.size() - 1);
        assertThat(testSubCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSubCategory.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testSubCategory.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createSubCategoryWithExistingId() throws Exception {
        // Create the SubCategory with an existing ID
        subCategory.setId(1L);
        SubCategoryDTO subCategoryDTO = subCategoryMapper.toDto(subCategory);

        int databaseSizeBeforeCreate = subCategoryRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subCategorySearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubCategory in the database
        List<SubCategory> subCategoryList = subCategoryRepository.findAll();
        assertThat(subCategoryList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(subCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = subCategoryRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subCategorySearchRepository.findAll());
        // set the field null
        subCategory.setName(null);

        // Create the SubCategory, which fails.
        SubCategoryDTO subCategoryDTO = subCategoryMapper.toDto(subCategory);

        restSubCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<SubCategory> subCategoryList = subCategoryRepository.findAll();
        assertThat(subCategoryList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(subCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllSubCategories() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);

        // Get all the subCategoryList
        restSubCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }

    @Test
    @Transactional
    void getSubCategory() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);

        // Get the subCategory
        restSubCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, subCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    void getSubCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);

        Long id = subCategory.getId();

        defaultSubCategoryShouldBeFound("id.equals=" + id);
        defaultSubCategoryShouldNotBeFound("id.notEquals=" + id);

        defaultSubCategoryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSubCategoryShouldNotBeFound("id.greaterThan=" + id);

        defaultSubCategoryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSubCategoryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSubCategoriesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);

        // Get all the subCategoryList where name equals to DEFAULT_NAME
        defaultSubCategoryShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the subCategoryList where name equals to UPDATED_NAME
        defaultSubCategoryShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSubCategoriesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);

        // Get all the subCategoryList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSubCategoryShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the subCategoryList where name equals to UPDATED_NAME
        defaultSubCategoryShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSubCategoriesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);

        // Get all the subCategoryList where name is not null
        defaultSubCategoryShouldBeFound("name.specified=true");

        // Get all the subCategoryList where name is null
        defaultSubCategoryShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllSubCategoriesByNameContainsSomething() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);

        // Get all the subCategoryList where name contains DEFAULT_NAME
        defaultSubCategoryShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the subCategoryList where name contains UPDATED_NAME
        defaultSubCategoryShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSubCategoriesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);

        // Get all the subCategoryList where name does not contain DEFAULT_NAME
        defaultSubCategoryShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the subCategoryList where name does not contain UPDATED_NAME
        defaultSubCategoryShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSubCategoriesByCategoryIsEqualToSomething() throws Exception {
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            subCategoryRepository.saveAndFlush(subCategory);
            category = CategoryResourceIT.createEntity(em);
        } else {
            category = TestUtil.findAll(em, Category.class).get(0);
        }
        em.persist(category);
        em.flush();
        subCategory.setCategory(category);
        subCategoryRepository.saveAndFlush(subCategory);
        Long categoryId = category.getId();

        // Get all the subCategoryList where category equals to categoryId
        defaultSubCategoryShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the subCategoryList where category equals to (categoryId + 1)
        defaultSubCategoryShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    @Test
    @Transactional
    void getAllSubCategoriesByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            subCategoryRepository.saveAndFlush(subCategory);
            product = ProductResourceIT.createEntity(em);
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(product);
        em.flush();
        subCategory.addProduct(product);
        subCategoryRepository.saveAndFlush(subCategory);
        Long productId = product.getId();

        // Get all the subCategoryList where product equals to productId
        defaultSubCategoryShouldBeFound("productId.equals=" + productId);

        // Get all the subCategoryList where product equals to (productId + 1)
        defaultSubCategoryShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSubCategoryShouldBeFound(String filter) throws Exception {
        restSubCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));

        // Check, that the count call also returns 1
        restSubCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSubCategoryShouldNotBeFound(String filter) throws Exception {
        restSubCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSubCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSubCategory() throws Exception {
        // Get the subCategory
        restSubCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSubCategory() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);

        int databaseSizeBeforeUpdate = subCategoryRepository.findAll().size();
        subCategorySearchRepository.save(subCategory);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subCategorySearchRepository.findAll());

        // Update the subCategory
        SubCategory updatedSubCategory = subCategoryRepository.findById(subCategory.getId()).get();
        // Disconnect from session so that the updates on updatedSubCategory are not directly saved in db
        em.detach(updatedSubCategory);
        updatedSubCategory.name(UPDATED_NAME).image(UPDATED_IMAGE).imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        SubCategoryDTO subCategoryDTO = subCategoryMapper.toDto(updatedSubCategory);

        restSubCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subCategoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the SubCategory in the database
        List<SubCategory> subCategoryList = subCategoryRepository.findAll();
        assertThat(subCategoryList).hasSize(databaseSizeBeforeUpdate);
        SubCategory testSubCategory = subCategoryList.get(subCategoryList.size() - 1);
        assertThat(testSubCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSubCategory.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testSubCategory.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(subCategorySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<SubCategory> subCategorySearchList = IterableUtils.toList(subCategorySearchRepository.findAll());
                SubCategory testSubCategorySearch = subCategorySearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testSubCategorySearch.getName()).isEqualTo(UPDATED_NAME);
                assertThat(testSubCategorySearch.getImage()).isEqualTo(UPDATED_IMAGE);
                assertThat(testSubCategorySearch.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
            });
    }

    @Test
    @Transactional
    void putNonExistingSubCategory() throws Exception {
        int databaseSizeBeforeUpdate = subCategoryRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subCategorySearchRepository.findAll());
        subCategory.setId(count.incrementAndGet());

        // Create the SubCategory
        SubCategoryDTO subCategoryDTO = subCategoryMapper.toDto(subCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubCategory in the database
        List<SubCategory> subCategoryList = subCategoryRepository.findAll();
        assertThat(subCategoryList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(subCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubCategory() throws Exception {
        int databaseSizeBeforeUpdate = subCategoryRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subCategorySearchRepository.findAll());
        subCategory.setId(count.incrementAndGet());

        // Create the SubCategory
        SubCategoryDTO subCategoryDTO = subCategoryMapper.toDto(subCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubCategory in the database
        List<SubCategory> subCategoryList = subCategoryRepository.findAll();
        assertThat(subCategoryList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(subCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubCategory() throws Exception {
        int databaseSizeBeforeUpdate = subCategoryRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subCategorySearchRepository.findAll());
        subCategory.setId(count.incrementAndGet());

        // Create the SubCategory
        SubCategoryDTO subCategoryDTO = subCategoryMapper.toDto(subCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubCategoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subCategoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubCategory in the database
        List<SubCategory> subCategoryList = subCategoryRepository.findAll();
        assertThat(subCategoryList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(subCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateSubCategoryWithPatch() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);

        int databaseSizeBeforeUpdate = subCategoryRepository.findAll().size();

        // Update the subCategory using partial update
        SubCategory partialUpdatedSubCategory = new SubCategory();
        partialUpdatedSubCategory.setId(subCategory.getId());

        partialUpdatedSubCategory.name(UPDATED_NAME);

        restSubCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubCategory))
            )
            .andExpect(status().isOk());

        // Validate the SubCategory in the database
        List<SubCategory> subCategoryList = subCategoryRepository.findAll();
        assertThat(subCategoryList).hasSize(databaseSizeBeforeUpdate);
        SubCategory testSubCategory = subCategoryList.get(subCategoryList.size() - 1);
        assertThat(testSubCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSubCategory.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testSubCategory.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateSubCategoryWithPatch() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);

        int databaseSizeBeforeUpdate = subCategoryRepository.findAll().size();

        // Update the subCategory using partial update
        SubCategory partialUpdatedSubCategory = new SubCategory();
        partialUpdatedSubCategory.setId(subCategory.getId());

        partialUpdatedSubCategory.name(UPDATED_NAME).image(UPDATED_IMAGE).imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restSubCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubCategory))
            )
            .andExpect(status().isOk());

        // Validate the SubCategory in the database
        List<SubCategory> subCategoryList = subCategoryRepository.findAll();
        assertThat(subCategoryList).hasSize(databaseSizeBeforeUpdate);
        SubCategory testSubCategory = subCategoryList.get(subCategoryList.size() - 1);
        assertThat(testSubCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSubCategory.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testSubCategory.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingSubCategory() throws Exception {
        int databaseSizeBeforeUpdate = subCategoryRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subCategorySearchRepository.findAll());
        subCategory.setId(count.incrementAndGet());

        // Create the SubCategory
        SubCategoryDTO subCategoryDTO = subCategoryMapper.toDto(subCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, subCategoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubCategory in the database
        List<SubCategory> subCategoryList = subCategoryRepository.findAll();
        assertThat(subCategoryList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(subCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubCategory() throws Exception {
        int databaseSizeBeforeUpdate = subCategoryRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subCategorySearchRepository.findAll());
        subCategory.setId(count.incrementAndGet());

        // Create the SubCategory
        SubCategoryDTO subCategoryDTO = subCategoryMapper.toDto(subCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubCategory in the database
        List<SubCategory> subCategoryList = subCategoryRepository.findAll();
        assertThat(subCategoryList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(subCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubCategory() throws Exception {
        int databaseSizeBeforeUpdate = subCategoryRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subCategorySearchRepository.findAll());
        subCategory.setId(count.incrementAndGet());

        // Create the SubCategory
        SubCategoryDTO subCategoryDTO = subCategoryMapper.toDto(subCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(subCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubCategory in the database
        List<SubCategory> subCategoryList = subCategoryRepository.findAll();
        assertThat(subCategoryList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(subCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteSubCategory() throws Exception {
        // Initialize the database
        subCategoryRepository.saveAndFlush(subCategory);
        subCategoryRepository.save(subCategory);
        subCategorySearchRepository.save(subCategory);

        int databaseSizeBeforeDelete = subCategoryRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the subCategory
        restSubCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, subCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SubCategory> subCategoryList = subCategoryRepository.findAll();
        assertThat(subCategoryList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(subCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchSubCategory() throws Exception {
        // Initialize the database
        subCategory = subCategoryRepository.saveAndFlush(subCategory);
        subCategorySearchRepository.save(subCategory);

        // Search the subCategory
        restSubCategoryMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + subCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }
}
