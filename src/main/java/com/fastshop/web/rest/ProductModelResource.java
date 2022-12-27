package com.fastshop.web.rest;

import com.fastshop.repository.ProductModelRepository;
import com.fastshop.service.ProductModelQueryService;
import com.fastshop.service.ProductModelService;
import com.fastshop.service.criteria.ProductModelCriteria;
import com.fastshop.service.dto.ProductModelDTO;
import com.fastshop.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link com.fastshop.domain.ProductModel}.
 */
@RestController
@RequestMapping("/api")
public class ProductModelResource {

    private final Logger log = LoggerFactory.getLogger(ProductModelResource.class);

    private static final String ENTITY_NAME = "productModel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductModelService productModelService;

    private final ProductModelRepository productModelRepository;

    private final ProductModelQueryService productModelQueryService;

    public ProductModelResource(
        ProductModelService productModelService,
        ProductModelRepository productModelRepository,
        ProductModelQueryService productModelQueryService
    ) {
        this.productModelService = productModelService;
        this.productModelRepository = productModelRepository;
        this.productModelQueryService = productModelQueryService;
    }

    /**
     * {@code POST  /product-models} : Create a new productModel.
     *
     * @param productModelDTO the productModelDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productModelDTO, or with status {@code 400 (Bad Request)} if the productModel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-models")
    public ResponseEntity<ProductModelDTO> createProductModel(@RequestBody ProductModelDTO productModelDTO) throws URISyntaxException {
        log.debug("REST request to save ProductModel : {}", productModelDTO);
        if (productModelDTO.getId() != null) {
            throw new BadRequestAlertException("A new productModel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductModelDTO result = productModelService.save(productModelDTO);
        return ResponseEntity
            .created(new URI("/api/product-models/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /product-models/:id} : Updates an existing productModel.
     *
     * @param id the id of the productModelDTO to save.
     * @param productModelDTO the productModelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productModelDTO,
     * or with status {@code 400 (Bad Request)} if the productModelDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productModelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-models/{id}")
    public ResponseEntity<ProductModelDTO> updateProductModel(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductModelDTO productModelDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ProductModel : {}, {}", id, productModelDTO);
        if (productModelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productModelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productModelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductModelDTO result = productModelService.update(productModelDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, productModelDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /product-models/:id} : Partial updates given fields of an existing productModel, field will ignore if it is null
     *
     * @param id the id of the productModelDTO to save.
     * @param productModelDTO the productModelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productModelDTO,
     * or with status {@code 400 (Bad Request)} if the productModelDTO is not valid,
     * or with status {@code 404 (Not Found)} if the productModelDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the productModelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/product-models/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductModelDTO> partialUpdateProductModel(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductModelDTO productModelDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductModel partially : {}, {}", id, productModelDTO);
        if (productModelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productModelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productModelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductModelDTO> result = productModelService.partialUpdate(productModelDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, productModelDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /product-models} : get all the productModels.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productModels in body.
     */
    @GetMapping("/product-models")
    public ResponseEntity<List<ProductModelDTO>> getAllProductModels(
        ProductModelCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get ProductModels by criteria: {}", criteria);
        Page<ProductModelDTO> page = productModelQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-models/count} : count all the productModels.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/product-models/count")
    public ResponseEntity<Long> countProductModels(ProductModelCriteria criteria) {
        log.debug("REST request to count ProductModels by criteria: {}", criteria);
        return ResponseEntity.ok().body(productModelQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /product-models/:id} : get the "id" productModel.
     *
     * @param id the id of the productModelDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productModelDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-models/{id}")
    public ResponseEntity<ProductModelDTO> getProductModel(@PathVariable Long id) {
        log.debug("REST request to get ProductModel : {}", id);
        Optional<ProductModelDTO> productModelDTO = productModelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productModelDTO);
    }

    /**
     * {@code DELETE  /product-models/:id} : delete the "id" productModel.
     *
     * @param id the id of the productModelDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-models/{id}")
    public ResponseEntity<Void> deleteProductModel(@PathVariable Long id) {
        log.debug("REST request to delete ProductModel : {}", id);
        productModelService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
