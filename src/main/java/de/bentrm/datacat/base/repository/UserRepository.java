package de.bentrm.datacat.base.repository;

import de.bentrm.datacat.auth.domain.User;

import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends EntityRepository<User> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("""
            MATCH (n:User {username: $username})
            RETURN n""")
    Optional<User> findByUsername(String username);

}
