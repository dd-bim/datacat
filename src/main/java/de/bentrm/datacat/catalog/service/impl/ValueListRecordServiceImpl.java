package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.domain.XtdLanguage;
import de.bentrm.datacat.catalog.domain.XtdOrderedValue;
import de.bentrm.datacat.catalog.domain.XtdValueList;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdUnit;
import de.bentrm.datacat.catalog.domain.XtdValue;
import de.bentrm.datacat.catalog.repository.OrderedValueRepository;
import de.bentrm.datacat.catalog.repository.ValueListRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.ValueListRecordService;
import de.bentrm.datacat.catalog.service.ValueRecordService;
import de.bentrm.datacat.catalog.service.dto.Relationships.UnitDtoProjection;
import de.bentrm.datacat.catalog.service.dto.Relationships.ValueDtoProjection;
import de.bentrm.datacat.catalog.service.dto.Relationships.ValuesDtoProjection;
import de.bentrm.datacat.catalog.service.ConceptRecordService;
import de.bentrm.datacat.catalog.service.LanguageRecordService;
import de.bentrm.datacat.catalog.service.OrderedValueRecordService;
import de.bentrm.datacat.catalog.service.PropertyRecordService;
import de.bentrm.datacat.catalog.service.UnitRecordService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.data.support.PageableExecutionUtils;

@Slf4j
@Service
@Validated
@Transactional(readOnly = true)
public class ValueListRecordServiceImpl extends AbstractSimpleRecordServiceImpl<XtdValueList, ValueListRepository>
        implements ValueListRecordService {

    @Autowired
    private OrderedValueRecordService orderedValueRecordService;

    @Autowired
    private LanguageRecordService languageRecordService;

    @Autowired
    private UnitRecordService unitRecordService;

    @Autowired
    private PropertyRecordService propertyRecordService;

    @Autowired
    private ConceptRecordService conceptRecordService;

    @Autowired
    private ValueRecordService valueRecordService;

    @Autowired
    private OrderedValueRepository orderedValueRepository;

    public ValueListRecordServiceImpl(Neo4jTemplate neo4jTemplate, ValueListRepository repository,
            CatalogCleanupService cleanupService) {
        super(XtdValueList.class, neo4jTemplate, repository, cleanupService);
    }

    @Override
    public @NotNull Page<XtdValueList> findAll(@NotNull de.bentrm.datacat.base.specification.QuerySpecification specification) {
        // Use optimized query when no complex filters are applied
        if (isSimpleQuery(specification)) {
            List<XtdValueList> valueLists = findValueListsWithRelations(specification);
            Pageable pageable = specification.getPageable().orElse(PageRequest.of(0, 20));
            return PageableExecutionUtils.getPage(valueLists, pageable, 
                () -> getRepository().count());
        }
        // Fallback to default implementation for complex queries
        return super.findAll(specification);
    }

    private boolean isSimpleQuery(de.bentrm.datacat.base.specification.QuerySpecification specification) {
        // Consider it simple if there are no filters, only pagination and sorting
        return specification.getFilters() == null || specification.getFilters().isEmpty();
    }

    private List<XtdValueList> findValueListsWithRelations(de.bentrm.datacat.base.specification.QuerySpecification specification) {
        Pageable pageable = specification.getPageable().orElse(PageRequest.of(0, 20));
        String query = buildOptimizedValueListQuery(pageable);
        return getNeo4jTemplate().findAll(query, XtdValueList.class);
    }

    private String buildOptimizedValueListQuery(Pageable pageable) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("MATCH (vl:XtdValueList) ");
        
        // Add optional matches for commonly used relations
        queryBuilder.append("OPTIONAL MATCH (vl)-[:VALUES]->(values:XtdValue) ");
        queryBuilder.append("OPTIONAL MATCH (vl)-[:ORDERED_VALUES]->(orderedValues:XtdOrderedValue) ");
        queryBuilder.append("OPTIONAL MATCH (vl)<-[:ASSIGNS_VALUE_LIST]-(properties:XtdProperty) ");
        
        queryBuilder.append("RETURN vl, ");
        queryBuilder.append("collect(DISTINCT values) as values, ");
        queryBuilder.append("collect(DISTINCT orderedValues) as orderedValues, ");
        queryBuilder.append("collect(DISTINCT properties) as properties ");
        
        // Add sorting if specified
        if (pageable.getSort().isSorted()) {
            queryBuilder.append("ORDER BY ");
            String sortClause = pageable.getSort().stream()
                    .map(order -> "vl." + order.getProperty() + " " + order.getDirection())
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
        return CatalogRecordType.ValueList;
    }

    @Override
    public @NotNull Optional<XtdValueList> findById(@NotNull String id) {
        // Überschreibe die Standard-Methode, um KEINE Relationen zu laden
        return getRepository().findByIdWithoutRelations(id);
    }

    @Override
    public List<XtdOrderedValue> getOrderedValues(XtdValueList valueList) {
        Assert.notNull(valueList.getId(), "ValueList must be persistent.");
        final List<String> valueIds = getRepository().findAllOrderedValueIdsAssignedToValueList(valueList.getId());
        final Iterable<XtdOrderedValue> values = orderedValueRecordService.findAllEntitiesById(valueIds);

        return StreamSupport.stream(values.spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public @NotNull Page<XtdOrderedValue> getOrderedValues(@NotNull XtdValueList valueList, @NotNull Pageable pageable) {
        Assert.notNull(valueList.getId(), "ValueList must be persistent.");
        
        final List<String> valueIds = getRepository().findOrderedValuesByValueListIdPaginated(
            valueList.getId(), 
            (int) pageable.getOffset(), 
            pageable.getPageSize()
        );
        
        final Iterable<XtdOrderedValue> values = orderedValueRecordService.findAllEntitiesById(valueIds);
        final List<XtdOrderedValue> valuesList = StreamSupport.stream(values.spliterator(), false)
            .collect(Collectors.toList());
        
        // Get total count for pagination
        final Long totalCount = getRepository().countOrderedValuesByValueListId(valueList.getId());
        
        return PageableExecutionUtils.getPage(valuesList, pageable, () -> totalCount);
    }

    @Override
    public List<XtdProperty> getProperties(XtdValueList valueList) {
        Assert.notNull(valueList.getId(), "ValueList must be persistent.");
        final List<String> propertyIds = getRepository().findAllPropertyIdsAssignedToValueList(valueList.getId());
        final Iterable<XtdProperty> properties = propertyRecordService.findAllEntitiesById(propertyIds);

        return StreamSupport.stream(properties.spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public Optional<XtdUnit> getUnit(XtdValueList valueList) {
        Assert.notNull(valueList.getId(), "ValueList must be persistent.");
        final String unitId = getRepository().findUnitIdAssignedToValueList(valueList.getId());
        if (unitId == null) {
            return null;
        } else {
            Optional<XtdUnit> unit = unitRecordService.findByIdWithDirectRelations(unitId);
            return unit;
        }
    }

    @Override
    public Optional<XtdLanguage> getLanguage(XtdValueList valueList) {
        Assert.notNull(valueList.getId(), "ValueList must be persistent.");
        final String languageId = getRepository().findLanguageIdAssignedToValueList(valueList.getId());
        if (languageId == null) {
            return null;
        } else {
            Optional<XtdLanguage> language = languageRecordService.findByIdWithDirectRelations(languageId);
            return language;
        }
    }

    @Override
    public Optional<XtdValueList> findByIdWithAllRelations(String id) {
        return getRepository().findByIdWithAllRelations(id);
    }
    
    @Override
    public Optional<XtdValueList> findByIdWithIncomingAndOutgoingRelations(String id) {
        return getRepository().findByIdWithIncomingAndOutgoingRelations(id);
    }

    @Override
    public Optional<XtdValueList> findByIdWithoutRelations(String id) {
        return getRepository().findByIdWithoutRelations(id);
    }

    @Transactional
    @Override
    public @NotNull XtdValueList setRelatedRecords(@NotBlank String recordId,
            @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {

        final XtdValueList valueList = getRepository().findByIdWithDirectRelations(recordId)
                .orElseThrow(() -> new IllegalArgumentException("No record with id " + recordId + " found."));

        switch (relationType) {
        case Unit -> {
            if (valueList.getUnit() != null) {
                throw new IllegalArgumentException("ValueList already has a Unit assigned.");
            } else if (relatedRecordIds.size() != 1) {
                throw new IllegalArgumentException("Exactly one Unit must be assigned to a ValueList.");
            } else {
                final XtdUnit unit = unitRecordService.findByIdWithDirectRelations(relatedRecordIds.get(0)).orElseThrow(
                        () -> new IllegalArgumentException("No record with id " + relatedRecordIds.get(0) + " found."));
                valueList.setUnit(unit);
            }
            neo4jTemplate.saveAs(valueList, UnitDtoProjection.class);
        }
        default -> conceptRecordService.setRelatedRecords(recordId, relatedRecordIds, relationType);
        }

        log.trace("Updated relationship: {}", valueList);
        return valueList;
    }

    @Transactional
    @Override
    public @NotNull XtdValueList setOrderedValues(@NotBlank String recordId,
            @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType,
            Integer orderNumber) {

        final XtdValueList valueList = getRepository().findByIdWithDirectRelations(recordId)
                .orElseThrow(() -> new IllegalArgumentException("No record with id " + recordId + " found."));

        final Iterable<XtdValue> items = valueRecordService.findAllEntitiesById(relatedRecordIds);
        final List<XtdValue> related = StreamSupport.stream(items.spliterator(), false).collect(Collectors.toList());

        Set<XtdOrderedValue> orderedValues = new HashSet<>();
        if (orderNumber == null) {
            orderNumber = 0;
            for (XtdOrderedValue orderd : valueList.getValues()) {
                orderNumber = Math.max(orderNumber, orderd.getOrder());
            }
        } else {
            orderNumber -= 1;
        }
        for (XtdValue value : related) {
            orderNumber += 1;
            XtdOrderedValue orderedValue = new XtdOrderedValue();
            // orderedValue.setOrderedValue(value);
            orderedValue.setOrder(orderNumber);
            orderedValueRepository.save(orderedValue);
            orderedValues.add(orderedValue);

            orderedValue.setOrderedValue(value);
            neo4jTemplate.saveAs(orderedValue, ValueDtoProjection.class);
        }

        valueList.getValues().addAll(orderedValues);
        neo4jTemplate.saveAs(valueList, ValuesDtoProjection.class);

        log.trace("Updated relationship: {}", valueList);
        return valueList;
    }

    @Transactional
    @Override
    public @NotNull XtdValueList removeRelationship(@NotBlank String recordId, @NotBlank String relatedRecordId,
            @NotNull SimpleRelationType relationType) {
        log.info("ValueListRecordService.removeRelationship called: recordId={}, relatedRecordId={}, relationType={}", 
            recordId, relatedRecordId, relationType.getRelationProperty());
        log.trace("Deleting relationship from record with id {}...", recordId);
        final XtdValueList entry = this.getRepository().findByIdWithDirectRelations(recordId)
                .orElseThrow(() -> new IllegalArgumentException("No record with id " + recordId + " found."));

        // Handle Values relationship (via OrderedValue)
        if (relationType == SimpleRelationType.Values) {
            Set<XtdOrderedValue> orderedValues = entry.getValues();
            for (XtdOrderedValue orderedValue : orderedValues) {
                XtdOrderedValue value = orderedValueRepository.findByIdWithDirectRelations(orderedValue.getId())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "No record with id " + orderedValue.getOrderedValue().getId() + " found."));
                if (value.getOrderedValue().getId().equals(relatedRecordId)) {
                    removeRecord(orderedValue.getId());
                }
            }
        } 
        // Handle other relationships (Unit, Language, etc.) via standard cleanup
        else {
            log.info("Delegating to super.removeRelationship for relationType: {}", relationType.getRelationProperty());
            super.removeRelationship(recordId, relatedRecordId, relationType);
        }
        
        return entry;
    }
}
