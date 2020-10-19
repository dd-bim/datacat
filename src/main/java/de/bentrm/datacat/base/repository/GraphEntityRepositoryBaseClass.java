package de.bentrm.datacat.base.repository;

import de.bentrm.datacat.base.specification.QuerySpecification;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.cypher.query.Pagination;
import org.neo4j.ogm.cypher.query.SortOrder;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.neo4j.repository.support.SimpleNeo4jRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
        Collection<T> items;
        Pageable pageable;
        final long count = count(specification);

        final Optional<Pageable> paged = specification.getPageable();
        if (paged.isPresent()) {
            pageable = paged.get();
            final Pagination pagination = new Pagination(pageable.getPageNumber(), pageable.getPageSize());

            if (pageable.getSort().isUnsorted()) {
                items = session.loadAll(entityType, specification.getFilters(), pagination);
            } else {
                final Sort sort = pageable.getSort();
                final Sort.Direction direction = sort.get().findFirst().map(Sort.Order::getDirection).get();
                final String[] properties = sort.get().map(Sort.Order::getProperty).toArray(String[]::new);
                final SortOrder sortOrder = new SortOrder(SortOrder.Direction.valueOf(direction.name()), properties);
                items = session.loadAll(entityType, specification.getFilters(), sortOrder, pagination, 1);



                log.debug("Target class: {}", AopProxyUtils.ultimateTargetClass(session));

                log.debug("Statements: {}", session);
            }
        } else {
            pageable = PageRequest.of(0, (int) Math.max(count, 10));
            items = session.loadAll(entityType, specification.getFilters());
        }

        return PageableExecutionUtils.getPage(List.copyOf(items), pageable, () -> count);
    }

    @Override
    public List<List<String>> getHierarchyPaths(List<String> rootNodeIds, int depth) {
        final String query = """               
                MATCH p=(a)-[*0..10]->(c:XtdRoot)              
                WHERE a.id IN $rootIds AND NOT (c)-->(:XtdRelationship)-->(:XtdRoot)                                    
                WITH [x IN nodes(p) | x.id] AS paths
                RETURN DISTINCT paths    
                """;
        final Map<String, Object> parameters = Map.of(
                "rootIds", rootNodeIds,
                "depth", depth
        );
        final Result result = session.query(query, parameters);
        final List<List<String>> paths = new ArrayList<>();
        result.forEach(x -> {
            final List<String> arr = List.of((String[]) x.get("paths"));
            paths.add(arr);
        });
        return paths;
    }
}
