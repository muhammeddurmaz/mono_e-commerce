package com.fastshop.repository;

import com.fastshop.domain.UserAdress;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserAdress entity.
 */
@Repository
public interface UserAdressRepository extends JpaRepository<UserAdress, Long>, JpaSpecificationExecutor<UserAdress> {
    @Query("select userAdress from UserAdress userAdress where userAdress.user.login = ?#{principal.username}")
    List<UserAdress> findByUserIsCurrentUser();

    default Optional<UserAdress> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<UserAdress> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<UserAdress> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct userAdress from UserAdress userAdress left join fetch userAdress.user",
        countQuery = "select count(distinct userAdress) from UserAdress userAdress"
    )
    Page<UserAdress> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct userAdress from UserAdress userAdress left join fetch userAdress.user")
    List<UserAdress> findAllWithToOneRelationships();

    @Query("select userAdress from UserAdress userAdress left join fetch userAdress.user where userAdress.id =:id")
    Optional<UserAdress> findOneWithToOneRelationships(@Param("id") Long id);
}
