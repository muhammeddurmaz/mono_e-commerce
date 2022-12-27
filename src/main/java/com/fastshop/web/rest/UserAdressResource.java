package com.fastshop.web.rest;

import com.fastshop.repository.UserAdressRepository;
import com.fastshop.service.UserAdressQueryService;
import com.fastshop.service.UserAdressService;
import com.fastshop.service.criteria.UserAdressCriteria;
import com.fastshop.service.dto.UserAdressDTO;
import com.fastshop.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.fastshop.domain.UserAdress}.
 */
@RestController
@RequestMapping("/api")
public class UserAdressResource {

    private final Logger log = LoggerFactory.getLogger(UserAdressResource.class);

    private static final String ENTITY_NAME = "userAdress";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserAdressService userAdressService;

    private final UserAdressRepository userAdressRepository;

    private final UserAdressQueryService userAdressQueryService;

    public UserAdressResource(
        UserAdressService userAdressService,
        UserAdressRepository userAdressRepository,
        UserAdressQueryService userAdressQueryService
    ) {
        this.userAdressService = userAdressService;
        this.userAdressRepository = userAdressRepository;
        this.userAdressQueryService = userAdressQueryService;
    }

    /**
     * {@code POST  /user-adresses} : Create a new userAdress.
     *
     * @param userAdressDTO the userAdressDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userAdressDTO, or with status {@code 400 (Bad Request)} if the userAdress has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-adresses")
    public ResponseEntity<UserAdressDTO> createUserAdress(@Valid @RequestBody UserAdressDTO userAdressDTO) throws URISyntaxException {
        log.debug("REST request to save UserAdress : {}", userAdressDTO);
        if (userAdressDTO.getId() != null) {
            throw new BadRequestAlertException("A new userAdress cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserAdressDTO result = userAdressService.save(userAdressDTO);
        return ResponseEntity
            .created(new URI("/api/user-adresses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-adresses/:id} : Updates an existing userAdress.
     *
     * @param id the id of the userAdressDTO to save.
     * @param userAdressDTO the userAdressDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAdressDTO,
     * or with status {@code 400 (Bad Request)} if the userAdressDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userAdressDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-adresses/{id}")
    public ResponseEntity<UserAdressDTO> updateUserAdress(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserAdressDTO userAdressDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserAdress : {}, {}", id, userAdressDTO);
        if (userAdressDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userAdressDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userAdressRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserAdressDTO result = userAdressService.update(userAdressDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userAdressDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-adresses/:id} : Partial updates given fields of an existing userAdress, field will ignore if it is null
     *
     * @param id the id of the userAdressDTO to save.
     * @param userAdressDTO the userAdressDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAdressDTO,
     * or with status {@code 400 (Bad Request)} if the userAdressDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userAdressDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userAdressDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-adresses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserAdressDTO> partialUpdateUserAdress(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserAdressDTO userAdressDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserAdress partially : {}, {}", id, userAdressDTO);
        if (userAdressDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userAdressDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userAdressRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserAdressDTO> result = userAdressService.partialUpdate(userAdressDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userAdressDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-adresses} : get all the userAdresses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userAdresses in body.
     */
    @GetMapping("/user-adresses")
    public ResponseEntity<List<UserAdressDTO>> getAllUserAdresses(
        UserAdressCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get UserAdresses by criteria: {}", criteria);
        Page<UserAdressDTO> page = userAdressQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-adresses/count} : count all the userAdresses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/user-adresses/count")
    public ResponseEntity<Long> countUserAdresses(UserAdressCriteria criteria) {
        log.debug("REST request to count UserAdresses by criteria: {}", criteria);
        return ResponseEntity.ok().body(userAdressQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /user-adresses/:id} : get the "id" userAdress.
     *
     * @param id the id of the userAdressDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userAdressDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-adresses/{id}")
    public ResponseEntity<UserAdressDTO> getUserAdress(@PathVariable Long id) {
        log.debug("REST request to get UserAdress : {}", id);
        Optional<UserAdressDTO> userAdressDTO = userAdressService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userAdressDTO);
    }

    /**
     * {@code DELETE  /user-adresses/:id} : delete the "id" userAdress.
     *
     * @param id the id of the userAdressDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-adresses/{id}")
    public ResponseEntity<Void> deleteUserAdress(@PathVariable Long id) {
        log.debug("REST request to delete UserAdress : {}", id);
        userAdressService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
