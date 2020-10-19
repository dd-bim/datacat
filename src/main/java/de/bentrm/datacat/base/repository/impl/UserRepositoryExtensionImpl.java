package de.bentrm.datacat.base.repository.impl;

import de.bentrm.datacat.auth.domain.User;
import de.bentrm.datacat.auth.specification.UserSpecification;
import de.bentrm.datacat.base.repository.UserRepositoryExtension;
import org.neo4j.ogm.cypher.query.Pagination;
import org.neo4j.ogm.cypher.query.SortOrder;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.support.PageableExecutionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class UserRepositoryExtensionImpl implements UserRepositoryExtension {

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

        final Optional<Pageable> paged = specification.getPageable();
        if (paged.isPresent()) {
            pageable = paged.get();
            final Pagination pagination = new Pagination(pageable.getPageNumber(), pageable.getPageSize());

            if (pageable.getSort().isUnsorted()) {
                users = session.loadAll(User.class, specification.getFilters(), pagination);
            } else {
                final Sort sort = pageable.getSort();
                final Sort.Direction direction = sort.get().findFirst().map(Sort.Order::getDirection).get();
                final String[] properties = sort.get().map(Sort.Order::getProperty).toArray(String[]::new);
                final SortOrder sortOrder = new SortOrder(SortOrder.Direction.valueOf(direction.name()), properties);
                users = session.loadAll(User.class, specification.getFilters(), sortOrder, pagination);
            }
        } else {
            pageable = PageRequest.of(0, (int) Math.max(count, 10));
            users = session.loadAll(User.class, specification.getFilters());
        }

        return PageableExecutionUtils.getPage(List.copyOf(users), pageable, () -> count);
    }
}
