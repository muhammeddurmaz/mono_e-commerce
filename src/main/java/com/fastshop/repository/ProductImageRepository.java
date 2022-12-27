package com.fastshop.repository;

import com.fastshop.domain.ProductImage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductImage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {}
