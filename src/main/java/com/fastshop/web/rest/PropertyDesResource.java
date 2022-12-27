package com.fastshop.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.fastshop.repository.PropertyDesRepository;
import com.fastshop.service.PropertyDesQueryService;
import com.fastshop.service.PropertyDesService;
import com.fastshop.service.criteria.PropertyDesCriteria;
import com.fastshop.service.dto.PropertyDesDTO;
import com.fastshop.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link com.fastshop.domain.PropertyDes}.
 */
@RestController
@RequestMapping("/api")
public class PropertyDesResource {

    private final Logger log = LoggerFactory.getLogger(PropertyDesResource.class);

    private static final String ENTITY_NAME = "propertyDes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PropertyDesService propertyDesService;

    private final PropertyDesRepository propertyDesRepository;

    private final PropertyDesQueryService propertyDesQueryService;

    public PropertyDesResource(
        PropertyDesService propertyDesService,
        PropertyDesRepository propertyDesRepository,
        PropertyDesQueryService propertyDesQueryService
    ) {
        this.propertyDesService = propertyDesService;
        this.propertyDesRepository = propertyDesRepository;
        this.propertyDesQueryService = propertyDesQueryService;
    }

    /**
     * {@code POST  /property-des} : Create a new propertyDes.
     *
     * @param propertyDesDTO the propertyDesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new propertyDesDTO, or with status {@code 400 (Bad Request)} if the propertyDes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/property-des")
    public ResponseEntity<PropertyDesDTO> createPropertyDes(@Valid @RequestBody PropertyDesDTO propertyDesDTO) throws URISyntaxException {
        log.debug("REST request to save PropertyDes : {}", propertyDesDTO);
        if (propertyDesDTO.getId() != null) {
            throw new BadRequestAlertException("A new propertyDes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PropertyDesDTO result = propertyDesService.save(propertyDesDTO);
        return ResponseEntity
            .created(new URI("/api/property-des/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /property-des/:id} : Updates an existing propertyDes.
     *
     * @param id the id of the propertyDesDTO to save.
     * @param propertyDesDTO the propertyDesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated propertyDesDTO,
     * or with status {@code 400 (Bad Request)} if the propertyDesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the propertyDesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/property-des/{id}")
    public ResponseEntity<PropertyDesDTO> updatePropertyDes(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PropertyDesDTO propertyDesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PropertyDes : {}, {}", id, propertyDesDTO);
        if (propertyDesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, propertyDesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!propertyDesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PropertyDesDTO result = propertyDesService.update(propertyDesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, propertyDesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /property-des/:id} : Partial updates given fields of an existing propertyDes, field will ignore if it is null
     *
     * @param id the id of the propertyDesDTO to save.
     * @param propertyDesDTO the propertyDesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated propertyDesDTO,
     * or with status {@code 400 (Bad Request)} if the propertyDesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the propertyDesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the propertyDesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/property-des/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PropertyDesDTO> partialUpdatePropertyDes(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PropertyDesDTO propertyDesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PropertyDes partially : {}, {}", id, propertyDesDTO);
        if (propertyDesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, propertyDesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!propertyDesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PropertyDesDTO> result = propertyDesService.partialUpdate(propertyDesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, propertyDesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /property-des} : get all the propertyDes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of propertyDes in body.
     */
    @GetMapping("/property-des")
    public ResponseEntity<List<PropertyDesDTO>> getAllPropertyDes(
        PropertyDesCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get PropertyDes by criteria: {}", criteria);
        Page<PropertyDesDTO> page = propertyDesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /property-des/count} : count all the propertyDes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/property-des/count")
    public ResponseEntity<Long> countPropertyDes(PropertyDesCriteria criteria) {
        log.debug("REST request to count PropertyDes by criteria: {}", criteria);
        return ResponseEntity.ok().body(propertyDesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /property-des/:id} : get the "id" propertyDes.
     *
     * @param id the id of the propertyDesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the propertyDesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/property-des/{id}")
    public ResponseEntity<PropertyDesDTO> getPropertyDes(@PathVariable Long id) {
        log.debug("REST request to get PropertyDes : {}", id);
        Optional<PropertyDesDTO> propertyDesDTO = propertyDesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(propertyDesDTO);
    }

    /**
     * {@code DELETE  /property-des/:id} : delete the "id" propertyDes.
     *
     * @param id the id of the propertyDesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/property-des/{id}")
    public ResponseEntity<Void> deletePropertyDes(@PathVariable Long id) {
        log.debug("REST request to delete PropertyDes : {}", id);
        propertyDesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/property-des?query=:query} : search for the propertyDes corresponding
     * to the query.
     *
     * @param query the query of the propertyDes search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/property-des")
    public ResponseEntity<List<PropertyDesDTO>> searchPropertyDes(
        @RequestParam String query,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of PropertyDes for query {}", query);
        Page<PropertyDesDTO> page = propertyDesService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
