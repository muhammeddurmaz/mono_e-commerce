package com.fastshop.service;

import com.fastshop.domain.*; // for static metamodels
import com.fastshop.domain.PropertyDes;
import com.fastshop.repository.PropertyDesRepository;
import com.fastshop.repository.search.PropertyDesSearchRepository;
import com.fastshop.service.criteria.PropertyDesCriteria;
import com.fastshop.service.dto.PropertyDesDTO;
import com.fastshop.service.mapper.PropertyDesMapper;
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
 * Service for executing complex queries for {@link PropertyDes} entities in the database.
 * The main input is a {@link PropertyDesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PropertyDesDTO} or a {@link Page} of {@link PropertyDesDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PropertyDesQueryService extends QueryService<PropertyDes> {

    private final Logger log = LoggerFactory.getLogger(PropertyDesQueryService.class);

    private final PropertyDesRepository propertyDesRepository;

    private final PropertyDesMapper propertyDesMapper;



    public PropertyDesQueryService(
        PropertyDesRepository propertyDesRepository,
        PropertyDesMapper propertyDesMapper
    ) {
        this.propertyDesRepository = propertyDesRepository;
        this.propertyDesMapper = propertyDesMapper;
    }

    /**
     * Return a {@link List} of {@link PropertyDesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PropertyDesDTO> findByCriteria(PropertyDesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PropertyDes> specification = createSpecification(criteria);
        return propertyDesMapper.toDto(propertyDesRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PropertyDesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PropertyDesDTO> findByCriteria(PropertyDesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PropertyDes> specification = createSpecification(criteria);
        return propertyDesRepository.findAll(specification, page).map(propertyDesMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PropertyDesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PropertyDes> specification = createSpecification(criteria);
        return propertyDesRepository.count(specification);
    }

    /**
     * Function to convert {@link PropertyDesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PropertyDes> createSpecification(PropertyDesCriteria criteria) {
        Specification<PropertyDes> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PropertyDes_.id));
            }
            if (criteria.getDetail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDetail(), PropertyDes_.detail));
            }
            if (criteria.getPropertyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPropertyId(),
                            root -> root.join(PropertyDes_.property, JoinType.LEFT).get(Property_.id)
                        )
                    );
            }
            if (criteria.getProductId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProductId(), root -> root.join(PropertyDes_.product, JoinType.LEFT).get(Product_.id))
                    );
            }
        }
        return specification;
    }
}
