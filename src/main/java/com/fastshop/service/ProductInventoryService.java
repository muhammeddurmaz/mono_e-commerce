package com.fastshop.service;

import com.fastshop.service.dto.ProductInventoryDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.fastshop.domain.ProductInventory}.
 */
public interface ProductInventoryService {
    /**
     * Save a productInventory.
     *
     * @param productInventoryDTO the entity to save.
     * @return the persisted entity.
     */
    ProductInventoryDTO save(ProductInventoryDTO productInventoryDTO);

    /**
     * Updates a productInventory.
     *
     * @param productInventoryDTO the entity to update.
     * @return the persisted entity.
     */
    ProductInventoryDTO update(ProductInventoryDTO productInventoryDTO);

    /**
     * Partially updates a productInventory.
     *
     * @param productInventoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProductInventoryDTO> partialUpdate(ProductInventoryDTO productInventoryDTO);

    /**
     * Get all the productInventories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProductInventoryDTO> findAll(Pageable pageable);

    /**
     * Get the "id" productInventory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductInventoryDTO> findOne(Long id);

    /**
     * Delete the "id" productInventory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
