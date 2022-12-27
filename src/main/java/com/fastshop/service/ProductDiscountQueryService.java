package com.fastshop.service;

import com.fastshop.domain.*; // for static metamodels
import com.fastshop.domain.ProductDiscount;
import com.fastshop.repository.ProductDiscountRepository;
import com.fastshop.repository.search.ProductDiscountSearchRepository;
import com.fastshop.service.criteria.ProductDiscountCriteria;
import com.fastshop.service.dto.ProductDiscountDTO;
import com.fastshop.service.mapper.ProductDiscountMapper;
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
 * Service for executing complex queries for {@link ProductDiscount} entities in the database.
 * The main input is a {@link ProductDiscountCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductDiscountDTO} or a {@link Page} of {@link ProductDiscountDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductDiscountQueryService extends QueryService<ProductDiscount> {

    private final Logger log = LoggerFactory.getLogger(ProductDiscountQueryService.class);

    private final ProductDiscountRepository productDiscountRepository;

    private final ProductDiscountMapper productDiscountMapper;


    public ProductDiscountQueryService(
        ProductDiscountRepository productDiscountRepository,
        ProductDiscountMapper productDiscountMapper
    ) {
        this.productDiscountRepository = productDiscountRepository;
        this.productDiscountMapper = productDiscountMapper;
    }

    /**
     * Return a {@link List} of {@link ProductDiscountDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductDiscountDTO> findByCriteria(ProductDiscountCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ProductDiscount> specification = createSpecification(criteria);
        return productDiscountMapper.toDto(productDiscountRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProductDiscountDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductDiscountDTO> findByCriteria(ProductDiscountCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductDiscount> specification = createSpecification(criteria);
        return productDiscountRepository.findAll(specification, page).map(productDiscountMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductDiscountCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ProductDiscount> specification = createSpecification(criteria);
        return productDiscountRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductDiscountCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductDiscount> createSpecification(ProductDiscountCriteria criteria) {
        Specification<ProductDiscount> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ProductDiscount_.id));
            }
            if (criteria.getAddedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAddedDate(), ProductDiscount_.addedDate));
            }
            if (criteria.getDueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDueDate(), ProductDiscount_.dueDate));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), ProductDiscount_.description));
            }
            if (criteria.getDiscountId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDiscountId(),
                            root -> root.join(ProductDiscount_.discount, JoinType.LEFT).get(Discount_.id)
                        )
                    );
            }
            if (criteria.getProductId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductId(),
                            root -> root.join(ProductDiscount_.product, JoinType.LEFT).get(Product_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
