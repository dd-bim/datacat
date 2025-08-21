package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.domain.XtdInterval;
import de.bentrm.datacat.catalog.domain.XtdValueList;
import de.bentrm.datacat.catalog.repository.IntervalRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.IntervalRecordService;
import de.bentrm.datacat.catalog.service.ValueListRecordService;
import de.bentrm.datacat.catalog.service.dto.Relationships.IntervallMaxMinDtoProjection;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Slf4j
@Service
@Validated
@Transactional(readOnly = true)
public class IntervalRecordServiceImpl
        extends AbstractSimpleRecordServiceImpl<XtdInterval, IntervalRepository>
        implements IntervalRecordService {

            @Autowired
            private ValueListRecordService valueListRecordService;

    public IntervalRecordServiceImpl(Neo4jTemplate neo4jTemplate,
                                     IntervalRepository repository,
                                     CatalogCleanupService cleanupService) {
        super(XtdInterval.class, neo4jTemplate, repository, cleanupService);
    }

    @Override
    public @NotNull Page<XtdInterval> findAll(@NotNull de.bentrm.datacat.base.specification.QuerySpecification specification) {
        // Use optimized query when no complex filters are applied
        if (isSimpleQuery(specification)) {
            List<XtdInterval> intervals = findIntervalsWithRelations(specification);
            Pageable pageable = specification.getPageable().orElse(PageRequest.of(0, 20));
            return PageableExecutionUtils.getPage(intervals, pageable, 
                () -> getRepository().count());
        }
        // Fallback to default implementation for complex queries
        return super.findAll(specification);
    }

    private boolean isSimpleQuery(de.bentrm.datacat.base.specification.QuerySpecification specification) {
        // Consider it simple if there are no filters, only pagination and sorting
        return specification.getFilters() == null || specification.getFilters().isEmpty();
    }

    private List<XtdInterval> findIntervalsWithRelations(de.bentrm.datacat.base.specification.QuerySpecification specification) {
        Pageable pageable = specification.getPageable().orElse(PageRequest.of(0, 20));
        String query = buildOptimizedIntervalQuery(pageable);
        return getNeo4jTemplate().findAll(query, XtdInterval.class);
    }

    private String buildOptimizedIntervalQuery(Pageable pageable) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("MATCH (i:XtdInterval) ");
        
        // Add optional matches for commonly used relations
        queryBuilder.append("OPTIONAL MATCH (i)<-[:INTERVALS]-(properties:XtdProperty) ");
        queryBuilder.append("OPTIONAL MATCH (i)-[:MINIMUM]->(minimum:XtdValueList) ");
        queryBuilder.append("OPTIONAL MATCH (i)-[:MAXIMUM]->(maximum:XtdValueList) ");
        queryBuilder.append("OPTIONAL MATCH (i)-[:NAMES]->(names:XtdMultiLanguageText) ");
        queryBuilder.append("OPTIONAL MATCH (i)-[:COMMENTS]->(comments:XtdMultiLanguageText) ");
        
        queryBuilder.append("RETURN i, ");
        queryBuilder.append("collect(DISTINCT properties) as properties, ");
        queryBuilder.append("collect(DISTINCT minimum) as minimum, ");
        queryBuilder.append("collect(DISTINCT maximum) as maximum, ");
        queryBuilder.append("collect(DISTINCT names) as names, ");
        queryBuilder.append("collect(DISTINCT comments) as comments ");
        
        // Add sorting if specified
        if (pageable.getSort().isSorted()) {
            queryBuilder.append("ORDER BY ");
            String sortClause = pageable.getSort().stream()
                    .map(order -> "i." + order.getProperty() + " " + order.getDirection())
                    .collect(Collectors.joining(", "));
            queryBuilder.append(sortClause).append(" ");
        }
        
        // Add pagination
        queryBuilder.append("SKIP ").append(pageable.getOffset()).append(" ");
        queryBuilder.append("LIMIT ").append(pageable.getPageSize());
        
        return queryBuilder.toString();
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.Interval;
    }

    @Override
    public Optional<XtdValueList> getMinimum(XtdInterval interval) {
        Assert.notNull(interval.getId(), "Interval must be persistent.");
        final String valueListId = getRepository().findMinValueListIdAssignedToInterval(interval.getId());
        if (valueListId == null) {
            return null;
        }
        return valueListRecordService.findByIdWithDirectRelations(valueListId);
    }

    @Override
    public Optional<XtdValueList> getMaximum(XtdInterval interval) {
        Assert.notNull(interval.getId(), "Interval must be persistent.");
        final String valueListId = getRepository().findMaxValueListIdAssignedToInterval(interval.getId());
        if (valueListId == null) {
            return null;
        }
        return valueListRecordService.findByIdWithDirectRelations(valueListId);
    }

    @Transactional
    @Override
    public @NotNull XtdInterval setRelatedRecords(@NotBlank String recordId,
                                                    @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {

        final XtdInterval interval = getRepository().findByIdWithDirectRelations(recordId).orElseThrow(() -> new IllegalArgumentException("No record with id " + recordId + " found."));

        switch (relationType) {
            case Minimum -> {
                if (interval.getMinimum() != null) {
                    throw new IllegalArgumentException("Minimum already set.");
                } else if (relatedRecordIds.size() > 1) {
                    throw new IllegalArgumentException("Maximum one value list must be provided.");
                } else {
                    final XtdValueList valueList = valueListRecordService.findByIdWithDirectRelations(relatedRecordIds.get(0)).orElseThrow(() -> new IllegalArgumentException("No record with id " + relatedRecordIds.get(0) + " found."));
                    interval.setMinimum(valueList);
                }
                }
            case Maximum -> {
                if (interval.getMaximum() != null) {
                    throw new IllegalArgumentException("Maximum already set.");
                } else if (relatedRecordIds.size() > 1) {
                    throw new IllegalArgumentException("Maximum one value list must be provided.");
                } else {
                    final XtdValueList valueList = valueListRecordService.findByIdWithDirectRelations(relatedRecordIds.get(0)).orElseThrow(() -> new IllegalArgumentException("No record with id " + relatedRecordIds.get(0) + " found."));
                    interval.setMaximum(valueList);
                }
                }
            default -> log.error("Unsupported relation type: {}", relationType);
        }
        
        neo4jTemplate.saveAs(interval, IntervallMaxMinDtoProjection.class);
        log.trace("Updated interval: {}", interval);
        return interval;
    }
}
