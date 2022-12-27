package com.fastshop.service;

import com.fastshop.domain.*; // for static metamodels
import com.fastshop.domain.UserAdress;
import com.fastshop.repository.UserAdressRepository;
import com.fastshop.service.criteria.UserAdressCriteria;
import com.fastshop.service.dto.UserAdressDTO;
import com.fastshop.service.mapper.UserAdressMapper;
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
 * Service for executing complex queries for {@link UserAdress} entities in the database.
 * The main input is a {@link UserAdressCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UserAdressDTO} or a {@link Page} of {@link UserAdressDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserAdressQueryService extends QueryService<UserAdress> {

    private final Logger log = LoggerFactory.getLogger(UserAdressQueryService.class);

    private final UserAdressRepository userAdressRepository;

    private final UserAdressMapper userAdressMapper;

    public UserAdressQueryService(UserAdressRepository userAdressRepository, UserAdressMapper userAdressMapper) {
        this.userAdressRepository = userAdressRepository;
        this.userAdressMapper = userAdressMapper;
    }

    /**
     * Return a {@link List} of {@link UserAdressDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UserAdressDTO> findByCriteria(UserAdressCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UserAdress> specification = createSpecification(criteria);
        return userAdressMapper.toDto(userAdressRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link UserAdressDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserAdressDTO> findByCriteria(UserAdressCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserAdress> specification = createSpecification(criteria);
        return userAdressRepository.findAll(specification, page).map(userAdressMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserAdressCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UserAdress> specification = createSpecification(criteria);
        return userAdressRepository.count(specification);
    }

    /**
     * Function to convert {@link UserAdressCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UserAdress> createSpecification(UserAdressCriteria criteria) {
        Specification<UserAdress> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UserAdress_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), UserAdress_.name));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), UserAdress_.lastName));
            }
            if (criteria.getTelephone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTelephone(), UserAdress_.telephone));
            }
            if (criteria.getCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCity(), UserAdress_.city));
            }
            if (criteria.getAdressTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAdressTitle(), UserAdress_.adressTitle));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(UserAdress_.user, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
