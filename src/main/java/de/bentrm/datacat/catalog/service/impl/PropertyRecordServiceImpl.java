package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdRelationshipToProperty;
import de.bentrm.datacat.catalog.domain.XtdSubject;
import de.bentrm.datacat.catalog.domain.XtdUnit;
import de.bentrm.datacat.catalog.domain.XtdValueList;
import de.bentrm.datacat.catalog.domain.XtdDimension;
import de.bentrm.datacat.catalog.domain.XtdInterval;
import de.bentrm.datacat.catalog.domain.XtdSymbol;
import de.bentrm.datacat.catalog.domain.XtdQuantityKind;
import de.bentrm.datacat.catalog.repository.DimensionRepository;
import de.bentrm.datacat.catalog.repository.PropertyRepository;
import de.bentrm.datacat.catalog.repository.QuantityKindRepository;
import de.bentrm.datacat.catalog.repository.RelationshipToPropertyRepository;
import de.bentrm.datacat.catalog.repository.SubjectRepository;
import de.bentrm.datacat.catalog.repository.UnitRepository;
import de.bentrm.datacat.catalog.repository.ValueListRepository;
import de.bentrm.datacat.catalog.repository.SymbolRepository;
import de.bentrm.datacat.catalog.repository.IntervalRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.PropertyRecordService;
import de.bentrm.datacat.catalog.service.ConceptRecordService;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@Validated
@Transactional(readOnly = true)
public class PropertyRecordServiceImpl
                extends AbstractSimpleRecordServiceImpl<XtdProperty, PropertyRepository>
                implements PropertyRecordService {

        private final SubjectRepository subjectRepository;
        private final ValueListRepository valueListRepository;
        private final UnitRepository unitRepository;
        private final RelationshipToPropertyRepository relationshipToPropertyRepository;
        private final DimensionRepository dimensionRepository;
        private final SymbolRepository symbolRepository;
        private final IntervalRepository intervalRepository;
        private final QuantityKindRepository quantityKindRepository;
        private final ConceptRecordService conceptRecordService;

        public PropertyRecordServiceImpl(SessionFactory sessionFactory,
                        PropertyRepository repository,
                        SubjectRepository subjectRepository,
                        ValueListRepository valueListRepository,
                        UnitRepository unitRepository,
                        RelationshipToPropertyRepository relationshipToPropertyRepository,
                        DimensionRepository dimensionRepository,
                        SymbolRepository symbolRepository,
                        IntervalRepository intervalRepository,
                        QuantityKindRepository quantityKindRepository,
                        ConceptRecordService conceptRecordService,
                        CatalogCleanupService cleanupService) {
                super(XtdProperty.class, sessionFactory, repository, cleanupService);
                this.subjectRepository = subjectRepository;
                this.valueListRepository = valueListRepository;
                this.unitRepository = unitRepository;
                this.relationshipToPropertyRepository = relationshipToPropertyRepository;
                this.dimensionRepository = dimensionRepository;
                this.symbolRepository = symbolRepository;
                this.intervalRepository = intervalRepository;
                this.quantityKindRepository = quantityKindRepository;
                this.conceptRecordService = conceptRecordService;
        }

        @Override
        public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
                return CatalogRecordType.Property;
        }

        @Override
        public List<XtdSubject> getSubjects(XtdProperty property) {
                Assert.notNull(property.getId(), "Property must be persistent.");
                final List<String> subjectIds = subjectRepository.findAllSubjectIdsAssignedToProperty(property.getId());
                final Iterable<XtdSubject> subjects = subjectRepository.findAllById(subjectIds);

                return StreamSupport
                                .stream(subjects.spliterator(), false)
                                .collect(Collectors.toList());
        }

        @Override
        public List<XtdValueList> getValueLists(XtdProperty property) {
                Assert.notNull(property.getId(), "Property must be persistent.");
                final List<String> valueListIds = valueListRepository
                                .findAllValueListIdsAssignedToProperty(property.getId());
                final Iterable<XtdValueList> valueLists = valueListRepository.findAllById(valueListIds);

                return StreamSupport
                                .stream(valueLists.spliterator(), false)
                                .collect(Collectors.toList());
        }

        @Override
        public List<XtdUnit> getUnits(XtdProperty property) {
                Assert.notNull(property.getId(), "Property must be persistent.");
                final List<String> unitIds = unitRepository.findAllUnitIdsAssignedToProperty(property.getId());
                final Iterable<XtdUnit> units = unitRepository.findAllById(unitIds);

                return StreamSupport
                                .stream(units.spliterator(), false)
                                .collect(Collectors.toList());
        }

        @Override
        public List<XtdRelationshipToProperty> getConnectedProperties(XtdProperty property) {
                Assert.notNull(property.getId(), "Property must be persistent.");
                final List<String> relationshipIds = relationshipToPropertyRepository
                                .findAllConnectedPropertyRelationshipIdsAssignedToProperty(property.getId());
                final Iterable<XtdRelationshipToProperty> relations = relationshipToPropertyRepository
                                .findAllById(relationshipIds);

                return StreamSupport
                                .stream(relations.spliterator(), false)
                                .collect(Collectors.toList());
        }

        @Override
        public List<XtdRelationshipToProperty> getConnectingProperties(XtdProperty property) {
                Assert.notNull(property.getId(), "Property must be persistent.");
                final List<String> relationshipIds = relationshipToPropertyRepository
                                .findAllConnectingPropertyRelationshipIdsAssignedToProperty(property.getId());
                final Iterable<XtdRelationshipToProperty> relations = relationshipToPropertyRepository
                                .findAllById(relationshipIds);

                return StreamSupport
                                .stream(relations.spliterator(), false)
                                .collect(Collectors.toList());
        }

        @Override
        public XtdDimension getDimension(XtdProperty property) {
                Assert.notNull(property.getId(), "Property must be persistent.");
                final String dimensionId = dimensionRepository.findDimensionIdAssignedToProperty(property.getId());
                if (dimensionId == null) {
                        return null;
                }
                final XtdDimension dimension = dimensionRepository.findById(dimensionId).orElse(null);

                return dimension;
        }

        @Override
        public List<XtdSymbol> getSymbols(XtdProperty property) {
                Assert.notNull(property.getId(), "Property must be persistent.");
                final List<String> symbolIds = symbolRepository.findAllSymbolIdsAssignedToProperty(property.getId());
                final Iterable<XtdSymbol> symbols = symbolRepository.findAllById(symbolIds);

                return StreamSupport
                                .stream(symbols.spliterator(), false)
                                .collect(Collectors.toList());
        }

        @Override
        public List<XtdInterval> getIntervals(XtdProperty property) {
                Assert.notNull(property.getId(), "Property must be persistent.");
                final List<String> intervalIds = intervalRepository
                                .findAllIntervalIdsAssignedToProperty(property.getId());
                final Iterable<XtdInterval> intervals = intervalRepository.findAllById(intervalIds);

                return StreamSupport
                                .stream(intervals.spliterator(), false)
                                .collect(Collectors.toList());

        }

        @Override
        public List<XtdQuantityKind> getQuantityKinds(XtdProperty property) {
                Assert.notNull(property.getId(), "Property must be persistent.");
                final List<String> quantityKindIds = quantityKindRepository
                                .findAllQuantityKindIdsAssignedToProperty(property.getId());
                final Iterable<XtdQuantityKind> quantityKinds = quantityKindRepository.findAllById(quantityKindIds);

                return StreamSupport
                                .stream(quantityKinds.spliterator(), false)
                                .collect(Collectors.toList());
        }

        @Transactional
        @Override
        public @NotNull XtdProperty setRelatedRecords(@NotBlank String recordId,
                        @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {

                final XtdProperty property = getRepository().findById(recordId, 0).orElseThrow();

                switch (relationType) {
                        case Symbols:
                                final Iterable<XtdSymbol> symbols = symbolRepository.findAllById(relatedRecordIds, 0);
                                final List<XtdSymbol> relatedSymbols = StreamSupport
                                                .stream(symbols.spliterator(), false)
                                                .collect(Collectors.toList());

                                property.getSymbols().clear();
                                property.getSymbols().addAll(relatedSymbols);
                                break;
                        case Units:
                                final Iterable<XtdUnit> units = unitRepository.findAllById(relatedRecordIds, 0);
                                final List<XtdUnit> relatedUnits = StreamSupport
                                                .stream(units.spliterator(), false)
                                                .collect(Collectors.toList());

                                property.getUnits().clear();
                                property.getUnits().addAll(relatedUnits);
                                break;
                        case Dimension:
                                if (property.getDimension() != null) {
                                        throw new IllegalArgumentException("Property already has a dimension assigned.");
                                } else if (relatedRecordIds.size() != 1) {
                                        throw new IllegalArgumentException("Exactly one dimension must be assigned.");
                                } else {
                                        final XtdDimension dimension = dimensionRepository.findById(relatedRecordIds.get(0))
                                                        .orElseThrow();
                                        property.setDimension(dimension);
                                }
                                break;
                        case BoundaryValues:
                                final Iterable<XtdInterval> intervals = intervalRepository.findAllById(relatedRecordIds, 0);
                                final List<XtdInterval> relatedIntervals = StreamSupport
                                                .stream(intervals.spliterator(), false)
                                                .collect(Collectors.toList());

                                property.getBoundaryValues().clear();
                                property.getBoundaryValues().addAll(relatedIntervals);
                                break;
                        case QuantityKinds:
                                final Iterable<XtdQuantityKind> quantityKinds = quantityKindRepository
                                                .findAllById(relatedRecordIds, 0);
                                final List<XtdQuantityKind> relatedQuantityKinds = StreamSupport
                                                .stream(quantityKinds.spliterator(), false)
                                                .collect(Collectors.toList());

                                property.getQuantityKinds().clear();
                                property.getQuantityKinds().addAll(relatedQuantityKinds);
                                break;
                        case PossibleValues:
                                final Iterable<XtdValueList> valueLists = valueListRepository.findAllById(relatedRecordIds, 0);
                                final List<XtdValueList> relatedValueLists = StreamSupport
                                                .stream(valueLists.spliterator(), false)
                                                .collect(Collectors.toList());

                                property.getPossibleValues().clear();
                                property.getPossibleValues().addAll(relatedValueLists);
                                break;
                        default:
                                conceptRecordService.setRelatedRecords(recordId, relatedRecordIds, relationType);
                                break;
                }

                final XtdProperty persistentProperty = getRepository().save(property);
                log.trace("Updated relationship: {}", persistentProperty);
                return persistentProperty;
        }
}
