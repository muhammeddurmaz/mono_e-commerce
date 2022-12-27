package com.fastshop.service;

import com.fastshop.domain.ProductStatistics;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ProductStatistics}.
 */
public interface ProductStatisticsService {
    /**
     * Save a productStatistics.
     *
     * @param productStatistics the entity to save.
     * @return the persisted entity.
     */
    ProductStatistics save(ProductStatistics productStatistics);

    /**
     * Updates a productStatistics.
     *
     * @param productStatistics the entity to update.
     * @return the persisted entity.
     */
    ProductStatistics update(ProductStatistics productStatistics);

    /**
     * Partially updates a productStatistics.
     *
     * @param productStatistics the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProductStatistics> partialUpdate(ProductStatistics productStatistics);

    /**
     * Get all the productStatistics.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProductStatistics> findAll(Pageable pageable);

    /**
     * Get the "id" productStatistics.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductStatistics> findOne(Long id);

    /**
     * Delete the "id" productStatistics.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
