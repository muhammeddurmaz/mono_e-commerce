package com.fastshop.service;

import com.fastshop.service.dto.ProductTypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.fastshop.domain.ProductType}.
 */
public interface ProductTypeService {
    /**
     * Save a productType.
     *
     * @param productTypeDTO the entity to save.
     * @return the persisted entity.
     */
    ProductTypeDTO save(ProductTypeDTO productTypeDTO);

    /**
     * Updates a productType.
     *
     * @param productTypeDTO the entity to update.
     * @return the persisted entity.
     */
    ProductTypeDTO update(ProductTypeDTO productTypeDTO);

    /**
     * Partially updates a productType.
     *
     * @param productTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProductTypeDTO> partialUpdate(ProductTypeDTO productTypeDTO);

    /**
     * Get all the productTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProductTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" productType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductTypeDTO> findOne(Long id);

    /**
     * Delete the "id" productType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the productType corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProductTypeDTO> search(String query, Pageable pageable);
}
