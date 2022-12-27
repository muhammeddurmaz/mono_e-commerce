package com.fastshop.repository;

import com.fastshop.domain.Color;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Color entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ColorRepository extends JpaRepository<Color, Long> {}
