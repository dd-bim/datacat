package de.bentrm.datacat.catalog.service.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
    public final Neo4jTemplate neo4jTemplate;
    private final R repository;

    public AbstractQueryServiceImpl(Class<T> domainClass, Neo4jTemplate neo4jTemplate, R repository) {
        this.domainClass = domainClass;
        this.neo4jTemplate = neo4jTemplate;
        this.repository = repository;
    }

    @Override
    public @NotNull Optional<T> findById(@NotNull String id) {
        return repository.findById(id);
    }

    @Override
    public @NotNull Optional<T> findByIdWithDirectRelations(@NotNull String id) {
        return repository.findByIdWithDirectRelations(id);
    }

    @Override
    public @NotNull Optional<T> findByIdWithDirectRelations(@NotNull String id, @NotNull String type) {
        return repository.findByIdWithDirectRelations(id, type);
    }

    @Override
    public @NotNull List<T> findAllByIds(@NotNull List<String> ids) {
        Iterable<T> source = repository.findAllById(ids);
        return StreamSupport.stream(source.spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public @NotNull List<T> findAllEntitiesById(@NotNull List<String> ids) {
        Iterable<T> source = repository.findAllEntitiesById(ids);
        return StreamSupport.stream(source.spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public @NotNull Page<T> findAll(@NotNull QuerySpecification specification) {
        Collection<T> users;
        Pageable pageable;
        final Long count = count(specification);

        final Optional<Pageable> paged = specification.getPageable();
        if (paged.isPresent()) {
            pageable = paged.get();
            if (pageable.getSort().isUnsorted()) {
                users = neo4jTemplate.findAll(getQuery(specification, pageable), domainClass);
            } else {
                final Sort sort = pageable.getSort();
                final Sort.Direction direction = sort.get().findFirst().map(Sort.Order::getDirection).get();
                final String[] properties = sort.get().map(Sort.Order::getProperty).toArray(String[]::new);
                users = neo4jTemplate.findAll(getQuery(specification, pageable, direction, properties), domainClass);
            }
        } else {
            pageable = PageRequest.of(0, (int) Math.max(count, 10));
            users = neo4jTemplate.findAll(getQuery(specification, pageable), domainClass);
        }

        return PageableExecutionUtils.getPage(List.copyOf(users), pageable, () -> count);
    }

    @Override
    public @NotNull Long count(@NotNull QuerySpecification specification) {
        String query;
        if (specification.getFilters().isEmpty()) {
            query = "MATCH (n:" + domainClass.getSimpleName() + ") RETURN count(n)";
        } else {
            String whereClause = "WHERE " + String.join(" AND ", specification.getFilters());
            query = "MATCH (n:" + domainClass.getSimpleName() + ") " + whereClause + " RETURN count(n)";
        }
        return neo4jTemplate.count(query);
    }

    public String getQuery(QuerySpecification specification, Pageable pageable) {
        return (getQuery(specification, pageable, null, null));
    }

    public String getQuery(QuerySpecification specification, Pageable pageable, Sort.Direction direction,
            String[] properties) {
        String query;
        String sort = "";
        if (direction != null && properties != null) {
            List<String> prefixedProperties = Arrays.stream(properties)
                    .map(property -> "n.`" + property + "` " + direction.name()).collect(Collectors.toList());
            sort = " ORDER BY " + String.join(", ", prefixedProperties);
        }

        if (specification.getFilters().isEmpty()) {
            query = "MATCH (n:" + domainClass.getSimpleName() + ")" + sort + " RETURN n";
        } else {
            String whereClause = "WHERE " + String.join(" AND ", specification.getFilters());
            query = "MATCH (n:" + domainClass.getSimpleName() + ") " + whereClause + sort +" RETURN n";
        }
        query = query + " SKIP " + pageable.getOffset() + " LIMIT " + pageable.getPageSize();
        return query;
    }
}
