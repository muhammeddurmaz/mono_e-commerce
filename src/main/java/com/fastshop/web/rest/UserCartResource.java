package com.fastshop.web.rest;

import com.fastshop.repository.UserCartRepository;
import com.fastshop.service.UserCartService;
import com.fastshop.service.dto.UserCartDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.fastshop.domain.UserCart}.
 */
@RestController
@RequestMapping("/api")
public class UserCartResource {

    private final Logger log = LoggerFactory.getLogger(UserCartResource.class);

    private static final String ENTITY_NAME = "userCart";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserCartService userCartService;

    private final UserCartRepository userCartRepository;

    public UserCartResource(UserCartService userCartService, UserCartRepository userCartRepository) {
        this.userCartService = userCartService;
        this.userCartRepository = userCartRepository;
    }

    /**
     * {@code POST  /user-carts} : Create a new userCart.
     *
     * @param userCartDTO the userCartDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userCartDTO, or with status {@code 400 (Bad Request)} if the userCart has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-carts")
    public ResponseEntity<UserCartDTO> createUserCart(@Valid @RequestBody UserCartDTO userCartDTO) throws URISyntaxException {
        log.debug("REST request to save UserCart : {}", userCartDTO);
        if (userCartDTO.getId() != null) {
            throw new BadRequestAlertException("A new userCart cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserCartDTO result = userCartService.save(userCartDTO);
        return ResponseEntity
            .created(new URI("/api/user-carts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-carts/:id} : Updates an existing userCart.
     *
     * @param id the id of the userCartDTO to save.
     * @param userCartDTO the userCartDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userCartDTO,
     * or with status {@code 400 (Bad Request)} if the userCartDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userCartDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-carts/{id}")
    public ResponseEntity<UserCartDTO> updateUserCart(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserCartDTO userCartDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserCart : {}, {}", id, userCartDTO);
        if (userCartDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userCartDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userCartRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserCartDTO result = userCartService.update(userCartDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userCartDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-carts/:id} : Partial updates given fields of an existing userCart, field will ignore if it is null
     *
     * @param id the id of the userCartDTO to save.
     * @param userCartDTO the userCartDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userCartDTO,
     * or with status {@code 400 (Bad Request)} if the userCartDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userCartDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userCartDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-carts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserCartDTO> partialUpdateUserCart(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserCartDTO userCartDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserCart partially : {}, {}", id, userCartDTO);
        if (userCartDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userCartDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userCartRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserCartDTO> result = userCartService.partialUpdate(userCartDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userCartDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-carts} : get all the userCarts.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userCarts in body.
     */
    @GetMapping("/user-carts")
    public ResponseEntity<List<UserCartDTO>> getAllUserCarts(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of UserCarts");
        Page<UserCartDTO> page;
        if (eagerload) {
            page = userCartService.findAllWithEagerRelationships(pageable);
        } else {
            page = userCartService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-carts/:id} : get the "id" userCart.
     *
     * @param id the id of the userCartDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userCartDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-carts/{id}")
    public ResponseEntity<UserCartDTO> getUserCart(@PathVariable Long id) {
        log.debug("REST request to get UserCart : {}", id);
        Optional<UserCartDTO> userCartDTO = userCartService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userCartDTO);
    }

    /**
     * {@code DELETE  /user-carts/:id} : delete the "id" userCart.
     *
     * @param id the id of the userCartDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-carts/{id}")
    public ResponseEntity<Void> deleteUserCart(@PathVariable Long id) {
        log.debug("REST request to delete UserCart : {}", id);
        userCartService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
