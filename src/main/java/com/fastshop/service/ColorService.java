package com.fastshop.service;

import com.fastshop.service.dto.ColorDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.fastshop.domain.Color}.
 */
public interface ColorService {
    /**
     * Save a color.
     *
     * @param colorDTO the entity to save.
     * @return the persisted entity.
     */
    ColorDTO save(ColorDTO colorDTO);

    /**
     * Updates a color.
     *
     * @param colorDTO the entity to update.
     * @return the persisted entity.
     */
    ColorDTO update(ColorDTO colorDTO);

    /**
     * Partially updates a color.
     *
     * @param colorDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ColorDTO> partialUpdate(ColorDTO colorDTO);

    /**
     * Get all the colors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ColorDTO> findAll(Pageable pageable);

    /**
     * Get the "id" color.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ColorDTO> findOne(Long id);

    /**
     * Delete the "id" color.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
