package com.fastshop.service;

import com.fastshop.service.dto.ProductDiscountDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.fastshop.domain.ProductDiscount}.
 */
public interface ProductDiscountService {
    /**
     * Save a productDiscount.
     *
     * @param productDiscountDTO the entity to save.
     * @return the persisted entity.
     */
    ProductDiscountDTO save(ProductDiscountDTO productDiscountDTO);

    /**
     * Updates a productDiscount.
     *
     * @param productDiscountDTO the entity to update.
     * @return the persisted entity.
     */
    ProductDiscountDTO update(ProductDiscountDTO productDiscountDTO);

    /**
     * Partially updates a productDiscount.
     *
     * @param productDiscountDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProductDiscountDTO> partialUpdate(ProductDiscountDTO productDiscountDTO);

    /**
     * Get all the productDiscounts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProductDiscountDTO> findAll(Pageable pageable);

    /**
     * Get the "id" productDiscount.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductDiscountDTO> findOne(Long id);

    /**
     * Delete the "id" productDiscount.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the productDiscount corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProductDiscountDTO> search(String query, Pageable pageable);
}
