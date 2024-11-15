package de.bentrm.datacat.catalog.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.neo4j.ogm.cypher.query.Pagination;
import org.neo4j.ogm.cypher.query.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.data.support.PageableExecutionUtils;

import de.bentrm.datacat.base.domain.Entity;
import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.base.specification.QuerySpecification;
import de.bentrm.datacat.catalog.service.QueryService;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter(AccessLevel.PROTECTED)
public abstract class AbstractQueryServiceImpl<T extends Entity, R extends EntityRepository<T>>
        implements QueryService<T> {

    private final Class<T> domainClass;
    private final Neo4jTemplate neo4jTemplate;
    private final R repository;

    public AbstractQueryServiceImpl(Class<T> domainClass,
                                    Neo4jTemplate neo4jTemplate,
                                    R repository) {
        this.domainClass = domainClass;
        this.neo4jTemplate = neo4jTemplate;
        this.repository = repository;
    }

    @Override
    public @NotNull Optional<T> findById(@NotNull String id) {
        return repository.findById(id);
    }

    @Override
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
        // return session.count(domainClass, specification.getFilters());
        return neo4jTemplate.count(domainClass, specification.getFilters());
    }
}
