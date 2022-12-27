package com.fastshop.service;

import com.fastshop.domain.*; // for static metamodels
import com.fastshop.domain.SellerStatistics;
import com.fastshop.repository.SellerStatisticsRepository;
import com.fastshop.repository.search.SellerStatisticsSearchRepository;
import com.fastshop.service.criteria.SellerStatisticsCriteria;
import com.fastshop.service.dto.SellerStatisticsDTO;
import com.fastshop.service.mapper.SellerStatisticsMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link SellerStatistics} entities in the database.
 * The main input is a {@link SellerStatisticsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SellerStatisticsDTO} or a {@link Page} of {@link SellerStatisticsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SellerStatisticsQueryService extends QueryService<SellerStatistics> {

    private final Logger log = LoggerFactory.getLogger(SellerStatisticsQueryService.class);

    private final SellerStatisticsRepository sellerStatisticsRepository;

    private final SellerStatisticsMapper sellerStatisticsMapper;



    public SellerStatisticsQueryService(
        SellerStatisticsRepository sellerStatisticsRepository,
        SellerStatisticsMapper sellerStatisticsMapper
    ) {
        this.sellerStatisticsRepository = sellerStatisticsRepository;
        this.sellerStatisticsMapper = sellerStatisticsMapper;
    }

    /**
     * Return a {@link List} of {@link SellerStatisticsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SellerStatisticsDTO> findByCriteria(SellerStatisticsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SellerStatistics> specification = createSpecification(criteria);
        return sellerStatisticsMapper.toDto(sellerStatisticsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SellerStatisticsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SellerStatisticsDTO> findByCriteria(SellerStatisticsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SellerStatistics> specification = createSpecification(criteria);
        return sellerStatisticsRepository.findAll(specification, page).map(sellerStatisticsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SellerStatisticsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SellerStatistics> specification = createSpecification(criteria);
        return sellerStatisticsRepository.count(specification);
    }

    /**
     * Function to convert {@link SellerStatisticsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SellerStatistics> createSpecification(SellerStatisticsCriteria criteria) {
        Specification<SellerStatistics> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SellerStatistics_.id));
            }
            if (criteria.getProduct() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getProduct(), SellerStatistics_.product));
            }
            if (criteria.getTotalOrder() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalOrder(), SellerStatistics_.totalOrder));
            }
            if (criteria.getTotalEarning() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalEarning(), SellerStatistics_.totalEarning));
            }
            if (criteria.getSellerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSellerId(),
                            root -> root.join(SellerStatistics_.seller, JoinType.LEFT).get(Seller_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
