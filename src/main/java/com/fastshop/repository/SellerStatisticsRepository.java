package com.fastshop.repository;

import com.fastshop.domain.SellerStatistics;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SellerStatistics entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SellerStatisticsRepository extends JpaRepository<SellerStatistics, Long>, JpaSpecificationExecutor<SellerStatistics> {}
