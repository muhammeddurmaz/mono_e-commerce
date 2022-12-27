package com.fastshop.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.fastshop.repository.SellerStatisticsRepository;
import com.fastshop.service.SellerStatisticsQueryService;
import com.fastshop.service.SellerStatisticsService;
import com.fastshop.service.criteria.SellerStatisticsCriteria;
import com.fastshop.service.dto.SellerStatisticsDTO;
import com.fastshop.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link com.fastshop.domain.SellerStatistics}.
 */
@RestController
@RequestMapping("/api")
public class SellerStatisticsResource {

    private final Logger log = LoggerFactory.getLogger(SellerStatisticsResource.class);

    private final SellerStatisticsService sellerStatisticsService;

    private final SellerStatisticsRepository sellerStatisticsRepository;

    private final SellerStatisticsQueryService sellerStatisticsQueryService;

    public SellerStatisticsResource(
        SellerStatisticsService sellerStatisticsService,
        SellerStatisticsRepository sellerStatisticsRepository,
        SellerStatisticsQueryService sellerStatisticsQueryService
    ) {
        this.sellerStatisticsService = sellerStatisticsService;
        this.sellerStatisticsRepository = sellerStatisticsRepository;
        this.sellerStatisticsQueryService = sellerStatisticsQueryService;
    }

    /**
     * {@code GET  /seller-statistics} : get all the sellerStatistics.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sellerStatistics in body.
     */
    @GetMapping("/seller-statistics")
    public ResponseEntity<List<SellerStatisticsDTO>> getAllSellerStatistics(
        SellerStatisticsCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get SellerStatistics by criteria: {}", criteria);
        Page<SellerStatisticsDTO> page = sellerStatisticsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /seller-statistics/count} : count all the sellerStatistics.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/seller-statistics/count")
    public ResponseEntity<Long> countSellerStatistics(SellerStatisticsCriteria criteria) {
        log.debug("REST request to count SellerStatistics by criteria: {}", criteria);
        return ResponseEntity.ok().body(sellerStatisticsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /seller-statistics/:id} : get the "id" sellerStatistics.
     *
     * @param id the id of the sellerStatisticsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sellerStatisticsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/seller-statistics/{id}")
    public ResponseEntity<SellerStatisticsDTO> getSellerStatistics(@PathVariable Long id) {
        log.debug("REST request to get SellerStatistics : {}", id);
        Optional<SellerStatisticsDTO> sellerStatisticsDTO = sellerStatisticsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sellerStatisticsDTO);
    }

    /**
     * {@code SEARCH  /_search/seller-statistics?query=:query} : search for the sellerStatistics corresponding
     * to the query.
     *
     * @param query the query of the sellerStatistics search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/seller-statistics")
    public ResponseEntity<List<SellerStatisticsDTO>> searchSellerStatistics(
        @RequestParam String query,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of SellerStatistics for query {}", query);
        Page<SellerStatisticsDTO> page = sellerStatisticsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
