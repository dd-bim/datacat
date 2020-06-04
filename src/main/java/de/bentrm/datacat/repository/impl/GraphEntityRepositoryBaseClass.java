package de.bentrm.datacat.repository.impl;

import de.bentrm.datacat.query.*;
import de.bentrm.datacat.repository.GraphEntityRepository;
import de.bentrm.datacat.service.Specification;
import org.neo4j.ogm.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
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

    @Override
    public Optional<T> findById(String id) {
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
    public long count(FilterOptions filterOptions) {
        return new CountAllQuery<>(entityType, session, filterOptions).execute();
    }

    @Override
    public Page<T> findAll(Specification spec) {
        logger.debug("Current query spec: {}", spec);

        final ListQuery.QueryBuilder<T> builder = new ListQuery.QueryBuilder<>(entityType, session);
        final Pageable pageable = spec.getPageable();

        spec.getQuery().ifPresent(builder::setSearchTerm);

        final String queryScope = spec.getQueryScope().name();
        logger.debug("User-selected queryscope '{}'", queryScope);

        final FullTextIndex fullTextIndex = FullTextIndex.valueOf(queryScope);
        logger.debug("Determined full-text index to use: {}", fullTextIndex);
        builder.setFullTextIndex(fullTextIndex);

        spec.getEntityTypeIn().ifPresent(builder::setLabels);
        spec.getEntityTypeNotIn().ifPresent(builder::setExcludedLabels);
        spec.getIdIn().ifPresent(builder::setIds);
        spec.getIdNotIn().ifPresent(builder::setExcludedIds);
        builder.setSkip(pageable.getOffset());
        builder.setLimit(pageable.getPageSize());

        final ListQuery<T> query = builder.build();
        Iterable<T> results = query.execute();
        List<T> content = new ArrayList<>();
        results.forEach(content::add);
        return PageableExecutionUtils.getPage(content, pageable, this::count);
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        Iterable<T> results = new FindAllQuery<>(entityType, session, pageable).execute();
        List<T> content = new ArrayList<>();
        results.forEach(content::add);
        return PageableExecutionUtils.getPage(content, pageable, this::count);
    }

    @Override
    public Page<T> findAll(FilterOptions options, Pageable pageable) {
        Iterable<T> results = new FindAllQuery<>(entityType, session, pageable, options).execute();
        List<T> content = new ArrayList<>();
        results.forEach(content::add);
        return PageableExecutionUtils.getPage(content, pageable, () -> new CountAllQuery<>(entityType, session, options).execute());
    }

    @Override
    public Page<T> findAllByTerm(String term, Pageable pageable) {
        Iterable<T> results = new FindAllByTermQuery<>(entityType, session, term, pageable).execute();
        List<T> content = new ArrayList<>();
        results.forEach(content::add);
        return PageableExecutionUtils.getPage(content, pageable, () -> new CountByTermQuery<>(entityType, session, term).execute());
    }
}
