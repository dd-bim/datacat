package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.catalog.service.CatalogSearchService;
import de.bentrm.datacat.catalog.specification.CatalogRecordSpecification;
import lombok.extern.slf4j.Slf4j;

import org.neo4j.ogm.cypher.query.Pagination;
import org.neo4j.ogm.cypher.query.SortOrder;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@Validated
@Transactional(readOnly = true)
public class CatalogSearchImpl implements CatalogSearchService {

    private final SessionFactory sessionFactory;

    public CatalogSearchImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Page<XtdObject> search(CatalogRecordSpecification specification) {
        Iterable<XtdObject> catalogRecords;
        Pageable pageable;
        final Session session = sessionFactory.openSession();

        final long count = session.count(XtdObject.class, specification.getFilters());

        final Optional<Pageable> paged = specification.getPageable();
        if (paged.isPresent()) {
            pageable = paged.get();
            final Pagination pagination = new Pagination(pageable.getPageNumber(), pageable.getPageSize());

            if (pageable.getSort().isUnsorted()) {
                catalogRecords = session.loadAll(XtdObject.class, specification.getFilters(), pagination);
            } else {
                final Sort sort = pageable.getSort();
                final Sort.Direction direction = sort.get().findFirst().map(Sort.Order::getDirection).get();
                final String[] properties = sort.get().map(Sort.Order::getProperty).toArray(String[]::new);
                final SortOrder sortOrder = new SortOrder(SortOrder.Direction.valueOf(direction.name()), properties);
                catalogRecords = session.loadAll(XtdObject.class, specification.getFilters(), sortOrder, pagination);
            }
        } else {
            pageable = PageRequest.of(0, (int) Math.max(count, 10));
            catalogRecords = session.loadAll(XtdObject.class, specification.getFilters());
        }

        List<XtdObject> content = StreamSupport.stream(catalogRecords.spliterator(), false).collect(Collectors.toList());

        return PageableExecutionUtils.getPage(content, pageable, () -> count);
    }

    @Override
    public long count(CatalogRecordSpecification specification) {
        final Session session = sessionFactory.openSession();
        return session.count(XtdObject.class, specification.getFilters());
    }
}
