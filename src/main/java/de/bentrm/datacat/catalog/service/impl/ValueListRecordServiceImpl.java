package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.domain.XtdLanguage;
import de.bentrm.datacat.catalog.domain.XtdOrderedValue;
import de.bentrm.datacat.catalog.domain.XtdValueList;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdUnit;
import de.bentrm.datacat.catalog.repository.ValueListRepository;
import de.bentrm.datacat.catalog.repository.OrderedValueRepository;
import de.bentrm.datacat.catalog.repository.PropertyRepository;
import de.bentrm.datacat.catalog.repository.UnitRepository;
import de.bentrm.datacat.catalog.repository.LanguageRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.ValueListRecordService;
import de.bentrm.datacat.catalog.service.ConceptRecordService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.data.neo4j.core.Neo4jTemplate;

@Slf4j
@Service
@Validated
@Transactional(readOnly = true)
public class ValueListRecordServiceImpl
        extends AbstractSimpleRecordServiceImpl<XtdValueList, ValueListRepository>
        implements ValueListRecordService {

            private final OrderedValueRepository orderedValueRepository;
            private final PropertyRepository propertyRepository;
            private final UnitRepository unitRepository;
            private final LanguageRepository languageRepository;
            private final ConceptRecordService conceptRecordService;

    public ValueListRecordServiceImpl(Neo4jTemplate neo4jTemplate,
                                    ValueListRepository repository,
                                    OrderedValueRepository orderedValueRepository,
                                    PropertyRepository propertyRepository,
                                    UnitRepository unitRepository,
                                    LanguageRepository languageRepository,
                                    ConceptRecordService conceptRecordService,
                                    CatalogCleanupService cleanupService) {
        super(XtdValueList.class, neo4jTemplate, repository, cleanupService);
        this.orderedValueRepository = orderedValueRepository;
        this.propertyRepository = propertyRepository;
        this.unitRepository = unitRepository;
        this.languageRepository = languageRepository;
        this.conceptRecordService = conceptRecordService;
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.ValueList;
    }

    @Override
    public List<XtdOrderedValue> getOrderedValues(XtdValueList valueList) {
        Assert.notNull(valueList.getId(), "ValueList must be persistent.");
        final List<String> valueIds = orderedValueRepository.findAllOrderedValueIdsAssignedToValueList(valueList.getId());
        final Iterable<XtdOrderedValue> values = orderedValueRepository.findAllById(valueIds);

        return StreamSupport
                .stream(values.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public List<XtdProperty> getProperties(XtdValueList valueList) {
        Assert.notNull(valueList.getId(), "ValueList must be persistent.");
        final List<String> propertyIds = propertyRepository.findAllPropertyIdsAssignedToValueList(valueList.getId());
        final Iterable<XtdProperty> properties = propertyRepository.findAllById(propertyIds);

        return StreamSupport
                .stream(properties.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public XtdUnit getUnit(XtdValueList valueList) {
        Assert.notNull(valueList.getId(), "ValueList must be persistent.");
        final String unitId = unitRepository.findUnitIdAssignedToValueList(valueList.getId());
        if (unitId == null) {
            return null;
        } else {
            XtdUnit unit = unitRepository.findById(unitId).orElse(null);
            return unit;
        }
    }

    @Transactional
    @Override
    public @NotNull XtdValueList setRelatedRecords(@NotBlank String recordId,
                                                    @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {

        final XtdValueList valueList = getRepository().findById(recordId).orElseThrow();

        switch (relationType) {
            case Unit:
                if (valueList.getUnit() != null) {
                    throw new IllegalArgumentException("ValueList already has a Unit assigned.");
                } else if (relatedRecordIds.size() != 1) {
                    throw new IllegalArgumentException("Exactly one Unit must be assigned to a ValueList.");
                } else {
                    final XtdUnit unit = unitRepository.findById(relatedRecordIds.get(0)).orElseThrow();
                    valueList.setUnit(unit);
                }
                break;
            case Values:
                final Iterable<XtdOrderedValue> items = orderedValueRepository.findAllById(relatedRecordIds);
                final List<XtdOrderedValue> related = StreamSupport
                        .stream(items.spliterator(), false)
                        .collect(Collectors.toList());

                valueList.getValues().clear();
                valueList.getValues().addAll(related);
                break;
            case Language:
                if (valueList.getLanguage() != null) {
                    throw new IllegalArgumentException("ValueList already has a Language assigned.");
                } else if (relatedRecordIds.size() != 1) {
                    throw new IllegalArgumentException("Exactly one Language must be assigned to a ValueList.");
                } else {
                    final XtdLanguage language = languageRepository.findById(relatedRecordIds.get(0))
                            .orElseThrow();
                    valueList.setLanguage(language);
                }
            default:
                conceptRecordService.setRelatedRecords(recordId, relatedRecordIds, relationType);
                break;
        }

        final XtdValueList persistentValueList = getRepository().save(valueList);
        log.trace("Updated relationship: {}", persistentValueList);
        return persistentValueList;
    }
}
