package com.fastshop.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fastshop.IntegrationTest;
import com.fastshop.domain.Product;
import com.fastshop.domain.Property;
import com.fastshop.domain.PropertyDes;
import com.fastshop.repository.PropertyDesRepository;
import com.fastshop.repository.search.PropertyDesSearchRepository;
import com.fastshop.service.criteria.PropertyDesCriteria;
import com.fastshop.service.dto.PropertyDesDTO;
import com.fastshop.service.mapper.PropertyDesMapper;
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
 * Integration tests for the {@link PropertyDesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PropertyDesResourceIT {

    private static final String DEFAULT_DETAIL = "AAAAAAAAAA";
    private static final String UPDATED_DETAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/property-des";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/property-des";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PropertyDesRepository propertyDesRepository;

    @Autowired
    private PropertyDesMapper propertyDesMapper;

    @Autowired
    private PropertyDesSearchRepository propertyDesSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPropertyDesMockMvc;

    private PropertyDes propertyDes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PropertyDes createEntity(EntityManager em) {
        PropertyDes propertyDes = new PropertyDes().detail(DEFAULT_DETAIL);
        // Add required entity
        Property property;
        if (TestUtil.findAll(em, Property.class).isEmpty()) {
            property = PropertyResourceIT.createEntity(em);
            em.persist(property);
            em.flush();
        } else {
            property = TestUtil.findAll(em, Property.class).get(0);
        }
        propertyDes.setProperty(property);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        propertyDes.setProduct(product);
        return propertyDes;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PropertyDes createUpdatedEntity(EntityManager em) {
        PropertyDes propertyDes = new PropertyDes().detail(UPDATED_DETAIL);
        // Add required entity
        Property property;
        if (TestUtil.findAll(em, Property.class).isEmpty()) {
            property = PropertyResourceIT.createUpdatedEntity(em);
            em.persist(property);
            em.flush();
        } else {
            property = TestUtil.findAll(em, Property.class).get(0);
        }
        propertyDes.setProperty(property);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        propertyDes.setProduct(product);
        return propertyDes;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        propertyDesSearchRepository.deleteAll();
        assertThat(propertyDesSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        propertyDes = createEntity(em);
    }

    @Test
    @Transactional
    void createPropertyDes() throws Exception {
        int databaseSizeBeforeCreate = propertyDesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(propertyDesSearchRepository.findAll());
        // Create the PropertyDes
        PropertyDesDTO propertyDesDTO = propertyDesMapper.toDto(propertyDes);
        restPropertyDesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(propertyDesDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PropertyDes in the database
        List<PropertyDes> propertyDesList = propertyDesRepository.findAll();
        assertThat(propertyDesList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(propertyDesSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        PropertyDes testPropertyDes = propertyDesList.get(propertyDesList.size() - 1);
        assertThat(testPropertyDes.getDetail()).isEqualTo(DEFAULT_DETAIL);
    }

    @Test
    @Transactional
    void createPropertyDesWithExistingId() throws Exception {
        // Create the PropertyDes with an existing ID
        propertyDes.setId(1L);
        PropertyDesDTO propertyDesDTO = propertyDesMapper.toDto(propertyDes);

        int databaseSizeBeforeCreate = propertyDesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(propertyDesSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restPropertyDesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(propertyDesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PropertyDes in the database
        List<PropertyDes> propertyDesList = propertyDesRepository.findAll();
        assertThat(propertyDesList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(propertyDesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDetailIsRequired() throws Exception {
        int databaseSizeBeforeTest = propertyDesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(propertyDesSearchRepository.findAll());
        // set the field null
        propertyDes.setDetail(null);

        // Create the PropertyDes, which fails.
        PropertyDesDTO propertyDesDTO = propertyDesMapper.toDto(propertyDes);

        restPropertyDesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(propertyDesDTO))
            )
            .andExpect(status().isBadRequest());

        List<PropertyDes> propertyDesList = propertyDesRepository.findAll();
        assertThat(propertyDesList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(propertyDesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllPropertyDes() throws Exception {
        // Initialize the database
        propertyDesRepository.saveAndFlush(propertyDes);

        // Get all the propertyDesList
        restPropertyDesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(propertyDes.getId().intValue())))
            .andExpect(jsonPath("$.[*].detail").value(hasItem(DEFAULT_DETAIL)));
    }

    @Test
    @Transactional
    void getPropertyDes() throws Exception {
        // Initialize the database
        propertyDesRepository.saveAndFlush(propertyDes);

        // Get the propertyDes
        restPropertyDesMockMvc
            .perform(get(ENTITY_API_URL_ID, propertyDes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(propertyDes.getId().intValue()))
            .andExpect(jsonPath("$.detail").value(DEFAULT_DETAIL));
    }

    @Test
    @Transactional
    void getPropertyDesByIdFiltering() throws Exception {
        // Initialize the database
        propertyDesRepository.saveAndFlush(propertyDes);

        Long id = propertyDes.getId();

        defaultPropertyDesShouldBeFound("id.equals=" + id);
        defaultPropertyDesShouldNotBeFound("id.notEquals=" + id);

        defaultPropertyDesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPropertyDesShouldNotBeFound("id.greaterThan=" + id);

        defaultPropertyDesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPropertyDesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPropertyDesByDetailIsEqualToSomething() throws Exception {
        // Initialize the database
        propertyDesRepository.saveAndFlush(propertyDes);

        // Get all the propertyDesList where detail equals to DEFAULT_DETAIL
        defaultPropertyDesShouldBeFound("detail.equals=" + DEFAULT_DETAIL);

        // Get all the propertyDesList where detail equals to UPDATED_DETAIL
        defaultPropertyDesShouldNotBeFound("detail.equals=" + UPDATED_DETAIL);
    }

    @Test
    @Transactional
    void getAllPropertyDesByDetailIsInShouldWork() throws Exception {
        // Initialize the database
        propertyDesRepository.saveAndFlush(propertyDes);

        // Get all the propertyDesList where detail in DEFAULT_DETAIL or UPDATED_DETAIL
        defaultPropertyDesShouldBeFound("detail.in=" + DEFAULT_DETAIL + "," + UPDATED_DETAIL);

        // Get all the propertyDesList where detail equals to UPDATED_DETAIL
        defaultPropertyDesShouldNotBeFound("detail.in=" + UPDATED_DETAIL);
    }

    @Test
    @Transactional
    void getAllPropertyDesByDetailIsNullOrNotNull() throws Exception {
        // Initialize the database
        propertyDesRepository.saveAndFlush(propertyDes);

        // Get all the propertyDesList where detail is not null
        defaultPropertyDesShouldBeFound("detail.specified=true");

        // Get all the propertyDesList where detail is null
        defaultPropertyDesShouldNotBeFound("detail.specified=false");
    }

    @Test
    @Transactional
    void getAllPropertyDesByDetailContainsSomething() throws Exception {
        // Initialize the database
        propertyDesRepository.saveAndFlush(propertyDes);

        // Get all the propertyDesList where detail contains DEFAULT_DETAIL
        defaultPropertyDesShouldBeFound("detail.contains=" + DEFAULT_DETAIL);

        // Get all the propertyDesList where detail contains UPDATED_DETAIL
        defaultPropertyDesShouldNotBeFound("detail.contains=" + UPDATED_DETAIL);
    }

    @Test
    @Transactional
    void getAllPropertyDesByDetailNotContainsSomething() throws Exception {
        // Initialize the database
        propertyDesRepository.saveAndFlush(propertyDes);

        // Get all the propertyDesList where detail does not contain DEFAULT_DETAIL
        defaultPropertyDesShouldNotBeFound("detail.doesNotContain=" + DEFAULT_DETAIL);

        // Get all the propertyDesList where detail does not contain UPDATED_DETAIL
        defaultPropertyDesShouldBeFound("detail.doesNotContain=" + UPDATED_DETAIL);
    }

    @Test
    @Transactional
    void getAllPropertyDesByPropertyIsEqualToSomething() throws Exception {
        Property property;
        if (TestUtil.findAll(em, Property.class).isEmpty()) {
            propertyDesRepository.saveAndFlush(propertyDes);
            property = PropertyResourceIT.createEntity(em);
        } else {
            property = TestUtil.findAll(em, Property.class).get(0);
        }
        em.persist(property);
        em.flush();
        propertyDes.setProperty(property);
        propertyDesRepository.saveAndFlush(propertyDes);
        Long propertyId = property.getId();

        // Get all the propertyDesList where property equals to propertyId
        defaultPropertyDesShouldBeFound("propertyId.equals=" + propertyId);

        // Get all the propertyDesList where property equals to (propertyId + 1)
        defaultPropertyDesShouldNotBeFound("propertyId.equals=" + (propertyId + 1));
    }

    @Test
    @Transactional
    void getAllPropertyDesByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            propertyDesRepository.saveAndFlush(propertyDes);
            product = ProductResourceIT.createEntity(em);
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(product);
        em.flush();
        propertyDes.setProduct(product);
        propertyDesRepository.saveAndFlush(propertyDes);
        Long productId = product.getId();

        // Get all the propertyDesList where product equals to productId
        defaultPropertyDesShouldBeFound("productId.equals=" + productId);

        // Get all the propertyDesList where product equals to (productId + 1)
        defaultPropertyDesShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPropertyDesShouldBeFound(String filter) throws Exception {
        restPropertyDesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(propertyDes.getId().intValue())))
            .andExpect(jsonPath("$.[*].detail").value(hasItem(DEFAULT_DETAIL)));

        // Check, that the count call also returns 1
        restPropertyDesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPropertyDesShouldNotBeFound(String filter) throws Exception {
        restPropertyDesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPropertyDesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPropertyDes() throws Exception {
        // Get the propertyDes
        restPropertyDesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPropertyDes() throws Exception {
        // Initialize the database
        propertyDesRepository.saveAndFlush(propertyDes);

        int databaseSizeBeforeUpdate = propertyDesRepository.findAll().size();
        propertyDesSearchRepository.save(propertyDes);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(propertyDesSearchRepository.findAll());

        // Update the propertyDes
        PropertyDes updatedPropertyDes = propertyDesRepository.findById(propertyDes.getId()).get();
        // Disconnect from session so that the updates on updatedPropertyDes are not directly saved in db
        em.detach(updatedPropertyDes);
        updatedPropertyDes.detail(UPDATED_DETAIL);
        PropertyDesDTO propertyDesDTO = propertyDesMapper.toDto(updatedPropertyDes);

        restPropertyDesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, propertyDesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(propertyDesDTO))
            )
            .andExpect(status().isOk());

        // Validate the PropertyDes in the database
        List<PropertyDes> propertyDesList = propertyDesRepository.findAll();
        assertThat(propertyDesList).hasSize(databaseSizeBeforeUpdate);
        PropertyDes testPropertyDes = propertyDesList.get(propertyDesList.size() - 1);
        assertThat(testPropertyDes.getDetail()).isEqualTo(UPDATED_DETAIL);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(propertyDesSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<PropertyDes> propertyDesSearchList = IterableUtils.toList(propertyDesSearchRepository.findAll());
                PropertyDes testPropertyDesSearch = propertyDesSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testPropertyDesSearch.getDetail()).isEqualTo(UPDATED_DETAIL);
            });
    }

    @Test
    @Transactional
    void putNonExistingPropertyDes() throws Exception {
        int databaseSizeBeforeUpdate = propertyDesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(propertyDesSearchRepository.findAll());
        propertyDes.setId(count.incrementAndGet());

        // Create the PropertyDes
        PropertyDesDTO propertyDesDTO = propertyDesMapper.toDto(propertyDes);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPropertyDesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, propertyDesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(propertyDesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PropertyDes in the database
        List<PropertyDes> propertyDesList = propertyDesRepository.findAll();
        assertThat(propertyDesList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(propertyDesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchPropertyDes() throws Exception {
        int databaseSizeBeforeUpdate = propertyDesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(propertyDesSearchRepository.findAll());
        propertyDes.setId(count.incrementAndGet());

        // Create the PropertyDes
        PropertyDesDTO propertyDesDTO = propertyDesMapper.toDto(propertyDes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPropertyDesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(propertyDesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PropertyDes in the database
        List<PropertyDes> propertyDesList = propertyDesRepository.findAll();
        assertThat(propertyDesList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(propertyDesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPropertyDes() throws Exception {
        int databaseSizeBeforeUpdate = propertyDesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(propertyDesSearchRepository.findAll());
        propertyDes.setId(count.incrementAndGet());

        // Create the PropertyDes
        PropertyDesDTO propertyDesDTO = propertyDesMapper.toDto(propertyDes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPropertyDesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(propertyDesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PropertyDes in the database
        List<PropertyDes> propertyDesList = propertyDesRepository.findAll();
        assertThat(propertyDesList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(propertyDesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdatePropertyDesWithPatch() throws Exception {
        // Initialize the database
        propertyDesRepository.saveAndFlush(propertyDes);

        int databaseSizeBeforeUpdate = propertyDesRepository.findAll().size();

        // Update the propertyDes using partial update
        PropertyDes partialUpdatedPropertyDes = new PropertyDes();
        partialUpdatedPropertyDes.setId(propertyDes.getId());

        restPropertyDesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPropertyDes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPropertyDes))
            )
            .andExpect(status().isOk());

        // Validate the PropertyDes in the database
        List<PropertyDes> propertyDesList = propertyDesRepository.findAll();
        assertThat(propertyDesList).hasSize(databaseSizeBeforeUpdate);
        PropertyDes testPropertyDes = propertyDesList.get(propertyDesList.size() - 1);
        assertThat(testPropertyDes.getDetail()).isEqualTo(DEFAULT_DETAIL);
    }

    @Test
    @Transactional
    void fullUpdatePropertyDesWithPatch() throws Exception {
        // Initialize the database
        propertyDesRepository.saveAndFlush(propertyDes);

        int databaseSizeBeforeUpdate = propertyDesRepository.findAll().size();

        // Update the propertyDes using partial update
        PropertyDes partialUpdatedPropertyDes = new PropertyDes();
        partialUpdatedPropertyDes.setId(propertyDes.getId());

        partialUpdatedPropertyDes.detail(UPDATED_DETAIL);

        restPropertyDesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPropertyDes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPropertyDes))
            )
            .andExpect(status().isOk());

        // Validate the PropertyDes in the database
        List<PropertyDes> propertyDesList = propertyDesRepository.findAll();
        assertThat(propertyDesList).hasSize(databaseSizeBeforeUpdate);
        PropertyDes testPropertyDes = propertyDesList.get(propertyDesList.size() - 1);
        assertThat(testPropertyDes.getDetail()).isEqualTo(UPDATED_DETAIL);
    }

    @Test
    @Transactional
    void patchNonExistingPropertyDes() throws Exception {
        int databaseSizeBeforeUpdate = propertyDesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(propertyDesSearchRepository.findAll());
        propertyDes.setId(count.incrementAndGet());

        // Create the PropertyDes
        PropertyDesDTO propertyDesDTO = propertyDesMapper.toDto(propertyDes);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPropertyDesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, propertyDesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(propertyDesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PropertyDes in the database
        List<PropertyDes> propertyDesList = propertyDesRepository.findAll();
        assertThat(propertyDesList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(propertyDesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPropertyDes() throws Exception {
        int databaseSizeBeforeUpdate = propertyDesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(propertyDesSearchRepository.findAll());
        propertyDes.setId(count.incrementAndGet());

        // Create the PropertyDes
        PropertyDesDTO propertyDesDTO = propertyDesMapper.toDto(propertyDes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPropertyDesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(propertyDesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PropertyDes in the database
        List<PropertyDes> propertyDesList = propertyDesRepository.findAll();
        assertThat(propertyDesList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(propertyDesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPropertyDes() throws Exception {
        int databaseSizeBeforeUpdate = propertyDesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(propertyDesSearchRepository.findAll());
        propertyDes.setId(count.incrementAndGet());

        // Create the PropertyDes
        PropertyDesDTO propertyDesDTO = propertyDesMapper.toDto(propertyDes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPropertyDesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(propertyDesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PropertyDes in the database
        List<PropertyDes> propertyDesList = propertyDesRepository.findAll();
        assertThat(propertyDesList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(propertyDesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deletePropertyDes() throws Exception {
        // Initialize the database
        propertyDesRepository.saveAndFlush(propertyDes);
        propertyDesRepository.save(propertyDes);
        propertyDesSearchRepository.save(propertyDes);

        int databaseSizeBeforeDelete = propertyDesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(propertyDesSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the propertyDes
        restPropertyDesMockMvc
            .perform(delete(ENTITY_API_URL_ID, propertyDes.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PropertyDes> propertyDesList = propertyDesRepository.findAll();
        assertThat(propertyDesList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(propertyDesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchPropertyDes() throws Exception {
        // Initialize the database
        propertyDes = propertyDesRepository.saveAndFlush(propertyDes);
        propertyDesSearchRepository.save(propertyDes);

        // Search the propertyDes
        restPropertyDesMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + propertyDes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(propertyDes.getId().intValue())))
            .andExpect(jsonPath("$.[*].detail").value(hasItem(DEFAULT_DETAIL)));
    }
}
