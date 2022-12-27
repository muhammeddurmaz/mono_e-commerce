package com.fastshop.repository;

import com.fastshop.domain.PropertyDes;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PropertyDes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PropertyDesRepository extends JpaRepository<PropertyDes, Long>, JpaSpecificationExecutor<PropertyDes> {}
