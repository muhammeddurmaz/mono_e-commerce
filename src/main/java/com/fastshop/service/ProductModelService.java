package com.fastshop.service;

import com.fastshop.service.dto.ProductModelDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.fastshop.domain.ProductModel}.
 */
public interface ProductModelService {
    /**
     * Save a productModel.
     *
     * @param productModelDTO the entity to save.
     * @return the persisted entity.
     */
    ProductModelDTO save(ProductModelDTO productModelDTO);

    /**
     * Updates a productModel.
     *
     * @param productModelDTO the entity to update.
     * @return the persisted entity.
     */
    ProductModelDTO update(ProductModelDTO productModelDTO);

    /**
     * Partially updates a productModel.
     *
     * @param productModelDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProductModelDTO> partialUpdate(ProductModelDTO productModelDTO);

    /**
     * Get all the productModels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProductModelDTO> findAll(Pageable pageable);

    /**
     * Get the "id" productModel.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductModelDTO> findOne(Long id);

    /**
     * Delete the "id" productModel.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
