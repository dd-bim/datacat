package de.bentrm.datacat.repository.impl;

import de.bentrm.datacat.repository.GraphEntityRepository;
import de.bentrm.datacat.specification.QuerySpecification;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.cypher.query.Pagination;
import org.neo4j.ogm.cypher.query.SortOrder;
import org.neo4j.ogm.session.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.neo4j.repository.support.SimpleNeo4jRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@NoRepositoryBean
@Transactional(readOnly = true)
public class GraphEntityRepositoryBaseClass<T>
        extends SimpleNeo4jRepository<T, String>
        implements GraphEntityRepository<T> {

    protected final Class<T> entityType;
    protected final Session session;

    public GraphEntityRepositoryBaseClass(Class<T> domainClass, Session session) {
        super(domainClass, session);
        this.entityType = domainClass;
        this.session = session;
    }

    @Override
    public long count(QuerySpecification specification) {
        return session.count(entityType, specification.getFilters());
    }

    @Override
    public Page<T> findAll(QuerySpecification specification) {
        Collection<T> users;
        Pageable pageable;
        final long count = count(specification);

        final Optional<Pageable> paged = specification.getPageable();
        if (paged.isPresent()) {
            pageable = paged.get();
            final Pagination pagination = new Pagination(pageable.getPageNumber(), pageable.getPageSize());

            if (pageable.getSort().isUnsorted()) {
                users = session.loadAll(entityType, specification.getFilters(), pagination);
            } else {
                final Sort sort = pageable.getSort();
                final Sort.Direction direction = sort.get().findFirst().map(Sort.Order::getDirection).get();
                final String[] properties = sort.get().map(Sort.Order::getProperty).toArray(String[]::new);
                final SortOrder sortOrder = new SortOrder(SortOrder.Direction.valueOf(direction.name()), properties);
                users = session.loadAll(entityType, specification.getFilters(), sortOrder, pagination);
            }
        } else {
            pageable = PageRequest.of(0, (int) Math.max(count, 10));
            users = session.loadAll(entityType, specification.getFilters());
        }

        return PageableExecutionUtils.getPage(List.copyOf(users), pageable, () -> count);
    }
}
