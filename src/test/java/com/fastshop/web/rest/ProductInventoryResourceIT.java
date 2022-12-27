package com.fastshop.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fastshop.IntegrationTest;
import com.fastshop.domain.Product;
import com.fastshop.domain.ProductInventory;
import com.fastshop.repository.ProductInventoryRepository;
import com.fastshop.service.criteria.ProductInventoryCriteria;
import com.fastshop.service.dto.ProductInventoryDTO;
import com.fastshop.service.mapper.ProductInventoryMapper;
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
 * Integration tests for the {@link ProductInventoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductInventoryResourceIT {

    private static final Integer DEFAULT_TOTAL = 0;
    private static final Integer UPDATED_TOTAL = 1;
    private static final Integer SMALLER_TOTAL = 0 - 1;

    private static final String ENTITY_API_URL = "/api/product-inventories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductInventoryRepository productInventoryRepository;

    @Autowired
    private ProductInventoryMapper productInventoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductInventoryMockMvc;

    private ProductInventory productInventory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductInventory createEntity(EntityManager em) {
        ProductInventory productInventory = new ProductInventory().total(DEFAULT_TOTAL);
        return productInventory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductInventory createUpdatedEntity(EntityManager em) {
        ProductInventory productInventory = new ProductInventory().total(UPDATED_TOTAL);
        return productInventory;
    }

    @BeforeEach
    public void initTest() {
        productInventory = createEntity(em);
    }

    @Test
    @Transactional
    void createProductInventory() throws Exception {
        int databaseSizeBeforeCreate = productInventoryRepository.findAll().size();
        // Create the ProductInventory
        ProductInventoryDTO productInventoryDTO = productInventoryMapper.toDto(productInventory);
        restProductInventoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productInventoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ProductInventory in the database
        List<ProductInventory> productInventoryList = productInventoryRepository.findAll();
        assertThat(productInventoryList).hasSize(databaseSizeBeforeCreate + 1);
        ProductInventory testProductInventory = productInventoryList.get(productInventoryList.size() - 1);
        assertThat(testProductInventory.getTotal()).isEqualTo(DEFAULT_TOTAL);
    }

    @Test
    @Transactional
    void createProductInventoryWithExistingId() throws Exception {
        // Create the ProductInventory with an existing ID
        productInventory.setId(1L);
        ProductInventoryDTO productInventoryDTO = productInventoryMapper.toDto(productInventory);

        int databaseSizeBeforeCreate = productInventoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductInventoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productInventoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductInventory in the database
        List<ProductInventory> productInventoryList = productInventoryRepository.findAll();
        assertThat(productInventoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = productInventoryRepository.findAll().size();
        // set the field null
        productInventory.setTotal(null);

        // Create the ProductInventory, which fails.
        ProductInventoryDTO productInventoryDTO = productInventoryMapper.toDto(productInventory);

        restProductInventoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productInventoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductInventory> productInventoryList = productInventoryRepository.findAll();
        assertThat(productInventoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductInventories() throws Exception {
        // Initialize the database
        productInventoryRepository.saveAndFlush(productInventory);

        // Get all the productInventoryList
        restProductInventoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productInventory.getId().intValue())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL)));
    }

    @Test
    @Transactional
    void getProductInventory() throws Exception {
        // Initialize the database
        productInventoryRepository.saveAndFlush(productInventory);

        // Get the productInventory
        restProductInventoryMockMvc
            .perform(get(ENTITY_API_URL_ID, productInventory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productInventory.getId().intValue()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL));
    }

    @Test
    @Transactional
    void getProductInventoriesByIdFiltering() throws Exception {
        // Initialize the database
        productInventoryRepository.saveAndFlush(productInventory);

        Long id = productInventory.getId();

        defaultProductInventoryShouldBeFound("id.equals=" + id);
        defaultProductInventoryShouldNotBeFound("id.notEquals=" + id);

        defaultProductInventoryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProductInventoryShouldNotBeFound("id.greaterThan=" + id);

        defaultProductInventoryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProductInventoryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductInventoriesByTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        productInventoryRepository.saveAndFlush(productInventory);

        // Get all the productInventoryList where total equals to DEFAULT_TOTAL
        defaultProductInventoryShouldBeFound("total.equals=" + DEFAULT_TOTAL);

        // Get all the productInventoryList where total equals to UPDATED_TOTAL
        defaultProductInventoryShouldNotBeFound("total.equals=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllProductInventoriesByTotalIsInShouldWork() throws Exception {
        // Initialize the database
        productInventoryRepository.saveAndFlush(productInventory);

        // Get all the productInventoryList where total in DEFAULT_TOTAL or UPDATED_TOTAL
        defaultProductInventoryShouldBeFound("total.in=" + DEFAULT_TOTAL + "," + UPDATED_TOTAL);

        // Get all the productInventoryList where total equals to UPDATED_TOTAL
        defaultProductInventoryShouldNotBeFound("total.in=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllProductInventoriesByTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        productInventoryRepository.saveAndFlush(productInventory);

        // Get all the productInventoryList where total is not null
        defaultProductInventoryShouldBeFound("total.specified=true");

        // Get all the productInventoryList where total is null
        defaultProductInventoryShouldNotBeFound("total.specified=false");
    }

    @Test
    @Transactional
    void getAllProductInventoriesByTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productInventoryRepository.saveAndFlush(productInventory);

        // Get all the productInventoryList where total is greater than or equal to DEFAULT_TOTAL
        defaultProductInventoryShouldBeFound("total.greaterThanOrEqual=" + DEFAULT_TOTAL);

        // Get all the productInventoryList where total is greater than or equal to UPDATED_TOTAL
        defaultProductInventoryShouldNotBeFound("total.greaterThanOrEqual=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllProductInventoriesByTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productInventoryRepository.saveAndFlush(productInventory);

        // Get all the productInventoryList where total is less than or equal to DEFAULT_TOTAL
        defaultProductInventoryShouldBeFound("total.lessThanOrEqual=" + DEFAULT_TOTAL);

        // Get all the productInventoryList where total is less than or equal to SMALLER_TOTAL
        defaultProductInventoryShouldNotBeFound("total.lessThanOrEqual=" + SMALLER_TOTAL);
    }

    @Test
    @Transactional
    void getAllProductInventoriesByTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        productInventoryRepository.saveAndFlush(productInventory);

        // Get all the productInventoryList where total is less than DEFAULT_TOTAL
        defaultProductInventoryShouldNotBeFound("total.lessThan=" + DEFAULT_TOTAL);

        // Get all the productInventoryList where total is less than UPDATED_TOTAL
        defaultProductInventoryShouldBeFound("total.lessThan=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllProductInventoriesByTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productInventoryRepository.saveAndFlush(productInventory);

        // Get all the productInventoryList where total is greater than DEFAULT_TOTAL
        defaultProductInventoryShouldNotBeFound("total.greaterThan=" + DEFAULT_TOTAL);

        // Get all the productInventoryList where total is greater than SMALLER_TOTAL
        defaultProductInventoryShouldBeFound("total.greaterThan=" + SMALLER_TOTAL);
    }

    @Test
    @Transactional
    void getAllProductInventoriesByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            productInventoryRepository.saveAndFlush(productInventory);
            product = ProductResourceIT.createEntity(em);
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(product);
        em.flush();
        productInventory.setProduct(product);
        productInventoryRepository.saveAndFlush(productInventory);
        Long productId = product.getId();

        // Get all the productInventoryList where product equals to productId
        defaultProductInventoryShouldBeFound("productId.equals=" + productId);

        // Get all the productInventoryList where product equals to (productId + 1)
        defaultProductInventoryShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductInventoryShouldBeFound(String filter) throws Exception {
        restProductInventoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productInventory.getId().intValue())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL)));

        // Check, that the count call also returns 1
        restProductInventoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductInventoryShouldNotBeFound(String filter) throws Exception {
        restProductInventoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductInventoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductInventory() throws Exception {
        // Get the productInventory
        restProductInventoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductInventory() throws Exception {
        // Initialize the database
        productInventoryRepository.saveAndFlush(productInventory);

        int databaseSizeBeforeUpdate = productInventoryRepository.findAll().size();

        // Update the productInventory
        ProductInventory updatedProductInventory = productInventoryRepository.findById(productInventory.getId()).get();
        // Disconnect from session so that the updates on updatedProductInventory are not directly saved in db
        em.detach(updatedProductInventory);
        updatedProductInventory.total(UPDATED_TOTAL);
        ProductInventoryDTO productInventoryDTO = productInventoryMapper.toDto(updatedProductInventory);

        restProductInventoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productInventoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productInventoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProductInventory in the database
        List<ProductInventory> productInventoryList = productInventoryRepository.findAll();
        assertThat(productInventoryList).hasSize(databaseSizeBeforeUpdate);
        ProductInventory testProductInventory = productInventoryList.get(productInventoryList.size() - 1);
        assertThat(testProductInventory.getTotal()).isEqualTo(UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void putNonExistingProductInventory() throws Exception {
        int databaseSizeBeforeUpdate = productInventoryRepository.findAll().size();
        productInventory.setId(count.incrementAndGet());

        // Create the ProductInventory
        ProductInventoryDTO productInventoryDTO = productInventoryMapper.toDto(productInventory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductInventoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productInventoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productInventoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductInventory in the database
        List<ProductInventory> productInventoryList = productInventoryRepository.findAll();
        assertThat(productInventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductInventory() throws Exception {
        int databaseSizeBeforeUpdate = productInventoryRepository.findAll().size();
        productInventory.setId(count.incrementAndGet());

        // Create the ProductInventory
        ProductInventoryDTO productInventoryDTO = productInventoryMapper.toDto(productInventory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductInventoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productInventoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductInventory in the database
        List<ProductInventory> productInventoryList = productInventoryRepository.findAll();
        assertThat(productInventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductInventory() throws Exception {
        int databaseSizeBeforeUpdate = productInventoryRepository.findAll().size();
        productInventory.setId(count.incrementAndGet());

        // Create the ProductInventory
        ProductInventoryDTO productInventoryDTO = productInventoryMapper.toDto(productInventory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductInventoryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productInventoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductInventory in the database
        List<ProductInventory> productInventoryList = productInventoryRepository.findAll();
        assertThat(productInventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductInventoryWithPatch() throws Exception {
        // Initialize the database
        productInventoryRepository.saveAndFlush(productInventory);

        int databaseSizeBeforeUpdate = productInventoryRepository.findAll().size();

        // Update the productInventory using partial update
        ProductInventory partialUpdatedProductInventory = new ProductInventory();
        partialUpdatedProductInventory.setId(productInventory.getId());

        partialUpdatedProductInventory.total(UPDATED_TOTAL);

        restProductInventoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductInventory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductInventory))
            )
            .andExpect(status().isOk());

        // Validate the ProductInventory in the database
        List<ProductInventory> productInventoryList = productInventoryRepository.findAll();
        assertThat(productInventoryList).hasSize(databaseSizeBeforeUpdate);
        ProductInventory testProductInventory = productInventoryList.get(productInventoryList.size() - 1);
        assertThat(testProductInventory.getTotal()).isEqualTo(UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void fullUpdateProductInventoryWithPatch() throws Exception {
        // Initialize the database
        productInventoryRepository.saveAndFlush(productInventory);

        int databaseSizeBeforeUpdate = productInventoryRepository.findAll().size();

        // Update the productInventory using partial update
        ProductInventory partialUpdatedProductInventory = new ProductInventory();
        partialUpdatedProductInventory.setId(productInventory.getId());

        partialUpdatedProductInventory.total(UPDATED_TOTAL);

        restProductInventoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductInventory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductInventory))
            )
            .andExpect(status().isOk());

        // Validate the ProductInventory in the database
        List<ProductInventory> productInventoryList = productInventoryRepository.findAll();
        assertThat(productInventoryList).hasSize(databaseSizeBeforeUpdate);
        ProductInventory testProductInventory = productInventoryList.get(productInventoryList.size() - 1);
        assertThat(testProductInventory.getTotal()).isEqualTo(UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void patchNonExistingProductInventory() throws Exception {
        int databaseSizeBeforeUpdate = productInventoryRepository.findAll().size();
        productInventory.setId(count.incrementAndGet());

        // Create the ProductInventory
        ProductInventoryDTO productInventoryDTO = productInventoryMapper.toDto(productInventory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductInventoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productInventoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productInventoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductInventory in the database
        List<ProductInventory> productInventoryList = productInventoryRepository.findAll();
        assertThat(productInventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductInventory() throws Exception {
        int databaseSizeBeforeUpdate = productInventoryRepository.findAll().size();
        productInventory.setId(count.incrementAndGet());

        // Create the ProductInventory
        ProductInventoryDTO productInventoryDTO = productInventoryMapper.toDto(productInventory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductInventoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productInventoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductInventory in the database
        List<ProductInventory> productInventoryList = productInventoryRepository.findAll();
        assertThat(productInventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductInventory() throws Exception {
        int databaseSizeBeforeUpdate = productInventoryRepository.findAll().size();
        productInventory.setId(count.incrementAndGet());

        // Create the ProductInventory
        ProductInventoryDTO productInventoryDTO = productInventoryMapper.toDto(productInventory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductInventoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productInventoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductInventory in the database
        List<ProductInventory> productInventoryList = productInventoryRepository.findAll();
        assertThat(productInventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductInventory() throws Exception {
        // Initialize the database
        productInventoryRepository.saveAndFlush(productInventory);

        int databaseSizeBeforeDelete = productInventoryRepository.findAll().size();

        // Delete the productInventory
        restProductInventoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, productInventory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductInventory> productInventoryList = productInventoryRepository.findAll();
        assertThat(productInventoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
