package de.bentrm.datacat.repository;

import de.bentrm.datacat.domain.User;
import org.springframework.data.domain.Page;

public interface UserRepositoryExtension {

    long count(UserSpecification specification);

    Page<User> findAll(UserSpecification specification);

}
