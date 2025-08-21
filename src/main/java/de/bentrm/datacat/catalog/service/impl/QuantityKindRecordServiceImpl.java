package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.domain.XtdDimension;
import de.bentrm.datacat.catalog.domain.XtdUnit;
import de.bentrm.datacat.catalog.domain.XtdQuantityKind;
import de.bentrm.datacat.catalog.repository.QuantityKindRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.ConceptRecordService;
import de.bentrm.datacat.catalog.service.DimensionRecordService;
import de.bentrm.datacat.catalog.service.QuantityKindRecordService;
import de.bentrm.datacat.catalog.service.UnitRecordService;
import de.bentrm.datacat.catalog.service.dto.Relationships.DimensionDtoProjection;
import de.bentrm.datacat.catalog.service.dto.Relationships.UnitsDtoProjection;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@Validated
@Transactional(readOnly = true)
public class QuantityKindRecordServiceImpl extends
        AbstractSimpleRecordServiceImpl<XtdQuantityKind, QuantityKindRepository> implements QuantityKindRecordService {

    @Autowired
    private UnitRecordService unitRecordService;

    @Autowired
    private DimensionRecordService dimensionRecordService;

    @Autowired
    private ConceptRecordService conceptRecordService;

    public QuantityKindRecordServiceImpl(Neo4jTemplate neo4jTemplate, QuantityKindRepository repository,
            CatalogCleanupService cleanupService) {
        super(XtdQuantityKind.class, neo4jTemplate, repository, cleanupService);
    }

    @Override
    public @NotNull Page<XtdQuantityKind> findAll(@NotNull de.bentrm.datacat.base.specification.QuerySpecification specification) {
        // Use optimized query when no complex filters are applied
        if (isSimpleQuery(specification)) {
            List<XtdQuantityKind> quantityKinds = findQuantityKindsWithRelations(specification);
            Pageable pageable = specification.getPageable().orElse(PageRequest.of(0, 20));
            return PageableExecutionUtils.getPage(quantityKinds, pageable, 
                () -> getRepository().count());
        }
        // Fallback to default implementation for complex queries
        return super.findAll(specification);
    }

    private boolean isSimpleQuery(de.bentrm.datacat.base.specification.QuerySpecification specification) {
        // Consider it simple if there are no filters, only pagination and sorting
        return specification.getFilters() == null || specification.getFilters().isEmpty();
    }

    private List<XtdQuantityKind> findQuantityKindsWithRelations(de.bentrm.datacat.base.specification.QuerySpecification specification) {
        Pageable pageable = specification.getPageable().orElse(PageRequest.of(0, 20));
        String query = buildOptimizedQuantityKindQuery(pageable);
        return getNeo4jTemplate().findAll(query, XtdQuantityKind.class);
    }

    private String buildOptimizedQuantityKindQuery(Pageable pageable) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("MATCH (qk:XtdQuantityKind) ");
        
        // Add optional matches for commonly used relations
        queryBuilder.append("OPTIONAL MATCH (qk)<-[:QUANTITY_KINDS]-(properties:XtdProperty) ");
        queryBuilder.append("OPTIONAL MATCH (qk)-[:DIMENSION]->(dimension:XtdDimension) ");
        queryBuilder.append("OPTIONAL MATCH (qk)-[:UNITS]->(units:XtdUnit) ");
        queryBuilder.append("OPTIONAL MATCH (qk)-[:NAMES]->(names:XtdMultiLanguageText) ");
        queryBuilder.append("OPTIONAL MATCH (qk)-[:COMMENTS]->(comments:XtdMultiLanguageText) ");
        
        queryBuilder.append("RETURN qk, ");
        queryBuilder.append("collect(DISTINCT properties) as properties, ");
        queryBuilder.append("collect(DISTINCT dimension) as dimension, ");
        queryBuilder.append("collect(DISTINCT units) as units, ");
        queryBuilder.append("collect(DISTINCT names) as names, ");
        queryBuilder.append("collect(DISTINCT comments) as comments ");
        
        // Add sorting if specified
        if (pageable.getSort().isSorted()) {
            queryBuilder.append("ORDER BY ");
            String sortClause = pageable.getSort().stream()
                    .map(order -> "qk." + order.getProperty() + " " + order.getDirection())
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
        return CatalogRecordType.QuantityKind;
    }

    @Override
    public List<XtdUnit> getUnits(XtdQuantityKind quantityKind) {
        Assert.notNull(quantityKind, "QuantityKind must be persistent.");
        final List<String> unitIds = getRepository().findAllUnitIdsAssignedToQuantityKind(quantityKind.getId());
        final Iterable<XtdUnit> units = unitRecordService.findAllEntitiesById(unitIds);

        return StreamSupport.stream(units.spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public Optional<XtdDimension> getDimension(XtdQuantityKind quantityKind) {
        Assert.notNull(quantityKind, "QuantityKind must be persistent.");
        final String dimensionId = getRepository().findDimensionIdAssignedToQuantityKind(quantityKind.getId());
        if (dimensionId == null) {
            return null;
        }
        final Optional<XtdDimension> dimension = dimensionRecordService.findByIdWithDirectRelations(dimensionId);
        return dimension;
    }

    @Transactional
    @Override
    public @NotNull XtdQuantityKind setRelatedRecords(@NotBlank String recordId,
            @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {

        final XtdQuantityKind quantityKind = getRepository().findById(recordId).orElseThrow(() -> new IllegalArgumentException("No record with id " + recordId + " found."));

        switch (relationType) {
        case Units -> {
            final Iterable<XtdUnit> units = unitRecordService.findAllEntitiesById(relatedRecordIds);
            final List<XtdUnit> relatedUnits = StreamSupport.stream(units.spliterator(), false)
                    .collect(Collectors.toList());

            quantityKind.getUnits().clear();
            quantityKind.getUnits().addAll(relatedUnits);
            neo4jTemplate.saveAs(quantityKind, UnitsDtoProjection.class);
        }
        case Dimension -> {
            if (quantityKind.getDimension() != null) {
                throw new IllegalArgumentException("QuantityKind already has a dimension assigned.");
            } else if (relatedRecordIds.size() != 1) {
                throw new IllegalArgumentException("Exactly one dimension must be assigned.");
            } else {
                final XtdDimension dimension = dimensionRecordService
                        .findByIdWithDirectRelations(relatedRecordIds.get(0)).orElseThrow(() -> new IllegalArgumentException("No record with id " + relatedRecordIds.get(0) + " found."));
                quantityKind.setDimension(dimension);
            }
            neo4jTemplate.saveAs(quantityKind, DimensionDtoProjection.class);
        }
        default -> conceptRecordService.setRelatedRecords(recordId, relatedRecordIds, relationType);
        }

        log.trace("Updated relationship: {}", quantityKind);
        return quantityKind;
    }
}
