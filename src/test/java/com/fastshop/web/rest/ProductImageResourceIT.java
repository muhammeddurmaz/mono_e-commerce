package com.fastshop.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fastshop.IntegrationTest;
import com.fastshop.domain.Product;
import com.fastshop.domain.ProductImage;
import com.fastshop.repository.ProductImageRepository;
import com.fastshop.service.dto.ProductImageDTO;
import com.fastshop.service.mapper.ProductImageMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ProductImageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductImageResourceIT {

    private static final byte[] DEFAULT_IMAGE_1 = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE_1 = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_1_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_1_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_IMAGE_2 = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE_2 = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_2_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_2_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_IMAGE_3 = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE_3 = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_3_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_3_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_IMAGE_4 = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE_4 = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_4_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_4_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_IMAGE_5 = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE_5 = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_5_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_5_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_IMAGE_6 = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE_6 = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_6_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_6_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/product-images";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ProductImageMapper productImageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductImageMockMvc;

    private ProductImage productImage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductImage createEntity(EntityManager em) {
        ProductImage productImage = new ProductImage()
            .image1(DEFAULT_IMAGE_1)
            .image1ContentType(DEFAULT_IMAGE_1_CONTENT_TYPE)
            .image2(DEFAULT_IMAGE_2)
            .image2ContentType(DEFAULT_IMAGE_2_CONTENT_TYPE)
            .image3(DEFAULT_IMAGE_3)
            .image3ContentType(DEFAULT_IMAGE_3_CONTENT_TYPE)
            .image4(DEFAULT_IMAGE_4)
            .image4ContentType(DEFAULT_IMAGE_4_CONTENT_TYPE)
            .image5(DEFAULT_IMAGE_5)
            .image5ContentType(DEFAULT_IMAGE_5_CONTENT_TYPE)
            .image6(DEFAULT_IMAGE_6)
            .image6ContentType(DEFAULT_IMAGE_6_CONTENT_TYPE);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        productImage.setProduct(product);
        return productImage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductImage createUpdatedEntity(EntityManager em) {
        ProductImage productImage = new ProductImage()
            .image1(UPDATED_IMAGE_1)
            .image1ContentType(UPDATED_IMAGE_1_CONTENT_TYPE)
            .image2(UPDATED_IMAGE_2)
            .image2ContentType(UPDATED_IMAGE_2_CONTENT_TYPE)
            .image3(UPDATED_IMAGE_3)
            .image3ContentType(UPDATED_IMAGE_3_CONTENT_TYPE)
            .image4(UPDATED_IMAGE_4)
            .image4ContentType(UPDATED_IMAGE_4_CONTENT_TYPE)
            .image5(UPDATED_IMAGE_5)
            .image5ContentType(UPDATED_IMAGE_5_CONTENT_TYPE)
            .image6(UPDATED_IMAGE_6)
            .image6ContentType(UPDATED_IMAGE_6_CONTENT_TYPE);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        productImage.setProduct(product);
        return productImage;
    }

    @BeforeEach
    public void initTest() {
        productImage = createEntity(em);
    }

    @Test
    @Transactional
    void createProductImage() throws Exception {
        int databaseSizeBeforeCreate = productImageRepository.findAll().size();
        // Create the ProductImage
        ProductImageDTO productImageDTO = productImageMapper.toDto(productImage);
        restProductImageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productImageDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ProductImage in the database
        List<ProductImage> productImageList = productImageRepository.findAll();
        assertThat(productImageList).hasSize(databaseSizeBeforeCreate + 1);
        ProductImage testProductImage = productImageList.get(productImageList.size() - 1);
        assertThat(testProductImage.getImage1()).isEqualTo(DEFAULT_IMAGE_1);
        assertThat(testProductImage.getImage1ContentType()).isEqualTo(DEFAULT_IMAGE_1_CONTENT_TYPE);
        assertThat(testProductImage.getImage2()).isEqualTo(DEFAULT_IMAGE_2);
        assertThat(testProductImage.getImage2ContentType()).isEqualTo(DEFAULT_IMAGE_2_CONTENT_TYPE);
        assertThat(testProductImage.getImage3()).isEqualTo(DEFAULT_IMAGE_3);
        assertThat(testProductImage.getImage3ContentType()).isEqualTo(DEFAULT_IMAGE_3_CONTENT_TYPE);
        assertThat(testProductImage.getImage4()).isEqualTo(DEFAULT_IMAGE_4);
        assertThat(testProductImage.getImage4ContentType()).isEqualTo(DEFAULT_IMAGE_4_CONTENT_TYPE);
        assertThat(testProductImage.getImage5()).isEqualTo(DEFAULT_IMAGE_5);
        assertThat(testProductImage.getImage5ContentType()).isEqualTo(DEFAULT_IMAGE_5_CONTENT_TYPE);
        assertThat(testProductImage.getImage6()).isEqualTo(DEFAULT_IMAGE_6);
        assertThat(testProductImage.getImage6ContentType()).isEqualTo(DEFAULT_IMAGE_6_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createProductImageWithExistingId() throws Exception {
        // Create the ProductImage with an existing ID
        productImage.setId(1L);
        ProductImageDTO productImageDTO = productImageMapper.toDto(productImage);

        int databaseSizeBeforeCreate = productImageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductImageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductImage in the database
        List<ProductImage> productImageList = productImageRepository.findAll();
        assertThat(productImageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProductImages() throws Exception {
        // Initialize the database
        productImageRepository.saveAndFlush(productImage);

        // Get all the productImageList
        restProductImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productImage.getId().intValue())))
            .andExpect(jsonPath("$.[*].image1ContentType").value(hasItem(DEFAULT_IMAGE_1_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image1").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE_1))))
            .andExpect(jsonPath("$.[*].image2ContentType").value(hasItem(DEFAULT_IMAGE_2_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image2").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE_2))))
            .andExpect(jsonPath("$.[*].image3ContentType").value(hasItem(DEFAULT_IMAGE_3_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image3").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE_3))))
            .andExpect(jsonPath("$.[*].image4ContentType").value(hasItem(DEFAULT_IMAGE_4_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image4").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE_4))))
            .andExpect(jsonPath("$.[*].image5ContentType").value(hasItem(DEFAULT_IMAGE_5_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image5").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE_5))))
            .andExpect(jsonPath("$.[*].image6ContentType").value(hasItem(DEFAULT_IMAGE_6_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image6").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE_6))));
    }

    @Test
    @Transactional
    void getProductImage() throws Exception {
        // Initialize the database
        productImageRepository.saveAndFlush(productImage);

        // Get the productImage
        restProductImageMockMvc
            .perform(get(ENTITY_API_URL_ID, productImage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productImage.getId().intValue()))
            .andExpect(jsonPath("$.image1ContentType").value(DEFAULT_IMAGE_1_CONTENT_TYPE))
            .andExpect(jsonPath("$.image1").value(Base64Utils.encodeToString(DEFAULT_IMAGE_1)))
            .andExpect(jsonPath("$.image2ContentType").value(DEFAULT_IMAGE_2_CONTENT_TYPE))
            .andExpect(jsonPath("$.image2").value(Base64Utils.encodeToString(DEFAULT_IMAGE_2)))
            .andExpect(jsonPath("$.image3ContentType").value(DEFAULT_IMAGE_3_CONTENT_TYPE))
            .andExpect(jsonPath("$.image3").value(Base64Utils.encodeToString(DEFAULT_IMAGE_3)))
            .andExpect(jsonPath("$.image4ContentType").value(DEFAULT_IMAGE_4_CONTENT_TYPE))
            .andExpect(jsonPath("$.image4").value(Base64Utils.encodeToString(DEFAULT_IMAGE_4)))
            .andExpect(jsonPath("$.image5ContentType").value(DEFAULT_IMAGE_5_CONTENT_TYPE))
            .andExpect(jsonPath("$.image5").value(Base64Utils.encodeToString(DEFAULT_IMAGE_5)))
            .andExpect(jsonPath("$.image6ContentType").value(DEFAULT_IMAGE_6_CONTENT_TYPE))
            .andExpect(jsonPath("$.image6").value(Base64Utils.encodeToString(DEFAULT_IMAGE_6)));
    }

    @Test
    @Transactional
    void getNonExistingProductImage() throws Exception {
        // Get the productImage
        restProductImageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductImage() throws Exception {
        // Initialize the database
        productImageRepository.saveAndFlush(productImage);

        int databaseSizeBeforeUpdate = productImageRepository.findAll().size();

        // Update the productImage
        ProductImage updatedProductImage = productImageRepository.findById(productImage.getId()).get();
        // Disconnect from session so that the updates on updatedProductImage are not directly saved in db
        em.detach(updatedProductImage);
        updatedProductImage
            .image1(UPDATED_IMAGE_1)
            .image1ContentType(UPDATED_IMAGE_1_CONTENT_TYPE)
            .image2(UPDATED_IMAGE_2)
            .image2ContentType(UPDATED_IMAGE_2_CONTENT_TYPE)
            .image3(UPDATED_IMAGE_3)
            .image3ContentType(UPDATED_IMAGE_3_CONTENT_TYPE)
            .image4(UPDATED_IMAGE_4)
            .image4ContentType(UPDATED_IMAGE_4_CONTENT_TYPE)
            .image5(UPDATED_IMAGE_5)
            .image5ContentType(UPDATED_IMAGE_5_CONTENT_TYPE)
            .image6(UPDATED_IMAGE_6)
            .image6ContentType(UPDATED_IMAGE_6_CONTENT_TYPE);
        ProductImageDTO productImageDTO = productImageMapper.toDto(updatedProductImage);

        restProductImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productImageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productImageDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProductImage in the database
        List<ProductImage> productImageList = productImageRepository.findAll();
        assertThat(productImageList).hasSize(databaseSizeBeforeUpdate);
        ProductImage testProductImage = productImageList.get(productImageList.size() - 1);
        assertThat(testProductImage.getImage1()).isEqualTo(UPDATED_IMAGE_1);
        assertThat(testProductImage.getImage1ContentType()).isEqualTo(UPDATED_IMAGE_1_CONTENT_TYPE);
        assertThat(testProductImage.getImage2()).isEqualTo(UPDATED_IMAGE_2);
        assertThat(testProductImage.getImage2ContentType()).isEqualTo(UPDATED_IMAGE_2_CONTENT_TYPE);
        assertThat(testProductImage.getImage3()).isEqualTo(UPDATED_IMAGE_3);
        assertThat(testProductImage.getImage3ContentType()).isEqualTo(UPDATED_IMAGE_3_CONTENT_TYPE);
        assertThat(testProductImage.getImage4()).isEqualTo(UPDATED_IMAGE_4);
        assertThat(testProductImage.getImage4ContentType()).isEqualTo(UPDATED_IMAGE_4_CONTENT_TYPE);
        assertThat(testProductImage.getImage5()).isEqualTo(UPDATED_IMAGE_5);
        assertThat(testProductImage.getImage5ContentType()).isEqualTo(UPDATED_IMAGE_5_CONTENT_TYPE);
        assertThat(testProductImage.getImage6()).isEqualTo(UPDATED_IMAGE_6);
        assertThat(testProductImage.getImage6ContentType()).isEqualTo(UPDATED_IMAGE_6_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingProductImage() throws Exception {
        int databaseSizeBeforeUpdate = productImageRepository.findAll().size();
        productImage.setId(count.incrementAndGet());

        // Create the ProductImage
        ProductImageDTO productImageDTO = productImageMapper.toDto(productImage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productImageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductImage in the database
        List<ProductImage> productImageList = productImageRepository.findAll();
        assertThat(productImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductImage() throws Exception {
        int databaseSizeBeforeUpdate = productImageRepository.findAll().size();
        productImage.setId(count.incrementAndGet());

        // Create the ProductImage
        ProductImageDTO productImageDTO = productImageMapper.toDto(productImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductImage in the database
        List<ProductImage> productImageList = productImageRepository.findAll();
        assertThat(productImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductImage() throws Exception {
        int databaseSizeBeforeUpdate = productImageRepository.findAll().size();
        productImage.setId(count.incrementAndGet());

        // Create the ProductImage
        ProductImageDTO productImageDTO = productImageMapper.toDto(productImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductImageMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productImageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductImage in the database
        List<ProductImage> productImageList = productImageRepository.findAll();
        assertThat(productImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductImageWithPatch() throws Exception {
        // Initialize the database
        productImageRepository.saveAndFlush(productImage);

        int databaseSizeBeforeUpdate = productImageRepository.findAll().size();

        // Update the productImage using partial update
        ProductImage partialUpdatedProductImage = new ProductImage();
        partialUpdatedProductImage.setId(productImage.getId());

        partialUpdatedProductImage
            .image1(UPDATED_IMAGE_1)
            .image1ContentType(UPDATED_IMAGE_1_CONTENT_TYPE)
            .image2(UPDATED_IMAGE_2)
            .image2ContentType(UPDATED_IMAGE_2_CONTENT_TYPE)
            .image5(UPDATED_IMAGE_5)
            .image5ContentType(UPDATED_IMAGE_5_CONTENT_TYPE)
            .image6(UPDATED_IMAGE_6)
            .image6ContentType(UPDATED_IMAGE_6_CONTENT_TYPE);

        restProductImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductImage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductImage))
            )
            .andExpect(status().isOk());

        // Validate the ProductImage in the database
        List<ProductImage> productImageList = productImageRepository.findAll();
        assertThat(productImageList).hasSize(databaseSizeBeforeUpdate);
        ProductImage testProductImage = productImageList.get(productImageList.size() - 1);
        assertThat(testProductImage.getImage1()).isEqualTo(UPDATED_IMAGE_1);
        assertThat(testProductImage.getImage1ContentType()).isEqualTo(UPDATED_IMAGE_1_CONTENT_TYPE);
        assertThat(testProductImage.getImage2()).isEqualTo(UPDATED_IMAGE_2);
        assertThat(testProductImage.getImage2ContentType()).isEqualTo(UPDATED_IMAGE_2_CONTENT_TYPE);
        assertThat(testProductImage.getImage3()).isEqualTo(DEFAULT_IMAGE_3);
        assertThat(testProductImage.getImage3ContentType()).isEqualTo(DEFAULT_IMAGE_3_CONTENT_TYPE);
        assertThat(testProductImage.getImage4()).isEqualTo(DEFAULT_IMAGE_4);
        assertThat(testProductImage.getImage4ContentType()).isEqualTo(DEFAULT_IMAGE_4_CONTENT_TYPE);
        assertThat(testProductImage.getImage5()).isEqualTo(UPDATED_IMAGE_5);
        assertThat(testProductImage.getImage5ContentType()).isEqualTo(UPDATED_IMAGE_5_CONTENT_TYPE);
        assertThat(testProductImage.getImage6()).isEqualTo(UPDATED_IMAGE_6);
        assertThat(testProductImage.getImage6ContentType()).isEqualTo(UPDATED_IMAGE_6_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateProductImageWithPatch() throws Exception {
        // Initialize the database
        productImageRepository.saveAndFlush(productImage);

        int databaseSizeBeforeUpdate = productImageRepository.findAll().size();

        // Update the productImage using partial update
        ProductImage partialUpdatedProductImage = new ProductImage();
        partialUpdatedProductImage.setId(productImage.getId());

        partialUpdatedProductImage
            .image1(UPDATED_IMAGE_1)
            .image1ContentType(UPDATED_IMAGE_1_CONTENT_TYPE)
            .image2(UPDATED_IMAGE_2)
            .image2ContentType(UPDATED_IMAGE_2_CONTENT_TYPE)
            .image3(UPDATED_IMAGE_3)
            .image3ContentType(UPDATED_IMAGE_3_CONTENT_TYPE)
            .image4(UPDATED_IMAGE_4)
            .image4ContentType(UPDATED_IMAGE_4_CONTENT_TYPE)
            .image5(UPDATED_IMAGE_5)
            .image5ContentType(UPDATED_IMAGE_5_CONTENT_TYPE)
            .image6(UPDATED_IMAGE_6)
            .image6ContentType(UPDATED_IMAGE_6_CONTENT_TYPE);

        restProductImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductImage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductImage))
            )
            .andExpect(status().isOk());

        // Validate the ProductImage in the database
        List<ProductImage> productImageList = productImageRepository.findAll();
        assertThat(productImageList).hasSize(databaseSizeBeforeUpdate);
        ProductImage testProductImage = productImageList.get(productImageList.size() - 1);
        assertThat(testProductImage.getImage1()).isEqualTo(UPDATED_IMAGE_1);
        assertThat(testProductImage.getImage1ContentType()).isEqualTo(UPDATED_IMAGE_1_CONTENT_TYPE);
        assertThat(testProductImage.getImage2()).isEqualTo(UPDATED_IMAGE_2);
        assertThat(testProductImage.getImage2ContentType()).isEqualTo(UPDATED_IMAGE_2_CONTENT_TYPE);
        assertThat(testProductImage.getImage3()).isEqualTo(UPDATED_IMAGE_3);
        assertThat(testProductImage.getImage3ContentType()).isEqualTo(UPDATED_IMAGE_3_CONTENT_TYPE);
        assertThat(testProductImage.getImage4()).isEqualTo(UPDATED_IMAGE_4);
        assertThat(testProductImage.getImage4ContentType()).isEqualTo(UPDATED_IMAGE_4_CONTENT_TYPE);
        assertThat(testProductImage.getImage5()).isEqualTo(UPDATED_IMAGE_5);
        assertThat(testProductImage.getImage5ContentType()).isEqualTo(UPDATED_IMAGE_5_CONTENT_TYPE);
        assertThat(testProductImage.getImage6()).isEqualTo(UPDATED_IMAGE_6);
        assertThat(testProductImage.getImage6ContentType()).isEqualTo(UPDATED_IMAGE_6_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingProductImage() throws Exception {
        int databaseSizeBeforeUpdate = productImageRepository.findAll().size();
        productImage.setId(count.incrementAndGet());

        // Create the ProductImage
        ProductImageDTO productImageDTO = productImageMapper.toDto(productImage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productImageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductImage in the database
        List<ProductImage> productImageList = productImageRepository.findAll();
        assertThat(productImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductImage() throws Exception {
        int databaseSizeBeforeUpdate = productImageRepository.findAll().size();
        productImage.setId(count.incrementAndGet());

        // Create the ProductImage
        ProductImageDTO productImageDTO = productImageMapper.toDto(productImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductImage in the database
        List<ProductImage> productImageList = productImageRepository.findAll();
        assertThat(productImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductImage() throws Exception {
        int databaseSizeBeforeUpdate = productImageRepository.findAll().size();
        productImage.setId(count.incrementAndGet());

        // Create the ProductImage
        ProductImageDTO productImageDTO = productImageMapper.toDto(productImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductImageMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productImageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductImage in the database
        List<ProductImage> productImageList = productImageRepository.findAll();
        assertThat(productImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductImage() throws Exception {
        // Initialize the database
        productImageRepository.saveAndFlush(productImage);

        int databaseSizeBeforeDelete = productImageRepository.findAll().size();

        // Delete the productImage
        restProductImageMockMvc
            .perform(delete(ENTITY_API_URL_ID, productImage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductImage> productImageList = productImageRepository.findAll();
        assertThat(productImageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
