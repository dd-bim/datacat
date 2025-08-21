package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.domain.XtdUnit;
import de.bentrm.datacat.catalog.domain.XtdValueList;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdRational;
import de.bentrm.datacat.catalog.domain.XtdDimension;
import de.bentrm.datacat.catalog.domain.XtdMultiLanguageText;
import de.bentrm.datacat.catalog.repository.UnitRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.UnitRecordService;
import de.bentrm.datacat.catalog.service.ValueListRecordService;
import de.bentrm.datacat.catalog.service.dto.Relationships.DimensionDtoProjection;
import de.bentrm.datacat.catalog.service.ConceptRecordService;
import de.bentrm.datacat.catalog.service.DimensionRecordService;
import de.bentrm.datacat.catalog.service.MultiLanguageTextRecordService;
import de.bentrm.datacat.catalog.service.PropertyRecordService;
import de.bentrm.datacat.catalog.service.RationalRecordService;
import lombok.extern.slf4j.Slf4j;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.data.support.PageableExecutionUtils;

@Slf4j
@Service
@Validated
@Transactional(readOnly = true)
public class UnitRecordServiceImpl extends AbstractSimpleRecordServiceImpl<XtdUnit, UnitRepository>
        implements UnitRecordService {

    @Autowired
    private PropertyRecordService propertyRecordService;

    @Autowired
    private DimensionRecordService dimensionRecordService;

    @Autowired
    private MultiLanguageTextRecordService multiLanguageTextRecordService;

    @Autowired
    private RationalRecordService rationalRecordService;

    @Autowired
    private ConceptRecordService conceptRecordService;

    @Autowired
    @Lazy
    private ValueListRecordService valueListRecordService;

    public UnitRecordServiceImpl(Neo4jTemplate neo4jTemplate, UnitRepository repository,
            CatalogCleanupService cleanupService) {
        super(XtdUnit.class, neo4jTemplate, repository, cleanupService);
    }

    @Override
    public @NotNull Page<XtdUnit> findAll(@NotNull de.bentrm.datacat.base.specification.QuerySpecification specification) {
        // Use optimized query when no complex filters are applied
        if (isSimpleQuery(specification)) {
            List<XtdUnit> units = findUnitsWithRelations(specification);
            Pageable pageable = specification.getPageable().orElse(PageRequest.of(0, 20));
            return PageableExecutionUtils.getPage(units, pageable, 
                () -> getRepository().count());
        }
        // Fallback to default implementation for complex queries
        return super.findAll(specification);
    }

    private boolean isSimpleQuery(de.bentrm.datacat.base.specification.QuerySpecification specification) {
        // Consider it simple if there are no filters, only pagination and sorting
        return specification.getFilters() == null || specification.getFilters().isEmpty();
    }

    private List<XtdUnit> findUnitsWithRelations(de.bentrm.datacat.base.specification.QuerySpecification specification) {
        Pageable pageable = specification.getPageable().orElse(PageRequest.of(0, 20));
        String query = buildOptimizedUnitQuery(pageable);
        return getNeo4jTemplate().findAll(query, XtdUnit.class);
    }

    private String buildOptimizedUnitQuery(Pageable pageable) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("MATCH (u:XtdUnit) ");
        
        // Add optional matches for commonly used relations
        queryBuilder.append("OPTIONAL MATCH (u)<-[:ASSIGNS_UNIT]-(properties:XtdProperty) ");
        queryBuilder.append("OPTIONAL MATCH (u)-[:DIMENSIONS]->(dimensions:XtdDimension) ");
        queryBuilder.append("OPTIONAL MATCH (u)-[:NAMES]->(names:XtdMultiLanguageText) ");
        queryBuilder.append("OPTIONAL MATCH (u)-[:COMMENTS]->(comments:XtdMultiLanguageText) ");
        
        queryBuilder.append("RETURN u, ");
        queryBuilder.append("collect(DISTINCT properties) as properties, ");
        queryBuilder.append("collect(DISTINCT dimensions) as dimensions, ");
        queryBuilder.append("collect(DISTINCT names) as names, ");
        queryBuilder.append("collect(DISTINCT comments) as comments ");
        
        // Add sorting if specified
        if (pageable.getSort().isSorted()) {
            queryBuilder.append("ORDER BY ");
            String sortClause = pageable.getSort().stream()
                    .map(order -> "u." + order.getProperty() + " " + order.getDirection())
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
        return CatalogRecordType.Unit;
    }

    @Override
    public List<XtdProperty> getProperties(XtdUnit unit) {
        Assert.notNull(unit.getId(), "Unit must be persistent.");
        final List<String> propertyIds = getRepository().findAllPropertyIdsAssigningUnit(unit.getId());
        final Iterable<XtdProperty> properties = propertyRecordService.findAllEntitiesById(propertyIds);

        return StreamSupport.stream(properties.spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public Optional<XtdDimension> getDimension(XtdUnit unit) {
        Assert.notNull(unit.getId(), "Unit must be persistent.");
        final String dimensionId = getRepository().findDimensionIdAssignedToUnit(unit.getId());
        if (dimensionId == null) {
            return null;
        }
        final Optional<XtdDimension> dimension = dimensionRecordService.findByIdWithDirectRelations(dimensionId);

        return dimension;
    }

    @Override
    public Optional<XtdMultiLanguageText> getSymbol(@NotNull XtdUnit unit) {
        Assert.notNull(unit.getId(), "Unit must be persistent.");
        final String symbolId = getRepository().findSymbolIdAssignedToUnit(unit.getId());
        if (symbolId == null) {
            return Optional.empty();
        }
        final Optional<XtdMultiLanguageText> symbol = multiLanguageTextRecordService
                .findByIdWithDirectRelations(symbolId);
        return symbol;
    }

    @Override
    public Optional<XtdRational> getCoefficient(@NotNull XtdUnit unit) {
        Assert.notNull(unit.getId(), "Unit must be persistent.");
        final String coefficientId = getRepository().findCoefficientIdAssignedToUnit(unit.getId());
        if (coefficientId == null) {
            return Optional.empty();
        }
        final Optional<XtdRational> coefficient = rationalRecordService.findByIdWithDirectRelations(coefficientId);
        return coefficient;
    }

    @Override
    public Optional<XtdRational> getOffset(@NotNull XtdUnit unit) {
        Assert.notNull(unit.getId(), "Unit must be persistent.");
        final String offsetId = getRepository().findOffsetIdAssignedToUnit(unit.getId());
        if (offsetId == null) {
            return Optional.empty();
        }
        final Optional<XtdRational> offset = rationalRecordService.findByIdWithDirectRelations(offsetId);
        return offset;
    }

    @Override
    public Optional<List<XtdValueList>> getValueLists(@NotNull XtdUnit unit) {
        Assert.notNull(unit.getId(), "Unit must be persistent.");
        final List<String> valueListId = getRepository().findValueListIdsAssigningUnit(unit.getId());
        final Iterable<XtdValueList> valueLists = valueListRecordService.findAllEntitiesById(valueListId);
        if (!valueLists.iterator().hasNext()) {
            return Optional.empty();
        } else {
            return Optional.of(StreamSupport.stream(valueLists.spliterator(), false).collect(Collectors.toList()));
        }
    }

    @Transactional
    @Override
    public @NotNull XtdUnit setRelatedRecords(@NotBlank String recordId,
            @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {

        final XtdUnit unit = getRepository().findByIdWithDirectRelations(recordId)
                .orElseThrow(() -> new IllegalArgumentException("No record with id " + recordId + " found."));

        switch (relationType) {
        case Dimension -> {
            if (unit.getDimension() != null) {
                throw new IllegalArgumentException("Unit already has a dimension.");
            } else if (relatedRecordIds.size() != 1) {
                throw new IllegalArgumentException("Exactly one dimension must be assigned to a unit.");
            } else {
                final XtdDimension dimension = dimensionRecordService
                        .findByIdWithDirectRelations(relatedRecordIds.get(0))
                        .orElseThrow(() -> new IllegalArgumentException(
                                "No record with id " + relatedRecordIds.get(0) + " found."));
                unit.setDimension(dimension);
            }
            neo4jTemplate.saveAs(unit, DimensionDtoProjection.class);
        }
        default -> conceptRecordService.setRelatedRecords(recordId, relatedRecordIds, relationType);
        }

        log.trace("Updated relationship: {}", unit);
        return unit;
    }
}
