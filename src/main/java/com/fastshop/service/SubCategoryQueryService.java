package com.fastshop.service;

import com.fastshop.domain.*; // for static metamodels
import com.fastshop.domain.SubCategory;
import com.fastshop.repository.SubCategoryRepository;
import com.fastshop.repository.search.SubCategorySearchRepository;
import com.fastshop.service.criteria.SubCategoryCriteria;
import com.fastshop.service.dto.SubCategoryDTO;
import com.fastshop.service.mapper.SubCategoryMapper;
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
 * Service for executing complex queries for {@link SubCategory} entities in the database.
 * The main input is a {@link SubCategoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SubCategoryDTO} or a {@link Page} of {@link SubCategoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SubCategoryQueryService extends QueryService<SubCategory> {

    private final Logger log = LoggerFactory.getLogger(SubCategoryQueryService.class);

    private final SubCategoryRepository subCategoryRepository;

    private final SubCategoryMapper subCategoryMapper;



    public SubCategoryQueryService(
        SubCategoryRepository subCategoryRepository,
        SubCategoryMapper subCategoryMapper
    ) {
        this.subCategoryRepository = subCategoryRepository;
        this.subCategoryMapper = subCategoryMapper;

    }

    /**
     * Return a {@link List} of {@link SubCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SubCategoryDTO> findByCriteria(SubCategoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SubCategory> specification = createSpecification(criteria);
        return subCategoryMapper.toDto(subCategoryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SubCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SubCategoryDTO> findByCriteria(SubCategoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SubCategory> specification = createSpecification(criteria);
        return subCategoryRepository.findAll(specification, page).map(subCategoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SubCategoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SubCategory> specification = createSpecification(criteria);
        return subCategoryRepository.count(specification);
    }

    /**
     * Function to convert {@link SubCategoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SubCategory> createSpecification(SubCategoryCriteria criteria) {
        Specification<SubCategory> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SubCategory_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), SubCategory_.name));
            }
            if (criteria.getCategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCategoryId(),
                            root -> root.join(SubCategory_.category, JoinType.LEFT).get(Category_.id)
                        )
                    );
            }
            if (criteria.getProductId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductId(),
                            root -> root.join(SubCategory_.products, JoinType.LEFT).get(Product_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
