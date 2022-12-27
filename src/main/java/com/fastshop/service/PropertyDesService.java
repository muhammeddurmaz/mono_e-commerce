package com.fastshop.service;

import com.fastshop.service.dto.PropertyDesDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.fastshop.domain.PropertyDes}.
 */
public interface PropertyDesService {
    /**
     * Save a propertyDes.
     *
     * @param propertyDesDTO the entity to save.
     * @return the persisted entity.
     */
    PropertyDesDTO save(PropertyDesDTO propertyDesDTO);

    /**
     * Updates a propertyDes.
     *
     * @param propertyDesDTO the entity to update.
     * @return the persisted entity.
     */
    PropertyDesDTO update(PropertyDesDTO propertyDesDTO);

    /**
     * Partially updates a propertyDes.
     *
     * @param propertyDesDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PropertyDesDTO> partialUpdate(PropertyDesDTO propertyDesDTO);

    /**
     * Get all the propertyDes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PropertyDesDTO> findAll(Pageable pageable);

    /**
     * Get the "id" propertyDes.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PropertyDesDTO> findOne(Long id);

    /**
     * Delete the "id" propertyDes.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the propertyDes corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PropertyDesDTO> search(String query, Pageable pageable);
}
