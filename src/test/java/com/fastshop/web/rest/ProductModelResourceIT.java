package com.fastshop.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fastshop.IntegrationTest;
import com.fastshop.domain.Product;
import com.fastshop.domain.ProductModel;
import com.fastshop.repository.ProductModelRepository;
import com.fastshop.service.criteria.ProductModelCriteria;
import com.fastshop.service.dto.ProductModelDTO;
import com.fastshop.service.mapper.ProductModelMapper;
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
 * Integration tests for the {@link ProductModelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductModelResourceIT {

    private static final String DEFAULT_MODEL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_MODEL_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-models";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductModelRepository productModelRepository;

    @Autowired
    private ProductModelMapper productModelMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductModelMockMvc;

    private ProductModel productModel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductModel createEntity(EntityManager em) {
        ProductModel productModel = new ProductModel().modelCode(DEFAULT_MODEL_CODE);
        return productModel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductModel createUpdatedEntity(EntityManager em) {
        ProductModel productModel = new ProductModel().modelCode(UPDATED_MODEL_CODE);
        return productModel;
    }

    @BeforeEach
    public void initTest() {
        productModel = createEntity(em);
    }

    @Test
    @Transactional
    void createProductModel() throws Exception {
        int databaseSizeBeforeCreate = productModelRepository.findAll().size();
        // Create the ProductModel
        ProductModelDTO productModelDTO = productModelMapper.toDto(productModel);
        restProductModelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productModelDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ProductModel in the database
        List<ProductModel> productModelList = productModelRepository.findAll();
        assertThat(productModelList).hasSize(databaseSizeBeforeCreate + 1);
        ProductModel testProductModel = productModelList.get(productModelList.size() - 1);
        assertThat(testProductModel.getModelCode()).isEqualTo(DEFAULT_MODEL_CODE);
    }

    @Test
    @Transactional
    void createProductModelWithExistingId() throws Exception {
        // Create the ProductModel with an existing ID
        productModel.setId(1L);
        ProductModelDTO productModelDTO = productModelMapper.toDto(productModel);

        int databaseSizeBeforeCreate = productModelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductModelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductModel in the database
        List<ProductModel> productModelList = productModelRepository.findAll();
        assertThat(productModelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProductModels() throws Exception {
        // Initialize the database
        productModelRepository.saveAndFlush(productModel);

        // Get all the productModelList
        restProductModelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productModel.getId().intValue())))
            .andExpect(jsonPath("$.[*].modelCode").value(hasItem(DEFAULT_MODEL_CODE)));
    }

    @Test
    @Transactional
    void getProductModel() throws Exception {
        // Initialize the database
        productModelRepository.saveAndFlush(productModel);

        // Get the productModel
        restProductModelMockMvc
            .perform(get(ENTITY_API_URL_ID, productModel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productModel.getId().intValue()))
            .andExpect(jsonPath("$.modelCode").value(DEFAULT_MODEL_CODE));
    }

    @Test
    @Transactional
    void getProductModelsByIdFiltering() throws Exception {
        // Initialize the database
        productModelRepository.saveAndFlush(productModel);

        Long id = productModel.getId();

        defaultProductModelShouldBeFound("id.equals=" + id);
        defaultProductModelShouldNotBeFound("id.notEquals=" + id);

        defaultProductModelShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProductModelShouldNotBeFound("id.greaterThan=" + id);

        defaultProductModelShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProductModelShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductModelsByModelCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        productModelRepository.saveAndFlush(productModel);

        // Get all the productModelList where modelCode equals to DEFAULT_MODEL_CODE
        defaultProductModelShouldBeFound("modelCode.equals=" + DEFAULT_MODEL_CODE);

        // Get all the productModelList where modelCode equals to UPDATED_MODEL_CODE
        defaultProductModelShouldNotBeFound("modelCode.equals=" + UPDATED_MODEL_CODE);
    }

    @Test
    @Transactional
    void getAllProductModelsByModelCodeIsInShouldWork() throws Exception {
        // Initialize the database
        productModelRepository.saveAndFlush(productModel);

        // Get all the productModelList where modelCode in DEFAULT_MODEL_CODE or UPDATED_MODEL_CODE
        defaultProductModelShouldBeFound("modelCode.in=" + DEFAULT_MODEL_CODE + "," + UPDATED_MODEL_CODE);

        // Get all the productModelList where modelCode equals to UPDATED_MODEL_CODE
        defaultProductModelShouldNotBeFound("modelCode.in=" + UPDATED_MODEL_CODE);
    }

    @Test
    @Transactional
    void getAllProductModelsByModelCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        productModelRepository.saveAndFlush(productModel);

        // Get all the productModelList where modelCode is not null
        defaultProductModelShouldBeFound("modelCode.specified=true");

        // Get all the productModelList where modelCode is null
        defaultProductModelShouldNotBeFound("modelCode.specified=false");
    }

    @Test
    @Transactional
    void getAllProductModelsByModelCodeContainsSomething() throws Exception {
        // Initialize the database
        productModelRepository.saveAndFlush(productModel);

        // Get all the productModelList where modelCode contains DEFAULT_MODEL_CODE
        defaultProductModelShouldBeFound("modelCode.contains=" + DEFAULT_MODEL_CODE);

        // Get all the productModelList where modelCode contains UPDATED_MODEL_CODE
        defaultProductModelShouldNotBeFound("modelCode.contains=" + UPDATED_MODEL_CODE);
    }

    @Test
    @Transactional
    void getAllProductModelsByModelCodeNotContainsSomething() throws Exception {
        // Initialize the database
        productModelRepository.saveAndFlush(productModel);

        // Get all the productModelList where modelCode does not contain DEFAULT_MODEL_CODE
        defaultProductModelShouldNotBeFound("modelCode.doesNotContain=" + DEFAULT_MODEL_CODE);

        // Get all the productModelList where modelCode does not contain UPDATED_MODEL_CODE
        defaultProductModelShouldBeFound("modelCode.doesNotContain=" + UPDATED_MODEL_CODE);
    }

    @Test
    @Transactional
    void getAllProductModelsByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            productModelRepository.saveAndFlush(productModel);
            product = ProductResourceIT.createEntity(em);
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(product);
        em.flush();
        productModel.addProduct(product);
        productModelRepository.saveAndFlush(productModel);
        Long productId = product.getId();

        // Get all the productModelList where product equals to productId
        defaultProductModelShouldBeFound("productId.equals=" + productId);

        // Get all the productModelList where product equals to (productId + 1)
        defaultProductModelShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductModelShouldBeFound(String filter) throws Exception {
        restProductModelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productModel.getId().intValue())))
            .andExpect(jsonPath("$.[*].modelCode").value(hasItem(DEFAULT_MODEL_CODE)));

        // Check, that the count call also returns 1
        restProductModelMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductModelShouldNotBeFound(String filter) throws Exception {
        restProductModelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductModelMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductModel() throws Exception {
        // Get the productModel
        restProductModelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductModel() throws Exception {
        // Initialize the database
        productModelRepository.saveAndFlush(productModel);

        int databaseSizeBeforeUpdate = productModelRepository.findAll().size();

        // Update the productModel
        ProductModel updatedProductModel = productModelRepository.findById(productModel.getId()).get();
        // Disconnect from session so that the updates on updatedProductModel are not directly saved in db
        em.detach(updatedProductModel);
        updatedProductModel.modelCode(UPDATED_MODEL_CODE);
        ProductModelDTO productModelDTO = productModelMapper.toDto(updatedProductModel);

        restProductModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productModelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productModelDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProductModel in the database
        List<ProductModel> productModelList = productModelRepository.findAll();
        assertThat(productModelList).hasSize(databaseSizeBeforeUpdate);
        ProductModel testProductModel = productModelList.get(productModelList.size() - 1);
        assertThat(testProductModel.getModelCode()).isEqualTo(UPDATED_MODEL_CODE);
    }

    @Test
    @Transactional
    void putNonExistingProductModel() throws Exception {
        int databaseSizeBeforeUpdate = productModelRepository.findAll().size();
        productModel.setId(count.incrementAndGet());

        // Create the ProductModel
        ProductModelDTO productModelDTO = productModelMapper.toDto(productModel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productModelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductModel in the database
        List<ProductModel> productModelList = productModelRepository.findAll();
        assertThat(productModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductModel() throws Exception {
        int databaseSizeBeforeUpdate = productModelRepository.findAll().size();
        productModel.setId(count.incrementAndGet());

        // Create the ProductModel
        ProductModelDTO productModelDTO = productModelMapper.toDto(productModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductModel in the database
        List<ProductModel> productModelList = productModelRepository.findAll();
        assertThat(productModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductModel() throws Exception {
        int databaseSizeBeforeUpdate = productModelRepository.findAll().size();
        productModel.setId(count.incrementAndGet());

        // Create the ProductModel
        ProductModelDTO productModelDTO = productModelMapper.toDto(productModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductModelMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productModelDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductModel in the database
        List<ProductModel> productModelList = productModelRepository.findAll();
        assertThat(productModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductModelWithPatch() throws Exception {
        // Initialize the database
        productModelRepository.saveAndFlush(productModel);

        int databaseSizeBeforeUpdate = productModelRepository.findAll().size();

        // Update the productModel using partial update
        ProductModel partialUpdatedProductModel = new ProductModel();
        partialUpdatedProductModel.setId(productModel.getId());

        partialUpdatedProductModel.modelCode(UPDATED_MODEL_CODE);

        restProductModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductModel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductModel))
            )
            .andExpect(status().isOk());

        // Validate the ProductModel in the database
        List<ProductModel> productModelList = productModelRepository.findAll();
        assertThat(productModelList).hasSize(databaseSizeBeforeUpdate);
        ProductModel testProductModel = productModelList.get(productModelList.size() - 1);
        assertThat(testProductModel.getModelCode()).isEqualTo(UPDATED_MODEL_CODE);
    }

    @Test
    @Transactional
    void fullUpdateProductModelWithPatch() throws Exception {
        // Initialize the database
        productModelRepository.saveAndFlush(productModel);

        int databaseSizeBeforeUpdate = productModelRepository.findAll().size();

        // Update the productModel using partial update
        ProductModel partialUpdatedProductModel = new ProductModel();
        partialUpdatedProductModel.setId(productModel.getId());

        partialUpdatedProductModel.modelCode(UPDATED_MODEL_CODE);

        restProductModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductModel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductModel))
            )
            .andExpect(status().isOk());

        // Validate the ProductModel in the database
        List<ProductModel> productModelList = productModelRepository.findAll();
        assertThat(productModelList).hasSize(databaseSizeBeforeUpdate);
        ProductModel testProductModel = productModelList.get(productModelList.size() - 1);
        assertThat(testProductModel.getModelCode()).isEqualTo(UPDATED_MODEL_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingProductModel() throws Exception {
        int databaseSizeBeforeUpdate = productModelRepository.findAll().size();
        productModel.setId(count.incrementAndGet());

        // Create the ProductModel
        ProductModelDTO productModelDTO = productModelMapper.toDto(productModel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productModelDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductModel in the database
        List<ProductModel> productModelList = productModelRepository.findAll();
        assertThat(productModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductModel() throws Exception {
        int databaseSizeBeforeUpdate = productModelRepository.findAll().size();
        productModel.setId(count.incrementAndGet());

        // Create the ProductModel
        ProductModelDTO productModelDTO = productModelMapper.toDto(productModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductModel in the database
        List<ProductModel> productModelList = productModelRepository.findAll();
        assertThat(productModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductModel() throws Exception {
        int databaseSizeBeforeUpdate = productModelRepository.findAll().size();
        productModel.setId(count.incrementAndGet());

        // Create the ProductModel
        ProductModelDTO productModelDTO = productModelMapper.toDto(productModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductModelMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productModelDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductModel in the database
        List<ProductModel> productModelList = productModelRepository.findAll();
        assertThat(productModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductModel() throws Exception {
        // Initialize the database
        productModelRepository.saveAndFlush(productModel);

        int databaseSizeBeforeDelete = productModelRepository.findAll().size();

        // Delete the productModel
        restProductModelMockMvc
            .perform(delete(ENTITY_API_URL_ID, productModel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductModel> productModelList = productModelRepository.findAll();
        assertThat(productModelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
