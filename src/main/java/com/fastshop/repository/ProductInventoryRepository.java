package com.fastshop.repository;

import com.fastshop.domain.ProductInventory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductInventory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductInventoryRepository extends JpaRepository<ProductInventory, Long>, JpaSpecificationExecutor<ProductInventory> {}
