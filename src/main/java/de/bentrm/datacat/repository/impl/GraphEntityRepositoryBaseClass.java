package de.bentrm.datacat.repository.impl;

import de.bentrm.datacat.query.*;
import de.bentrm.datacat.repository.GraphEntityRepository;
import de.bentrm.datacat.service.Specification;
import org.jetbrains.annotations.NotNull;
import org.neo4j.ogm.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.support.SimpleNeo4jRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
@Transactional(readOnly = true)
public class GraphEntityRepositoryBaseClass<T>
        extends SimpleNeo4jRepository<T, String>
        implements GraphEntityRepository<T> {

    Logger logger = LoggerFactory.getLogger(GraphEntityRepositoryBaseClass.class);

    protected final Class<T> entityType;
    protected final Session session;

    public GraphEntityRepositoryBaseClass(Class<T> domainClass, Session session) {
        super(domainClass, session);
        this.entityType = domainClass;
        this.session = session;

        logger.debug("Initializing custom repository for class {}", domainClass);
    }

    @NotNull
    @Override
    public Optional<T> findById(@NotNull String id) {
        FindByIdQuery<T> query = new FindByIdQuery<>(entityType, session, id);
        return query.execute();
    }

    @Override
    public Page<T> findAllById(Iterable<String> ids, Pageable pageable) {
        Iterable<T> results = new FindAllByIdQuery<>(entityType, session, ids, pageable).execute();
        List<T> content = new ArrayList<>();
        results.forEach(content::add);
        return PageableExecutionUtils.getPage(content, pageable, () -> new CountAllByIdQuery<>(entityType, session, ids).execute());
    }

    @Override
    public Page<T> findAll(Specification spec) {
        logger.debug("Current query spec: {}", spec);

        final CatalogItemListQuery.Builder<T> builder = new CatalogItemListQuery.Builder<>(entityType, session);
        final Optional<Pageable> pagingSpec = spec.getPageable();

        spec.getQuery().ifPresent(builder::setSearchTerm);

        final String queryScope = spec.getQueryScope().name();
        logger.debug("User-selected queryscope '{}'", queryScope);

        final FullTextIndex fullTextIndex = FullTextIndex.valueOf(queryScope);
        logger.debug("Determined full-text index to use: {}", fullTextIndex);
        builder.setFullTextIndex(fullTextIndex);

        spec.getEntityTypeIn().ifPresent(builder::setLabelsIn);
        spec.getEntityTypeNotIn().ifPresent(builder::setLabelsNotIn);
        spec.getIdIn().ifPresent(builder::setIdsIn);
        spec.getIdNotIn().ifPresent(builder::setIdsNotIn);
        pagingSpec.ifPresent(pageable -> {
            builder.setSkip(pageable.getOffset());
            builder.setLimit(pageable.getPageSize());
        });

        final CatalogItemListQuery<T> query = builder.build();
        final Iterable<T> results = query.execute();
        final int count = query.count();

        List<T> content = new ArrayList<>();
        results.forEach(content::add);
        Pageable pageable = pagingSpec.orElseGet(() -> PageRequest.of(0, Math.max(count, 10)));
        return PageableExecutionUtils.getPage(content, pageable, () -> count);
    }
}
