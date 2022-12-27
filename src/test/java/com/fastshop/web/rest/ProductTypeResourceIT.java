package com.fastshop.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fastshop.IntegrationTest;
import com.fastshop.domain.Category;
import com.fastshop.domain.ProductType;
import com.fastshop.repository.ProductTypeRepository;
import com.fastshop.repository.search.ProductTypeSearchRepository;
import com.fastshop.service.criteria.ProductTypeCriteria;
import com.fastshop.service.dto.ProductTypeDTO;
import com.fastshop.service.mapper.ProductTypeMapper;
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
 * Integration tests for the {@link ProductTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/product-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Autowired
    private ProductTypeMapper productTypeMapper;

    @Autowired
    private ProductTypeSearchRepository productTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductTypeMockMvc;

    private ProductType productType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductType createEntity(EntityManager em) {
        ProductType productType = new ProductType().name(DEFAULT_NAME);
        return productType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductType createUpdatedEntity(EntityManager em) {
        ProductType productType = new ProductType().name(UPDATED_NAME);
        return productType;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        productTypeSearchRepository.deleteAll();
        assertThat(productTypeSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        productType = createEntity(em);
    }

    @Test
    @Transactional
    void createProductType() throws Exception {
        int databaseSizeBeforeCreate = productTypeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productTypeSearchRepository.findAll());
        // Create the ProductType
        ProductTypeDTO productTypeDTO = productTypeMapper.toDto(productType);
        restProductTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ProductType in the database
        List<ProductType> productTypeList = productTypeRepository.findAll();
        assertThat(productTypeList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(productTypeSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        ProductType testProductType = productTypeList.get(productTypeList.size() - 1);
        assertThat(testProductType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createProductTypeWithExistingId() throws Exception {
        // Create the ProductType with an existing ID
        productType.setId(1L);
        ProductTypeDTO productTypeDTO = productTypeMapper.toDto(productType);

        int databaseSizeBeforeCreate = productTypeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productTypeSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductType in the database
        List<ProductType> productTypeList = productTypeRepository.findAll();
        assertThat(productTypeList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = productTypeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productTypeSearchRepository.findAll());
        // set the field null
        productType.setName(null);

        // Create the ProductType, which fails.
        ProductTypeDTO productTypeDTO = productTypeMapper.toDto(productType);

        restProductTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductType> productTypeList = productTypeRepository.findAll();
        assertThat(productTypeList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllProductTypes() throws Exception {
        // Initialize the database
        productTypeRepository.saveAndFlush(productType);

        // Get all the productTypeList
        restProductTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getProductType() throws Exception {
        // Initialize the database
        productTypeRepository.saveAndFlush(productType);

        // Get the productType
        restProductTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, productType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getProductTypesByIdFiltering() throws Exception {
        // Initialize the database
        productTypeRepository.saveAndFlush(productType);

        Long id = productType.getId();

        defaultProductTypeShouldBeFound("id.equals=" + id);
        defaultProductTypeShouldNotBeFound("id.notEquals=" + id);

        defaultProductTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProductTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultProductTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProductTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        productTypeRepository.saveAndFlush(productType);

        // Get all the productTypeList where name equals to DEFAULT_NAME
        defaultProductTypeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the productTypeList where name equals to UPDATED_NAME
        defaultProductTypeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        productTypeRepository.saveAndFlush(productType);

        // Get all the productTypeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultProductTypeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the productTypeList where name equals to UPDATED_NAME
        defaultProductTypeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        productTypeRepository.saveAndFlush(productType);

        // Get all the productTypeList where name is not null
        defaultProductTypeShouldBeFound("name.specified=true");

        // Get all the productTypeList where name is null
        defaultProductTypeShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllProductTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        productTypeRepository.saveAndFlush(productType);

        // Get all the productTypeList where name contains DEFAULT_NAME
        defaultProductTypeShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the productTypeList where name contains UPDATED_NAME
        defaultProductTypeShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        productTypeRepository.saveAndFlush(productType);

        // Get all the productTypeList where name does not contain DEFAULT_NAME
        defaultProductTypeShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the productTypeList where name does not contain UPDATED_NAME
        defaultProductTypeShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductTypesByCategoryIsEqualToSomething() throws Exception {
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            productTypeRepository.saveAndFlush(productType);
            category = CategoryResourceIT.createEntity(em);
        } else {
            category = TestUtil.findAll(em, Category.class).get(0);
        }
        em.persist(category);
        em.flush();
        productType.addCategory(category);
        productTypeRepository.saveAndFlush(productType);
        Long categoryId = category.getId();

        // Get all the productTypeList where category equals to categoryId
        defaultProductTypeShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the productTypeList where category equals to (categoryId + 1)
        defaultProductTypeShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductTypeShouldBeFound(String filter) throws Exception {
        restProductTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restProductTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductTypeShouldNotBeFound(String filter) throws Exception {
        restProductTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductType() throws Exception {
        // Get the productType
        restProductTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductType() throws Exception {
        // Initialize the database
        productTypeRepository.saveAndFlush(productType);

        int databaseSizeBeforeUpdate = productTypeRepository.findAll().size();
        productTypeSearchRepository.save(productType);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productTypeSearchRepository.findAll());

        // Update the productType
        ProductType updatedProductType = productTypeRepository.findById(productType.getId()).get();
        // Disconnect from session so that the updates on updatedProductType are not directly saved in db
        em.detach(updatedProductType);
        updatedProductType.name(UPDATED_NAME);
        ProductTypeDTO productTypeDTO = productTypeMapper.toDto(updatedProductType);

        restProductTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProductType in the database
        List<ProductType> productTypeList = productTypeRepository.findAll();
        assertThat(productTypeList).hasSize(databaseSizeBeforeUpdate);
        ProductType testProductType = productTypeList.get(productTypeList.size() - 1);
        assertThat(testProductType.getName()).isEqualTo(UPDATED_NAME);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(productTypeSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ProductType> productTypeSearchList = IterableUtils.toList(productTypeSearchRepository.findAll());
                ProductType testProductTypeSearch = productTypeSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testProductTypeSearch.getName()).isEqualTo(UPDATED_NAME);
            });
    }

    @Test
    @Transactional
    void putNonExistingProductType() throws Exception {
        int databaseSizeBeforeUpdate = productTypeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productTypeSearchRepository.findAll());
        productType.setId(count.incrementAndGet());

        // Create the ProductType
        ProductTypeDTO productTypeDTO = productTypeMapper.toDto(productType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductType in the database
        List<ProductType> productTypeList = productTypeRepository.findAll();
        assertThat(productTypeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductType() throws Exception {
        int databaseSizeBeforeUpdate = productTypeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productTypeSearchRepository.findAll());
        productType.setId(count.incrementAndGet());

        // Create the ProductType
        ProductTypeDTO productTypeDTO = productTypeMapper.toDto(productType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductType in the database
        List<ProductType> productTypeList = productTypeRepository.findAll();
        assertThat(productTypeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductType() throws Exception {
        int databaseSizeBeforeUpdate = productTypeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productTypeSearchRepository.findAll());
        productType.setId(count.incrementAndGet());

        // Create the ProductType
        ProductTypeDTO productTypeDTO = productTypeMapper.toDto(productType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductType in the database
        List<ProductType> productTypeList = productTypeRepository.findAll();
        assertThat(productTypeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateProductTypeWithPatch() throws Exception {
        // Initialize the database
        productTypeRepository.saveAndFlush(productType);

        int databaseSizeBeforeUpdate = productTypeRepository.findAll().size();

        // Update the productType using partial update
        ProductType partialUpdatedProductType = new ProductType();
        partialUpdatedProductType.setId(productType.getId());

        partialUpdatedProductType.name(UPDATED_NAME);

        restProductTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductType))
            )
            .andExpect(status().isOk());

        // Validate the ProductType in the database
        List<ProductType> productTypeList = productTypeRepository.findAll();
        assertThat(productTypeList).hasSize(databaseSizeBeforeUpdate);
        ProductType testProductType = productTypeList.get(productTypeList.size() - 1);
        assertThat(testProductType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateProductTypeWithPatch() throws Exception {
        // Initialize the database
        productTypeRepository.saveAndFlush(productType);

        int databaseSizeBeforeUpdate = productTypeRepository.findAll().size();

        // Update the productType using partial update
        ProductType partialUpdatedProductType = new ProductType();
        partialUpdatedProductType.setId(productType.getId());

        partialUpdatedProductType.name(UPDATED_NAME);

        restProductTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductType))
            )
            .andExpect(status().isOk());

        // Validate the ProductType in the database
        List<ProductType> productTypeList = productTypeRepository.findAll();
        assertThat(productTypeList).hasSize(databaseSizeBeforeUpdate);
        ProductType testProductType = productTypeList.get(productTypeList.size() - 1);
        assertThat(testProductType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingProductType() throws Exception {
        int databaseSizeBeforeUpdate = productTypeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productTypeSearchRepository.findAll());
        productType.setId(count.incrementAndGet());

        // Create the ProductType
        ProductTypeDTO productTypeDTO = productTypeMapper.toDto(productType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductType in the database
        List<ProductType> productTypeList = productTypeRepository.findAll();
        assertThat(productTypeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductType() throws Exception {
        int databaseSizeBeforeUpdate = productTypeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productTypeSearchRepository.findAll());
        productType.setId(count.incrementAndGet());

        // Create the ProductType
        ProductTypeDTO productTypeDTO = productTypeMapper.toDto(productType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductType in the database
        List<ProductType> productTypeList = productTypeRepository.findAll();
        assertThat(productTypeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductType() throws Exception {
        int databaseSizeBeforeUpdate = productTypeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productTypeSearchRepository.findAll());
        productType.setId(count.incrementAndGet());

        // Create the ProductType
        ProductTypeDTO productTypeDTO = productTypeMapper.toDto(productType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(productTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductType in the database
        List<ProductType> productTypeList = productTypeRepository.findAll();
        assertThat(productTypeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteProductType() throws Exception {
        // Initialize the database
        productTypeRepository.saveAndFlush(productType);
        productTypeRepository.save(productType);
        productTypeSearchRepository.save(productType);

        int databaseSizeBeforeDelete = productTypeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the productType
        restProductTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, productType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductType> productTypeList = productTypeRepository.findAll();
        assertThat(productTypeList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchProductType() throws Exception {
        // Initialize the database
        productType = productTypeRepository.saveAndFlush(productType);
        productTypeSearchRepository.save(productType);

        // Search the productType
        restProductTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + productType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
}
