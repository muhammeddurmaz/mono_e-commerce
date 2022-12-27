package com.fastshop.repository;

import com.fastshop.domain.ProductModel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductModel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductModelRepository extends JpaRepository<ProductModel, Long>, JpaSpecificationExecutor<ProductModel> {}
