package com.fastshop.service;

import com.fastshop.service.dto.UserCartDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.fastshop.domain.UserCart}.
 */
public interface UserCartService {
    /**
     * Save a userCart.
     *
     * @param userCartDTO the entity to save.
     * @return the persisted entity.
     */
    UserCartDTO save(UserCartDTO userCartDTO);

    /**
     * Updates a userCart.
     *
     * @param userCartDTO the entity to update.
     * @return the persisted entity.
     */
    UserCartDTO update(UserCartDTO userCartDTO);

    /**
     * Partially updates a userCart.
     *
     * @param userCartDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserCartDTO> partialUpdate(UserCartDTO userCartDTO);

    /**
     * Get all the userCarts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserCartDTO> findAll(Pageable pageable);

    /**
     * Get all the userCarts with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserCartDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" userCart.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserCartDTO> findOne(Long id);

    /**
     * Delete the "id" userCart.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
