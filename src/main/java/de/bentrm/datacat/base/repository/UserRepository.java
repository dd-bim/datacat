package de.bentrm.datacat.base.repository;

import de.bentrm.datacat.auth.domain.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends GraphEntityRepository<User>, UserRepositoryExtension {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

}
