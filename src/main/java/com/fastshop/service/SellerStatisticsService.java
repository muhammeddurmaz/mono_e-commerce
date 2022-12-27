package com.fastshop.service;

import com.fastshop.service.dto.SellerStatisticsDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.fastshop.domain.SellerStatistics}.
 */
public interface SellerStatisticsService {
    /**
     * Save a sellerStatistics.
     *
     * @param sellerStatisticsDTO the entity to save.
     * @return the persisted entity.
     */
    SellerStatisticsDTO save(SellerStatisticsDTO sellerStatisticsDTO);

    /**
     * Updates a sellerStatistics.
     *
     * @param sellerStatisticsDTO the entity to update.
     * @return the persisted entity.
     */
    SellerStatisticsDTO update(SellerStatisticsDTO sellerStatisticsDTO);

    /**
     * Partially updates a sellerStatistics.
     *
     * @param sellerStatisticsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SellerStatisticsDTO> partialUpdate(SellerStatisticsDTO sellerStatisticsDTO);

    /**
     * Get all the sellerStatistics.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SellerStatisticsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" sellerStatistics.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SellerStatisticsDTO> findOne(Long id);

    /**
     * Delete the "id" sellerStatistics.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the sellerStatistics corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SellerStatisticsDTO> search(String query, Pageable pageable);
}
