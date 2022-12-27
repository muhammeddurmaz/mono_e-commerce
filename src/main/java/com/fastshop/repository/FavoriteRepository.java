package com.fastshop.repository;

import com.fastshop.domain.Favorite;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Favorite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long>, JpaSpecificationExecutor<Favorite> {
    @Query("select favorite from Favorite favorite where favorite.user.login = ?#{principal.username}")
    List<Favorite> findByUserIsCurrentUser();
}
