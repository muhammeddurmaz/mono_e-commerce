package com.fastshop.service;

import com.fastshop.domain.*; // for static metamodels
import com.fastshop.domain.Product;
import com.fastshop.repository.ProductRepository;
import com.fastshop.repository.search.ProductSearchRepository;
import com.fastshop.service.criteria.ProductCriteria;
import com.fastshop.service.dto.ProductDTO;
import com.fastshop.service.mapper.ProductMapper;
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
 * Service for executing complex queries for {@link Product} entities in the database.
 * The main input is a {@link ProductCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductDTO} or a {@link Page} of {@link ProductDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductQueryService extends QueryService<Product> {

    private final Logger log = LoggerFactory.getLogger(ProductQueryService.class);

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;


    public ProductQueryService(
        ProductRepository productRepository,
        ProductMapper productMapper
    ) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    /**
     * Return a {@link List} of {@link ProductDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductDTO> findByCriteria(ProductCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Product> specification = createSpecification(criteria);
        return productMapper.toDto(productRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProductDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductDTO> findByCriteria(ProductCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Product> specification = createSpecification(criteria);
        return productRepository.findAll(specification, page).map(productMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Product> specification = createSpecification(criteria);
        return productRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Product> createSpecification(ProductCriteria criteria) {
        Specification<Product> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Product_.id));
            }
            if (criteria.getBarcode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBarcode(), Product_.barcode));
            }
            if (criteria.getModelCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getModelCode(), Product_.modelCode));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Product_.name));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), Product_.price));
            }
            if (criteria.getDiscountPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDiscountPrice(), Product_.discountPrice));
            }
            if (criteria.getAddedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAddedDate(), Product_.addedDate));
            }
            if (criteria.getRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRating(), Product_.rating));
            }
            if (criteria.getSizee() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSizee(), Product_.sizee));
            }
            if (criteria.getStock() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStock(), Product_.stock));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), Product_.active));
            }
            if (criteria.getProductDiscountId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductDiscountId(),
                            root -> root.join(Product_.productDiscounts, JoinType.LEFT).get(ProductDiscount_.id)
                        )
                    );
            }
            if (criteria.getPropertyDetailsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPropertyDetailsId(),
                            root -> root.join(Product_.propertyDetails, JoinType.LEFT).get(PropertyDes_.id)
                        )
                    );
            }
            if (criteria.getProductInventoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductInventoryId(),
                            root -> root.join(Product_.productInventories, JoinType.LEFT).get(ProductInventory_.id)
                        )
                    );
            }
            if (criteria.getCommentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCommentId(), root -> root.join(Product_.comments, JoinType.LEFT).get(Comment_.id))
                    );
            }
            if (criteria.getCategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCategoryId(), root -> root.join(Product_.category, JoinType.LEFT).get(Category_.id))
                    );
            }
            if (criteria.getSubCategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSubCategoryId(),
                            root -> root.join(Product_.subCategory, JoinType.LEFT).get(SubCategory_.id)
                        )
                    );
            }
            if (criteria.getColorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getColorId(), root -> root.join(Product_.color, JoinType.LEFT).get(Color_.id))
                    );
            }
            if (criteria.getProductStatisticsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductStatisticsId(),
                            root -> root.join(Product_.productStatistics, JoinType.LEFT).get(ProductStatistics_.id)
                        )
                    );
            }
            if (criteria.getProductModelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductModelId(),
                            root -> root.join(Product_.productModel, JoinType.LEFT).get(ProductModel_.id)
                        )
                    );
            }
            if (criteria.getSellerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSellerId(), root -> root.join(Product_.seller, JoinType.LEFT).get(Seller_.id))
                    );
            }
            if (criteria.getBrandId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getBrandId(), root -> root.join(Product_.brand, JoinType.LEFT).get(Brand_.id))
                    );
            }
        }
        return specification;
    }
}
