package com.fastshop.repository;

import com.fastshop.domain.ProductDiscount;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductDiscount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductDiscountRepository extends JpaRepository<ProductDiscount, Long>, JpaSpecificationExecutor<ProductDiscount> {}
