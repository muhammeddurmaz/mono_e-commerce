package com.fastshop.service;

import com.fastshop.domain.*; // for static metamodels
import com.fastshop.domain.Favorite;
import com.fastshop.repository.FavoriteRepository;
import com.fastshop.repository.search.FavoriteSearchRepository;
import com.fastshop.service.criteria.FavoriteCriteria;
import com.fastshop.service.dto.FavoriteDTO;
import com.fastshop.service.mapper.FavoriteMapper;
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
 * Service for executing complex queries for {@link Favorite} entities in the database.
 * The main input is a {@link FavoriteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FavoriteDTO} or a {@link Page} of {@link FavoriteDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FavoriteQueryService extends QueryService<Favorite> {

    private final Logger log = LoggerFactory.getLogger(FavoriteQueryService.class);

    private final FavoriteRepository favoriteRepository;

    private final FavoriteMapper favoriteMapper;


    public FavoriteQueryService(
        FavoriteRepository favoriteRepository,
        FavoriteMapper favoriteMapper
    ) {
        this.favoriteRepository = favoriteRepository;
        this.favoriteMapper = favoriteMapper;

    }

    /**
     * Return a {@link List} of {@link FavoriteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FavoriteDTO> findByCriteria(FavoriteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Favorite> specification = createSpecification(criteria);
        return favoriteMapper.toDto(favoriteRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FavoriteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FavoriteDTO> findByCriteria(FavoriteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Favorite> specification = createSpecification(criteria);
        return favoriteRepository.findAll(specification, page).map(favoriteMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FavoriteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Favorite> specification = createSpecification(criteria);
        return favoriteRepository.count(specification);
    }

    /**
     * Function to convert {@link FavoriteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Favorite> createSpecification(FavoriteCriteria criteria) {
        Specification<Favorite> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Favorite_.id));
            }
            if (criteria.getProductId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProductId(), root -> root.join(Favorite_.product, JoinType.LEFT).get(Product_.id))
                    );
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Favorite_.user, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
