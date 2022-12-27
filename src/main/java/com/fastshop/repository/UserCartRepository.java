package com.fastshop.repository;

import com.fastshop.domain.UserCart;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserCart entity.
 */
@Repository
public interface UserCartRepository extends JpaRepository<UserCart, Long> {
    @Query("select userCart from UserCart userCart where userCart.user.login = ?#{principal.username}")
    List<UserCart> findByUserIsCurrentUser();

    default Optional<UserCart> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<UserCart> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<UserCart> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct userCart from UserCart userCart left join fetch userCart.user",
        countQuery = "select count(distinct userCart) from UserCart userCart"
    )
    Page<UserCart> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct userCart from UserCart userCart left join fetch userCart.user")
    List<UserCart> findAllWithToOneRelationships();

    @Query("select userCart from UserCart userCart left join fetch userCart.user where userCart.id =:id")
    Optional<UserCart> findOneWithToOneRelationships(@Param("id") Long id);
}
