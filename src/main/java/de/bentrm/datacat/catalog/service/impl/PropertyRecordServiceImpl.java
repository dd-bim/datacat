package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.base.specification.QuerySpecification;
import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdRelationshipToProperty;
import de.bentrm.datacat.catalog.domain.XtdSubject;
import de.bentrm.datacat.catalog.domain.XtdUnit;
import de.bentrm.datacat.catalog.domain.XtdValueList;
import de.bentrm.datacat.catalog.domain.Enums.XtdDataTypeEnum;
import de.bentrm.datacat.catalog.domain.XtdDimension;
import de.bentrm.datacat.catalog.domain.XtdInterval;
import de.bentrm.datacat.catalog.domain.XtdSymbol;
import de.bentrm.datacat.catalog.domain.XtdQuantityKind;
import de.bentrm.datacat.catalog.repository.PropertyRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.PropertyRecordService;
import de.bentrm.datacat.catalog.service.QuantityKindRecordService;
import de.bentrm.datacat.catalog.service.RelationshipToPropertyRecordService;
import de.bentrm.datacat.catalog.service.SubjectRecordService;
import de.bentrm.datacat.catalog.service.SymbolRecordService;
import de.bentrm.datacat.catalog.service.UnitRecordService;
import de.bentrm.datacat.catalog.service.ValueListRecordService;
import de.bentrm.datacat.catalog.service.dto.PropertyDtoProjection;
import de.bentrm.datacat.catalog.service.dto.Relationships.BoundaryValuesDtoProjection;
import de.bentrm.datacat.catalog.service.dto.Relationships.DimensionDtoProjection;
import de.bentrm.datacat.catalog.service.dto.Relationships.PossibleValuesDtoProjection;
import de.bentrm.datacat.catalog.service.dto.Relationships.QuantityKindsDtoProjection;
import de.bentrm.datacat.catalog.service.dto.Relationships.SymbolsDtoProjection;
import de.bentrm.datacat.catalog.service.dto.Relationships.UnitsDtoProjection;
import de.bentrm.datacat.catalog.service.ConceptRecordService;
import de.bentrm.datacat.catalog.service.DimensionRecordService;
import de.bentrm.datacat.catalog.service.IntervalRecordService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@Validated
@Transactional(readOnly = true)
public class PropertyRecordServiceImpl extends AbstractSimpleRecordServiceImpl<XtdProperty, PropertyRepository>
                implements PropertyRecordService {

        @Autowired
        @Lazy
        private SubjectRecordService subjectRecordService;

        @Autowired
        @Lazy
        private ValueListRecordService valueListRecordService;

        @Autowired
        @Lazy
        private UnitRecordService unitRecordService;

        @Autowired
        private RelationshipToPropertyRecordService relationshipToPropertyRecordService;

        @Autowired
        private DimensionRecordService dimensionRecordService;

        @Autowired
        @Lazy
        private SymbolRecordService symbolRecordService;

        @Autowired
        @Lazy
        private IntervalRecordService intervalRecordService;

        @Autowired
        @Lazy
        private QuantityKindRecordService quantityKindRecordService;

        @Autowired
        private ConceptRecordService conceptRecordService;

        public PropertyRecordServiceImpl(Neo4jTemplate neo4jTemplate, PropertyRepository repository,
                        CatalogCleanupService cleanupService) {
                super(XtdProperty.class, neo4jTemplate, repository, cleanupService);
        }

        @Override
        public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
                return CatalogRecordType.Property;
        }

        @Override
        public @NotNull Optional<XtdProperty> findById(@NotNull String id) {
                // Überschreibe die Standard-Methode, um KEINE Relationen zu laden
                return getRepository().findByIdWithoutRelations(id);
        }

        @Override
        public List<XtdSubject> getSubjects(XtdProperty property) {
                Assert.notNull(property.getId(), "Property must be persistent.");
                final List<String> subjectIds = getRepository().findAllSubjectIdsAssignedToProperty(property.getId());
                final Iterable<XtdSubject> subjects = subjectRecordService.findAllEntitiesById(subjectIds);

                return StreamSupport.stream(subjects.spliterator(), false).collect(Collectors.toList());
        }

        @Override
        public List<XtdValueList> getValueLists(XtdProperty property) {
                Assert.notNull(property.getId(), "Property must be persistent.");
                final List<String> valueListIds = getRepository()
                                .findAllValueListIdsAssignedToProperty(property.getId());
                final Iterable<XtdValueList> valueLists = valueListRecordService.findAllEntitiesById(valueListIds);
  
                return StreamSupport.stream(valueLists.spliterator(), false).collect(Collectors.toList());
        }

        @Override
        public List<XtdUnit> getUnits(XtdProperty property) {
                Assert.notNull(property.getId(), "Property must be persistent.");
                final List<String> unitIds = getRepository().findAllUnitIdsAssignedToProperty(property.getId());
                final Iterable<XtdUnit> units = unitRecordService.findAllEntitiesById(unitIds);

                return StreamSupport.stream(units.spliterator(), false).collect(Collectors.toList());
        }

        @Override
        public List<XtdRelationshipToProperty> getConnectedProperties(XtdProperty property) {
                Assert.notNull(property.getId(), "Property must be persistent.");
                final List<String> relationshipIds = getRepository()
                                .findAllConnectedPropertyRelationshipIdsAssignedToProperty(property.getId());
                final Iterable<XtdRelationshipToProperty> relations = relationshipToPropertyRecordService
                                .findAllEntitiesById(relationshipIds);

                return StreamSupport.stream(relations.spliterator(), false).collect(Collectors.toList());
        }

        @Override
        public List<XtdRelationshipToProperty> getConnectingProperties(XtdProperty property) {
                Assert.notNull(property.getId(), "Property must be persistent.");
                final List<String> relationshipIds = getRepository()
                                .findAllConnectingPropertyRelationshipIdsAssignedToProperty(property.getId());
                final Iterable<XtdRelationshipToProperty> relations = relationshipToPropertyRecordService
                                .findAllEntitiesById(relationshipIds);

                return StreamSupport.stream(relations.spliterator(), false).collect(Collectors.toList());
        }

        @Override
        public Optional<XtdDimension> getDimension(XtdProperty property) {
                Assert.notNull(property.getId(), "Property must be persistent.");
                final String dimensionId = getRepository().findDimensionIdAssignedToProperty(property.getId());
                if (dimensionId == null) {
                        return null;
                }
                final Optional<XtdDimension> dimension = dimensionRecordService.findById(dimensionId);

                return dimension;
        }

        @Override
        public List<XtdSymbol> getSymbols(XtdProperty property) {
                Assert.notNull(property.getId(), "Property must be persistent.");
                final List<String> symbolIds = getRepository().findAllSymbolIdsAssignedToProperty(property.getId());
                final Iterable<XtdSymbol> symbols = symbolRecordService.findAllEntitiesById(symbolIds);

                return StreamSupport.stream(symbols.spliterator(), false).collect(Collectors.toList());
        }

        @Override
        public List<XtdInterval> getIntervals(XtdProperty property) {
                Assert.notNull(property.getId(), "Property must be persistent.");
                final List<String> intervalIds = getRepository().findAllIntervalIdsAssignedToProperty(property.getId());
                final Iterable<XtdInterval> intervals = intervalRecordService.findAllEntitiesById(intervalIds);

                return StreamSupport.stream(intervals.spliterator(), false).collect(Collectors.toList());

        }

        @Override
        public List<XtdQuantityKind> getQuantityKinds(XtdProperty property) {
                Assert.notNull(property.getId(), "Property must be persistent.");
                final List<String> quantityKindIds = getRepository()
                                .findAllQuantityKindIdsAssignedToProperty(property.getId());
                final Iterable<XtdQuantityKind> quantityKinds = quantityKindRecordService
                                .findAllEntitiesById(quantityKindIds);

                return StreamSupport.stream(quantityKinds.spliterator(), false).collect(Collectors.toList());
        }

        @Transactional
        @Override
        public @NotNull XtdProperty updateDataType(@NotNull String id, @NotNull XtdDataTypeEnum dataType) {
                Assert.hasText(id, "Id must not be empty.");
                Assert.notNull(dataType, "Data type must not be null.");

                final XtdProperty property = getRepository().findByIdWithDirectRelations(id)
                                .orElseThrow(() -> new IllegalArgumentException("No record with id " + id + " found."));

                property.setDataType(dataType);
                neo4jTemplate.saveAs(property, PropertyDtoProjection.class);
                return property;
        }

        @Override
        public Optional<XtdProperty> findByIdWithIncomingAndOutgoingRelations(String id) {
                return getRepository().findByIdWithIncomingAndOutgoingRelations(id);
        }

        @Override
        public @NotNull Page<XtdProperty> findAll(@NotNull QuerySpecification specification) {
                // Verwende eine optimierte Query für Properties, die wichtige Relationen
                // vorlädt
                Collection<XtdProperty> properties;
                Pageable pageable;
                final Long count = count(specification);

                final Optional<Pageable> paged = specification.getPageable();
                if (paged.isPresent()) {
                        pageable = paged.get();
                        // Verwende optimierte Cypher-Query statt der Standard-Query
                        properties = findPropertiesWithRelations(specification, pageable);
                } else {
                        pageable = PageRequest.of(0, (int) Math.max(count, 10));
                        properties = findPropertiesWithRelations(specification, pageable);
                }

                return PageableExecutionUtils.getPage(List.copyOf(properties), pageable, () -> count);
        }

        private Collection<XtdProperty> findPropertiesWithRelations(QuerySpecification specification,
                        Pageable pageable) {
                String query = buildOptimizedPropertyQuery(specification, pageable);
                return neo4jTemplate.findAll(query, XtdProperty.class);
        }

        private String buildOptimizedPropertyQuery(QuerySpecification specification, Pageable pageable) {
                String sort = "";
                if (pageable.getSort().isSorted()) {
                        final Sort.Direction direction = pageable.getSort().get().findFirst()
                                        .map(Sort.Order::getDirection).orElse(Sort.Direction.ASC);
                        final String[] properties = pageable.getSort().get().map(Sort.Order::getProperty)
                                        .toArray(String[]::new);
                        List<String> prefixedProperties = Arrays.stream(properties)
                                        .map(property -> "p.`" + property + "` " + direction.name())
                                        .collect(Collectors.toList());
                        sort = " ORDER BY " + String.join(", ", prefixedProperties);
                }

                String whereClause = "";
                if (!specification.getFilters().isEmpty()) {
                        whereClause = " WHERE " + String.join(" AND ", specification.getFilters().stream()
                                        .map(filter -> filter.replace("n.", "p.")).collect(Collectors.toList()));
                }

                return String.format("""
                                MATCH (p:XtdProperty)%s

                                // Wichtigste Relationen vorladen
                                OPTIONAL MATCH (p)-[:DIMENSION]->(dim:XtdDimension)
                                OPTIONAL MATCH (p)-[:POSSIBLE_VALUES]->(vl:XtdValueList)
                                OPTIONAL MATCH (p)-[:UNITS]->(unit:XtdUnit)
                                OPTIONAL MATCH (p)-[:SYMBOLS]->(sym:XtdSymbol)
                                OPTIONAL MATCH (p)-[:QUANTITY_KINDS]->(qk:XtdQuantityKind)
                                OPTIONAL MATCH (p)-[:BOUNDARY_VALUES]->(iv:XtdInterval)
                                OPTIONAL MATCH (p)-[:TAGGED]->(tag:Tag)

                                WITH p,
                                     collect(DISTINCT dim) as dimensions,
                                     collect(DISTINCT vl) as valueLists,
                                     collect(DISTINCT unit) as units,
                                     collect(DISTINCT sym) as symbols,
                                     collect(DISTINCT qk) as quantityKinds,
                                     collect(DISTINCT iv) as intervals,
                                     collect(DISTINCT tag) as tags%s

                                RETURN p, dimensions, valueLists, units, symbols, quantityKinds, intervals, tags
                                SKIP %d LIMIT %d
                                """, whereClause, sort, pageable.getOffset(), pageable.getPageSize());
        }

        @Transactional
        @Override
        public @NotNull XtdProperty setRelatedRecords(@NotBlank String recordId,
                        @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {

                final XtdProperty property = getRepository().findByIdWithDirectRelations(recordId).orElseThrow(
                                () -> new IllegalArgumentException("No record with id " + recordId + " found."));

                switch (relationType) {
                case Symbols -> {
                        final Iterable<XtdSymbol> symbols = symbolRecordService.findAllEntitiesById(relatedRecordIds);
                        final List<XtdSymbol> relatedSymbols = StreamSupport.stream(symbols.spliterator(), false)
                                        .collect(Collectors.toList());

                        property.getSymbols().clear();
                        property.getSymbols().addAll(relatedSymbols);
                        neo4jTemplate.saveAs(property, SymbolsDtoProjection.class);
                }
                case Units -> {
                        final Iterable<XtdUnit> units = unitRecordService.findAllEntitiesById(relatedRecordIds);
                        final List<XtdUnit> relatedUnits = StreamSupport.stream(units.spliterator(), false)
                                        .collect(Collectors.toList());

                        property.getUnits().clear();
                        property.getUnits().addAll(relatedUnits);
                        neo4jTemplate.saveAs(property, UnitsDtoProjection.class);
                }
                case Dimension -> {
                        if (property.getDimension() != null) {
                                throw new IllegalArgumentException("Property already has a dimension assigned.");
                        } else if (relatedRecordIds.size() != 1) {
                                throw new IllegalArgumentException("Exactly one dimension must be assigned.");
                        } else {
                                final XtdDimension dimension = dimensionRecordService
                                                .findByIdWithDirectRelations(relatedRecordIds.get(0))
                                                .orElseThrow(() -> new IllegalArgumentException("No record with id "
                                                                + relatedRecordIds.get(0) + " found."));
                                property.setDimension(dimension);
                        }
                        neo4jTemplate.saveAs(property, DimensionDtoProjection.class);
                }
                case BoundaryValues -> {
                        final Iterable<XtdInterval> intervals = intervalRecordService
                                        .findAllEntitiesById(relatedRecordIds);
                        final List<XtdInterval> relatedIntervals = StreamSupport.stream(intervals.spliterator(), false)
                                        .collect(Collectors.toList());

                        property.getBoundaryValues().clear();
                        property.getBoundaryValues().addAll(relatedIntervals);
                        neo4jTemplate.saveAs(property, BoundaryValuesDtoProjection.class);
                }
                case QuantityKinds -> {
                        final Iterable<XtdQuantityKind> quantityKinds = quantityKindRecordService
                                        .findAllEntitiesById(relatedRecordIds);
                        final List<XtdQuantityKind> relatedQuantityKinds = StreamSupport
                                        .stream(quantityKinds.spliterator(), false).collect(Collectors.toList());

                        property.getQuantityKinds().clear();
                        property.getQuantityKinds().addAll(relatedQuantityKinds);
                        neo4jTemplate.saveAs(property, QuantityKindsDtoProjection.class);
                }
                case PossibleValues -> {
                        final Iterable<XtdValueList> valueLists = valueListRecordService
                                        .findAllEntitiesById(relatedRecordIds);
                        final List<XtdValueList> relatedValueLists = StreamSupport
                                        .stream(valueLists.spliterator(), false).collect(Collectors.toList());

                        property.getPossibleValues().clear();
                        property.getPossibleValues().addAll(relatedValueLists);
                        neo4jTemplate.saveAs(property, PossibleValuesDtoProjection.class);
                }
                default -> conceptRecordService.setRelatedRecords(recordId, relatedRecordIds, relationType);
                }

                log.trace("Updated relationship: {}", property);
                return property;
        }
}
