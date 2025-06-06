package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.base.specification.QuerySpecification;
import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.catalog.service.CatalogSearchService;
import de.bentrm.datacat.catalog.specification.CatalogRecordSpecification;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Validated
@Transactional(readOnly = true)
public class CatalogSearchServiceImpl implements CatalogSearchService {

    private final Neo4jTemplate neo4jTemplate;

    public CatalogSearchServiceImpl(Neo4jTemplate neo4jTemplate) {
        this.neo4jTemplate = neo4jTemplate;
    }

    @Override
    public Page<XtdObject> search(@NotNull CatalogRecordSpecification specification) {
        Collection<XtdObject> catalogRecords;
        Pageable pageable;
        final Long count = count(specification);

        final Optional<Pageable> paged = specification.getPageable();
        if (paged.isPresent()) {
            pageable = paged.get();
            if (pageable.getSort().isUnsorted()) {
                catalogRecords = neo4jTemplate.findAll(getQuery(specification, pageable), XtdObject.class);
            } else {
                final Sort sort = pageable.getSort();
                final Sort.Direction direction = sort.get().findFirst().map(Sort.Order::getDirection).get();
                final String[] properties = sort.get().map(Sort.Order::getProperty).toArray(String[]::new);
                catalogRecords = neo4jTemplate.findAll(getQuery(specification, pageable, direction, properties),
                        XtdObject.class);
            }
        } else {
            pageable = PageRequest.of(0, (int) Math.max(count, 10));
            catalogRecords = neo4jTemplate.findAll(getQuery(specification, pageable), XtdObject.class);
        }

        return PageableExecutionUtils.getPage(List.copyOf(catalogRecords), pageable, () -> count);
    }

    @Override
    public Long count(@NotNull CatalogRecordSpecification specification) {
        String query;
        if (specification.getFilters().isEmpty()) {
            query = "MATCH (n:XtdObject) RETURN count(n)";
        } else {
            String whereClause = "WHERE " + String.join(" AND ", specification.getFilters());
            query = "MATCH (n:XtdObject) " + whereClause + " RETURN count(n)";
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
            query = "MATCH (n:XtdObject)" + sort + " RETURN n";
        } else {
            String whereClause = "WHERE " + String.join(" AND ", specification.getFilters());
            query = "MATCH (n:XtdObject) " + whereClause + sort + " RETURN n";
        }
        query = query + " SKIP " + pageable.getOffset() + " LIMIT " + pageable.getPageSize();
        return query;
    }
}
