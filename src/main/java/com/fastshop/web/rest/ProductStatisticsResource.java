package com.fastshop.web.rest;

import com.fastshop.domain.ProductStatistics;
import com.fastshop.repository.ProductStatisticsRepository;
import com.fastshop.service.ProductStatisticsService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.fastshop.domain.ProductStatistics}.
 */
@RestController
@RequestMapping("/api")
public class ProductStatisticsResource {

    private final Logger log = LoggerFactory.getLogger(ProductStatisticsResource.class);

    private final ProductStatisticsService productStatisticsService;

    private final ProductStatisticsRepository productStatisticsRepository;

    public ProductStatisticsResource(
        ProductStatisticsService productStatisticsService,
        ProductStatisticsRepository productStatisticsRepository
    ) {
        this.productStatisticsService = productStatisticsService;
        this.productStatisticsRepository = productStatisticsRepository;
    }

    /**
     * {@code GET  /product-statistics} : get all the productStatistics.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productStatistics in body.
     */
    @GetMapping("/product-statistics")
    public ResponseEntity<List<ProductStatistics>> getAllProductStatistics(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of ProductStatistics");
        Page<ProductStatistics> page = productStatisticsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-statistics/:id} : get the "id" productStatistics.
     *
     * @param id the id of the productStatistics to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productStatistics, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-statistics/{id}")
    public ResponseEntity<ProductStatistics> getProductStatistics(@PathVariable Long id) {
        log.debug("REST request to get ProductStatistics : {}", id);
        Optional<ProductStatistics> productStatistics = productStatisticsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productStatistics);
    }
}
