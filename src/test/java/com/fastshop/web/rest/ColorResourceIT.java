package com.fastshop.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fastshop.IntegrationTest;
import com.fastshop.domain.Color;
import com.fastshop.repository.ColorRepository;
import com.fastshop.service.dto.ColorDTO;
import com.fastshop.service.mapper.ColorMapper;
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
 * Integration tests for the {@link ColorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ColorResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/colors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ColorRepository colorRepository;

    @Autowired
    private ColorMapper colorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restColorMockMvc;

    private Color color;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Color createEntity(EntityManager em) {
        Color color = new Color().name(DEFAULT_NAME).code(DEFAULT_CODE);
        return color;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Color createUpdatedEntity(EntityManager em) {
        Color color = new Color().name(UPDATED_NAME).code(UPDATED_CODE);
        return color;
    }

    @BeforeEach
    public void initTest() {
        color = createEntity(em);
    }

    @Test
    @Transactional
    void createColor() throws Exception {
        int databaseSizeBeforeCreate = colorRepository.findAll().size();
        // Create the Color
        ColorDTO colorDTO = colorMapper.toDto(color);
        restColorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(colorDTO)))
            .andExpect(status().isCreated());

        // Validate the Color in the database
        List<Color> colorList = colorRepository.findAll();
        assertThat(colorList).hasSize(databaseSizeBeforeCreate + 1);
        Color testColor = colorList.get(colorList.size() - 1);
        assertThat(testColor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testColor.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    void createColorWithExistingId() throws Exception {
        // Create the Color with an existing ID
        color.setId(1L);
        ColorDTO colorDTO = colorMapper.toDto(color);

        int databaseSizeBeforeCreate = colorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restColorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(colorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Color in the database
        List<Color> colorList = colorRepository.findAll();
        assertThat(colorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = colorRepository.findAll().size();
        // set the field null
        color.setName(null);

        // Create the Color, which fails.
        ColorDTO colorDTO = colorMapper.toDto(color);

        restColorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(colorDTO)))
            .andExpect(status().isBadRequest());

        List<Color> colorList = colorRepository.findAll();
        assertThat(colorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllColors() throws Exception {
        // Initialize the database
        colorRepository.saveAndFlush(color);

        // Get all the colorList
        restColorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(color.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }

    @Test
    @Transactional
    void getColor() throws Exception {
        // Initialize the database
        colorRepository.saveAndFlush(color);

        // Get the color
        restColorMockMvc
            .perform(get(ENTITY_API_URL_ID, color.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(color.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    void getNonExistingColor() throws Exception {
        // Get the color
        restColorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingColor() throws Exception {
        // Initialize the database
        colorRepository.saveAndFlush(color);

        int databaseSizeBeforeUpdate = colorRepository.findAll().size();

        // Update the color
        Color updatedColor = colorRepository.findById(color.getId()).get();
        // Disconnect from session so that the updates on updatedColor are not directly saved in db
        em.detach(updatedColor);
        updatedColor.name(UPDATED_NAME).code(UPDATED_CODE);
        ColorDTO colorDTO = colorMapper.toDto(updatedColor);

        restColorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, colorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(colorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Color in the database
        List<Color> colorList = colorRepository.findAll();
        assertThat(colorList).hasSize(databaseSizeBeforeUpdate);
        Color testColor = colorList.get(colorList.size() - 1);
        assertThat(testColor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testColor.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void putNonExistingColor() throws Exception {
        int databaseSizeBeforeUpdate = colorRepository.findAll().size();
        color.setId(count.incrementAndGet());

        // Create the Color
        ColorDTO colorDTO = colorMapper.toDto(color);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restColorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, colorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(colorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Color in the database
        List<Color> colorList = colorRepository.findAll();
        assertThat(colorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchColor() throws Exception {
        int databaseSizeBeforeUpdate = colorRepository.findAll().size();
        color.setId(count.incrementAndGet());

        // Create the Color
        ColorDTO colorDTO = colorMapper.toDto(color);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restColorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(colorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Color in the database
        List<Color> colorList = colorRepository.findAll();
        assertThat(colorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamColor() throws Exception {
        int databaseSizeBeforeUpdate = colorRepository.findAll().size();
        color.setId(count.incrementAndGet());

        // Create the Color
        ColorDTO colorDTO = colorMapper.toDto(color);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restColorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(colorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Color in the database
        List<Color> colorList = colorRepository.findAll();
        assertThat(colorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateColorWithPatch() throws Exception {
        // Initialize the database
        colorRepository.saveAndFlush(color);

        int databaseSizeBeforeUpdate = colorRepository.findAll().size();

        // Update the color using partial update
        Color partialUpdatedColor = new Color();
        partialUpdatedColor.setId(color.getId());

        restColorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedColor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedColor))
            )
            .andExpect(status().isOk());

        // Validate the Color in the database
        List<Color> colorList = colorRepository.findAll();
        assertThat(colorList).hasSize(databaseSizeBeforeUpdate);
        Color testColor = colorList.get(colorList.size() - 1);
        assertThat(testColor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testColor.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    void fullUpdateColorWithPatch() throws Exception {
        // Initialize the database
        colorRepository.saveAndFlush(color);

        int databaseSizeBeforeUpdate = colorRepository.findAll().size();

        // Update the color using partial update
        Color partialUpdatedColor = new Color();
        partialUpdatedColor.setId(color.getId());

        partialUpdatedColor.name(UPDATED_NAME).code(UPDATED_CODE);

        restColorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedColor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedColor))
            )
            .andExpect(status().isOk());

        // Validate the Color in the database
        List<Color> colorList = colorRepository.findAll();
        assertThat(colorList).hasSize(databaseSizeBeforeUpdate);
        Color testColor = colorList.get(colorList.size() - 1);
        assertThat(testColor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testColor.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingColor() throws Exception {
        int databaseSizeBeforeUpdate = colorRepository.findAll().size();
        color.setId(count.incrementAndGet());

        // Create the Color
        ColorDTO colorDTO = colorMapper.toDto(color);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restColorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, colorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(colorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Color in the database
        List<Color> colorList = colorRepository.findAll();
        assertThat(colorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchColor() throws Exception {
        int databaseSizeBeforeUpdate = colorRepository.findAll().size();
        color.setId(count.incrementAndGet());

        // Create the Color
        ColorDTO colorDTO = colorMapper.toDto(color);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restColorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(colorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Color in the database
        List<Color> colorList = colorRepository.findAll();
        assertThat(colorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamColor() throws Exception {
        int databaseSizeBeforeUpdate = colorRepository.findAll().size();
        color.setId(count.incrementAndGet());

        // Create the Color
        ColorDTO colorDTO = colorMapper.toDto(color);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restColorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(colorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Color in the database
        List<Color> colorList = colorRepository.findAll();
        assertThat(colorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteColor() throws Exception {
        // Initialize the database
        colorRepository.saveAndFlush(color);

        int databaseSizeBeforeDelete = colorRepository.findAll().size();

        // Delete the color
        restColorMockMvc
            .perform(delete(ENTITY_API_URL_ID, color.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Color> colorList = colorRepository.findAll();
        assertThat(colorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
