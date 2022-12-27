package com.fastshop.service;

import com.fastshop.domain.*; // for static metamodels
import com.fastshop.domain.ProductType;
import com.fastshop.repository.ProductTypeRepository;
import com.fastshop.repository.search.ProductTypeSearchRepository;
import com.fastshop.service.criteria.ProductTypeCriteria;
import com.fastshop.service.dto.ProductTypeDTO;
import com.fastshop.service.mapper.ProductTypeMapper;
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
 * Service for executing complex queries for {@link ProductType} entities in the database.
 * The main input is a {@link ProductTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductTypeDTO} or a {@link Page} of {@link ProductTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductTypeQueryService extends QueryService<ProductType> {

    private final Logger log = LoggerFactory.getLogger(ProductTypeQueryService.class);

    private final ProductTypeRepository productTypeRepository;

    private final ProductTypeMapper productTypeMapper;


    public ProductTypeQueryService(
        ProductTypeRepository productTypeRepository,
        ProductTypeMapper productTypeMapper
    ) {
        this.productTypeRepository = productTypeRepository;
        this.productTypeMapper = productTypeMapper;
    }

    /**
     * Return a {@link List} of {@link ProductTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductTypeDTO> findByCriteria(ProductTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ProductType> specification = createSpecification(criteria);
        return productTypeMapper.toDto(productTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProductTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductTypeDTO> findByCriteria(ProductTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductType> specification = createSpecification(criteria);
        return productTypeRepository.findAll(specification, page).map(productTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ProductType> specification = createSpecification(criteria);
        return productTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductType> createSpecification(ProductTypeCriteria criteria) {
        Specification<ProductType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ProductType_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ProductType_.name));
            }
            if (criteria.getCategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCategoryId(),
                            root -> root.join(ProductType_.categories, JoinType.LEFT).get(Category_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
