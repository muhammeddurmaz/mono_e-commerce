package com.fastshop.service;

import com.fastshop.domain.*; // for static metamodels
import com.fastshop.domain.Property;
import com.fastshop.repository.PropertyRepository;
import com.fastshop.repository.search.PropertySearchRepository;
import com.fastshop.service.criteria.PropertyCriteria;
import com.fastshop.service.dto.PropertyDTO;
import com.fastshop.service.mapper.PropertyMapper;
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
 * Service for executing complex queries for {@link Property} entities in the database.
 * The main input is a {@link PropertyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PropertyDTO} or a {@link Page} of {@link PropertyDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PropertyQueryService extends QueryService<Property> {

    private final Logger log = LoggerFactory.getLogger(PropertyQueryService.class);

    private final PropertyRepository propertyRepository;

    private final PropertyMapper propertyMapper;



    public PropertyQueryService(
        PropertyRepository propertyRepository,
        PropertyMapper propertyMapper
    ) {
        this.propertyRepository = propertyRepository;
        this.propertyMapper = propertyMapper;
    }

    /**
     * Return a {@link List} of {@link PropertyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PropertyDTO> findByCriteria(PropertyCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Property> specification = createSpecification(criteria);
        return propertyMapper.toDto(propertyRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PropertyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PropertyDTO> findByCriteria(PropertyCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Property> specification = createSpecification(criteria);
        return propertyRepository.findAll(specification, page).map(propertyMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PropertyCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Property> specification = createSpecification(criteria);
        return propertyRepository.count(specification);
    }

    /**
     * Function to convert {@link PropertyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Property> createSpecification(PropertyCriteria criteria) {
        Specification<Property> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Property_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Property_.name));
            }
            if (criteria.getCategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCategoryId(), root -> root.join(Property_.category, JoinType.LEFT).get(Category_.id))
                    );
            }
        }
        return specification;
    }
}
