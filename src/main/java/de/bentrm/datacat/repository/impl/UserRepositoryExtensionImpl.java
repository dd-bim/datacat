package de.bentrm.datacat.repository.impl;

import de.bentrm.datacat.domain.User;
import de.bentrm.datacat.repository.UserRepositoryExtension;
import de.bentrm.datacat.repository.UserSpecification;
import org.neo4j.ogm.cypher.query.Pagination;
import org.neo4j.ogm.session.Session;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class UserRepositoryExtensionImpl implements UserRepositoryExtension {

    @Autowired
    private Logger logger;

    @Autowired
    private Session session;

    @Override
    public long count(UserSpecification specification) {
        return session.count(User.class, specification.getFilters());
    }

    @Override
    public Page<User> findAll(UserSpecification specification) {
        Collection<User> users;
        Pageable pageable;
        final long count = count(specification);

        specification.getFilters().forEach(filter -> {
            logger.info(filter.toString());
        });

        final Optional<Pageable> optionalPageable = specification.getPageable();
        if (optionalPageable.isPresent()) {
            pageable = optionalPageable.get();
            Pagination pagination = new Pagination(pageable.getPageNumber(), pageable.getPageSize());
            users = session.loadAll(User.class, specification.getFilters(), pagination);
        } else {
            pageable = PageRequest.of(0, (int) Math.max(count, 10));
            users = session.loadAll(User.class, specification.getFilters());
        }

        return PageableExecutionUtils.getPage(List.copyOf(users), pageable, () -> count);
    }
}
