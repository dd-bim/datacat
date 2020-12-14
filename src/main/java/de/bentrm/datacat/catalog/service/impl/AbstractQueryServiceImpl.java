package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.base.domain.Entity;
import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.base.specification.QuerySpecification;
import de.bentrm.datacat.catalog.service.QueryService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.cypher.query.Pagination;
import org.neo4j.ogm.cypher.query.SortOrder;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.support.PageableExecutionUtils;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Getter(AccessLevel.PROTECTED)
public abstract class AbstractQueryServiceImpl<T extends Entity> implements QueryService<T> {

    private final Class<T> domainClass;
    private final SessionFactory sessionFactory;
    private final EntityRepository<T> repository;

    public AbstractQueryServiceImpl(Class<T> domainClass,
                                    SessionFactory sessionFactory,
                                    EntityRepository<T> repository) {
        this.domainClass = domainClass;
        this.sessionFactory = sessionFactory;
        this.repository = repository;
    }

    public @NotNull Optional<T> findById(@NotNull String id) {
        return repository.findById(id);
    }

    public @NotNull List<T> findAllByIds(@NotNull List<String> ids) {
        Iterable<T> source = repository.findAllById(ids);
        return StreamSupport
                .stream(source.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public @NotNull Page<T> findAll(@NotNull QuerySpecification specification) {
        Collection<T> users;
        Pageable pageable;
        final long count = count(specification);
        final Session session = sessionFactory.openSession();

        final Optional<Pageable> paged = specification.getPageable();
        if (paged.isPresent()) {
            pageable = paged.get();
            final Pagination pagination = new Pagination(pageable.getPageNumber(), pageable.getPageSize());

            if (pageable.getSort().isUnsorted()) {
                users = session.loadAll(domainClass, specification.getFilters(), pagination);
            } else {
                final Sort sort = pageable.getSort();
                final Sort.Direction direction = sort.get().findFirst().map(Sort.Order::getDirection).get();
                final String[] properties = sort.get().map(Sort.Order::getProperty).toArray(String[]::new);
                final SortOrder sortOrder = new SortOrder(SortOrder.Direction.valueOf(direction.name()), properties);
                users = session.loadAll(domainClass, specification.getFilters(), sortOrder, pagination);
            }
        } else {
            pageable = PageRequest.of(0, (int) Math.max(count, 10));
            users = session.loadAll(domainClass, specification.getFilters());
        }

        return PageableExecutionUtils.getPage(List.copyOf(users), pageable, () -> count);
    }

    @Override
    public @NotNull long count(@NotNull QuerySpecification specification) {
        final Session session = sessionFactory.openSession();
        return session.count(domainClass, specification.getFilters());
    }
}
