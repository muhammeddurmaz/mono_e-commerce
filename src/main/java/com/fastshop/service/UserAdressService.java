package com.fastshop.service;

import com.fastshop.service.dto.UserAdressDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.fastshop.domain.UserAdress}.
 */
public interface UserAdressService {
    /**
     * Save a userAdress.
     *
     * @param userAdressDTO the entity to save.
     * @return the persisted entity.
     */
    UserAdressDTO save(UserAdressDTO userAdressDTO);

    /**
     * Updates a userAdress.
     *
     * @param userAdressDTO the entity to update.
     * @return the persisted entity.
     */
    UserAdressDTO update(UserAdressDTO userAdressDTO);

    /**
     * Partially updates a userAdress.
     *
     * @param userAdressDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserAdressDTO> partialUpdate(UserAdressDTO userAdressDTO);

    /**
     * Get all the userAdresses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserAdressDTO> findAll(Pageable pageable);

    /**
     * Get all the userAdresses with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserAdressDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" userAdress.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserAdressDTO> findOne(Long id);

    /**
     * Delete the "id" userAdress.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
