package com.fastshop.service;

import com.fastshop.service.dto.SubCategoryDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.fastshop.domain.SubCategory}.
 */
public interface SubCategoryService {
    /**
     * Save a subCategory.
     *
     * @param subCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    SubCategoryDTO save(SubCategoryDTO subCategoryDTO);

    /**
     * Updates a subCategory.
     *
     * @param subCategoryDTO the entity to update.
     * @return the persisted entity.
     */
    SubCategoryDTO update(SubCategoryDTO subCategoryDTO);

    /**
     * Partially updates a subCategory.
     *
     * @param subCategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SubCategoryDTO> partialUpdate(SubCategoryDTO subCategoryDTO);

    /**
     * Get all the subCategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SubCategoryDTO> findAll(Pageable pageable);

    /**
     * Get the "id" subCategory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SubCategoryDTO> findOne(Long id);

    /**
     * Delete the "id" subCategory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the subCategory corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SubCategoryDTO> search(String query, Pageable pageable);
}
