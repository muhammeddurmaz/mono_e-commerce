package com.fastshop.repository;

import com.fastshop.domain.ProductStatistics;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductStatistics entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductStatisticsRepository extends JpaRepository<ProductStatistics, Long> {}
