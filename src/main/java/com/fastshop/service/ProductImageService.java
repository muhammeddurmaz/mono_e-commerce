package com.fastshop.service;

import com.fastshop.service.dto.ProductImageDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.fastshop.domain.ProductImage}.
 */
public interface ProductImageService {
    /**
     * Save a productImage.
     *
     * @param productImageDTO the entity to save.
     * @return the persisted entity.
     */
    ProductImageDTO save(ProductImageDTO productImageDTO);

    /**
     * Updates a productImage.
     *
     * @param productImageDTO the entity to update.
     * @return the persisted entity.
     */
    ProductImageDTO update(ProductImageDTO productImageDTO);

    /**
     * Partially updates a productImage.
     *
     * @param productImageDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProductImageDTO> partialUpdate(ProductImageDTO productImageDTO);

    /**
     * Get all the productImages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProductImageDTO> findAll(Pageable pageable);

    /**
     * Get the "id" productImage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductImageDTO> findOne(Long id);

    /**
     * Delete the "id" productImage.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
