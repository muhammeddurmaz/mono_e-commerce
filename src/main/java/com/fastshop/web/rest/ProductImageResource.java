package com.fastshop.web.rest;

import com.fastshop.repository.ProductImageRepository;
import com.fastshop.service.ProductImageService;
import com.fastshop.service.dto.ProductImageDTO;
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
 * REST controller for managing {@link com.fastshop.domain.ProductImage}.
 */
@RestController
@RequestMapping("/api")
public class ProductImageResource {

    private final Logger log = LoggerFactory.getLogger(ProductImageResource.class);

    private static final String ENTITY_NAME = "productImage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductImageService productImageService;

    private final ProductImageRepository productImageRepository;

    public ProductImageResource(ProductImageService productImageService, ProductImageRepository productImageRepository) {
        this.productImageService = productImageService;
        this.productImageRepository = productImageRepository;
    }

    /**
     * {@code POST  /product-images} : Create a new productImage.
     *
     * @param productImageDTO the productImageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productImageDTO, or with status {@code 400 (Bad Request)} if the productImage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-images")
    public ResponseEntity<ProductImageDTO> createProductImage(@Valid @RequestBody ProductImageDTO productImageDTO)
        throws URISyntaxException {
        log.debug("REST request to save ProductImage : {}", productImageDTO);
        if (productImageDTO.getId() != null) {
            throw new BadRequestAlertException("A new productImage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductImageDTO result = productImageService.save(productImageDTO);
        return ResponseEntity
            .created(new URI("/api/product-images/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /product-images/:id} : Updates an existing productImage.
     *
     * @param id the id of the productImageDTO to save.
     * @param productImageDTO the productImageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productImageDTO,
     * or with status {@code 400 (Bad Request)} if the productImageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-images/{id}")
    public ResponseEntity<ProductImageDTO> updateProductImage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductImageDTO productImageDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ProductImage : {}, {}", id, productImageDTO);
        if (productImageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productImageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductImageDTO result = productImageService.update(productImageDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, productImageDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /product-images/:id} : Partial updates given fields of an existing productImage, field will ignore if it is null
     *
     * @param id the id of the productImageDTO to save.
     * @param productImageDTO the productImageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productImageDTO,
     * or with status {@code 400 (Bad Request)} if the productImageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the productImageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the productImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/product-images/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductImageDTO> partialUpdateProductImage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductImageDTO productImageDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductImage partially : {}, {}", id, productImageDTO);
        if (productImageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productImageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductImageDTO> result = productImageService.partialUpdate(productImageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, productImageDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /product-images} : get all the productImages.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productImages in body.
     */
    @GetMapping("/product-images")
    public ResponseEntity<List<ProductImageDTO>> getAllProductImages(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of ProductImages");
        Page<ProductImageDTO> page = productImageService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-images/:id} : get the "id" productImage.
     *
     * @param id the id of the productImageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productImageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-images/{id}")
    public ResponseEntity<ProductImageDTO> getProductImage(@PathVariable Long id) {
        log.debug("REST request to get ProductImage : {}", id);
        Optional<ProductImageDTO> productImageDTO = productImageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productImageDTO);
    }

    /**
     * {@code DELETE  /product-images/:id} : delete the "id" productImage.
     *
     * @param id the id of the productImageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-images/{id}")
    public ResponseEntity<Void> deleteProductImage(@PathVariable Long id) {
        log.debug("REST request to delete ProductImage : {}", id);
        productImageService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
