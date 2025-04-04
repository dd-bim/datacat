package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.domain.XtdLanguage;
import de.bentrm.datacat.catalog.domain.XtdOrderedValue;
import de.bentrm.datacat.catalog.domain.XtdValueList;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdUnit;
import de.bentrm.datacat.catalog.repository.ValueListRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.ValueListRecordService;
import de.bentrm.datacat.catalog.service.dto.Relationships.LanguageDtoProjection;
import de.bentrm.datacat.catalog.service.dto.Relationships.UnitDtoProjection;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jTemplate;

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

    public ValueListRecordServiceImpl(Neo4jTemplate neo4jTemplate, ValueListRepository repository,
            CatalogCleanupService cleanupService) {
        super(XtdValueList.class, neo4jTemplate, repository, cleanupService);
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.ValueList;
    }

    @Override
    public List<XtdOrderedValue> getOrderedValues(XtdValueList valueList) {
        Assert.notNull(valueList.getId(), "ValueList must be persistent.");
        final List<String> valueIds = getRepository().findAllOrderedValueIdsAssignedToValueList(valueList.getId());
        final Iterable<XtdOrderedValue> values = orderedValueRecordService.findAllEntitiesById(valueIds);

        return StreamSupport.stream(values.spliterator(), false).collect(Collectors.toList());
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

    @Transactional
    @Override
    public @NotNull XtdValueList setRelatedRecords(@NotBlank String recordId,
            @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {

        final XtdValueList valueList = getRepository().findByIdWithDirectRelations(recordId).orElseThrow(() -> new IllegalArgumentException("No record with id " + recordId + " found."));

        switch (relationType) {
        case Unit -> {
            if (valueList.getUnit() != null) {
                throw new IllegalArgumentException("ValueList already has a Unit assigned.");
            } else if (relatedRecordIds.size() != 1) {
                throw new IllegalArgumentException("Exactly one Unit must be assigned to a ValueList.");
            } else {
                final XtdUnit unit = unitRecordService.findByIdWithDirectRelations(relatedRecordIds.get(0))
                        .orElseThrow(() -> new IllegalArgumentException("No record with id " + relatedRecordIds.get(0) + " found."));
                valueList.setUnit(unit);
            }
            neo4jTemplate.saveAs(valueList, UnitDtoProjection.class);
        }
        case Values -> {
            final Iterable<XtdOrderedValue> items = orderedValueRecordService.findAllEntitiesById(relatedRecordIds);
            final List<XtdOrderedValue> related = StreamSupport.stream(items.spliterator(), false)
                    .collect(Collectors.toList());

            valueList.getValues().clear();
            valueList.getValues().addAll(related);
            neo4jTemplate.saveAs(valueList, ValuesDtoProjection.class);
        }
        case Language -> {
            if (valueList.getLanguage() != null) {
                throw new IllegalArgumentException("ValueList already has a Language assigned.");
            } else if (relatedRecordIds.size() != 1) {
                throw new IllegalArgumentException("Exactly one Language must be assigned to a ValueList.");
            } else {
                final XtdLanguage language = languageRecordService.findByIdWithDirectRelations(relatedRecordIds.get(0))
                        .orElseThrow(() -> new IllegalArgumentException("No record with id " + relatedRecordIds.get(0) + " found."));
                valueList.setLanguage(language);
            }
            neo4jTemplate.saveAs(valueList, LanguageDtoProjection.class);
        }
        default -> conceptRecordService.setRelatedRecords(recordId, relatedRecordIds, relationType);
        }

        log.trace("Updated relationship: {}", valueList);
        return valueList;
    }
}
