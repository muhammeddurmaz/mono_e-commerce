package com.fastshop.service;

import com.fastshop.domain.*; // for static metamodels
import com.fastshop.domain.Seller;
import com.fastshop.repository.SellerRepository;
import com.fastshop.repository.search.SellerSearchRepository;
import com.fastshop.service.criteria.SellerCriteria;
import com.fastshop.service.dto.SellerDTO;
import com.fastshop.service.mapper.SellerMapper;
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
 * Service for executing complex queries for {@link Seller} entities in the database.
 * The main input is a {@link SellerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SellerDTO} or a {@link Page} of {@link SellerDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SellerQueryService extends QueryService<Seller> {

    private final Logger log = LoggerFactory.getLogger(SellerQueryService.class);

    private final SellerRepository sellerRepository;

    private final SellerMapper sellerMapper;


    public SellerQueryService(SellerRepository sellerRepository, SellerMapper sellerMapper) {
        this.sellerRepository = sellerRepository;
        this.sellerMapper = sellerMapper;

    }

    /**
     * Return a {@link List} of {@link SellerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SellerDTO> findByCriteria(SellerCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Seller> specification = createSpecification(criteria);
        return sellerMapper.toDto(sellerRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SellerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SellerDTO> findByCriteria(SellerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Seller> specification = createSpecification(criteria);
        return sellerRepository.findAll(specification, page).map(sellerMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SellerCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Seller> specification = createSpecification(criteria);
        return sellerRepository.count(specification);
    }

    /**
     * Function to convert {@link SellerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Seller> createSpecification(SellerCriteria criteria) {
        Specification<Seller> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Seller_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Seller_.name));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Seller_.lastName));
            }
            if (criteria.getShopName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getShopName(), Seller_.shopName));
            }
            if (criteria.getMail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMail(), Seller_.mail));
            }
            if (criteria.getActivated() != null) {
                specification = specification.and(buildSpecification(criteria.getActivated(), Seller_.activated));
            }
            if (criteria.getTckn() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTckn(), Seller_.tckn));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), Seller_.phone));
            }
            if (criteria.getCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCity(), Seller_.city));
            }
            if (criteria.getPlacedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPlacedDate(), Seller_.placedDate));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Seller_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getProductsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProductsId(), root -> root.join(Seller_.products, JoinType.LEFT).get(Product_.id))
                    );
            }
            if (criteria.getSellerProductTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSellerProductTypeId(),
                            root -> root.join(Seller_.sellerProductType, JoinType.LEFT).get(ProductType_.id)
                        )
                    );
            }
            if (criteria.getBrandId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getBrandId(), root -> root.join(Seller_.brand, JoinType.LEFT).get(Brand_.id))
                    );
            }
            if (criteria.getSellerStatisticsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSellerStatisticsId(),
                            root -> root.join(Seller_.sellerStatistics, JoinType.LEFT).get(SellerStatistics_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
