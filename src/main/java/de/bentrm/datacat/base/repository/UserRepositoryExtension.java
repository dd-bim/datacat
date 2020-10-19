package de.bentrm.datacat.base.repository;

import de.bentrm.datacat.auth.domain.User;
import de.bentrm.datacat.auth.specification.UserSpecification;
import org.springframework.data.domain.Page;

public interface UserRepositoryExtension {

    long count(UserSpecification specification);

    Page<User> findAll(UserSpecification specification);

}
