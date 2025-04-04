package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.domain.XtdOrderedValue;
import de.bentrm.datacat.catalog.domain.XtdValueList;
import de.bentrm.datacat.catalog.domain.XtdValue;
import de.bentrm.datacat.catalog.repository.OrderedValueRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.ObjectRecordService;
import de.bentrm.datacat.catalog.service.OrderedValueRecordService;
import de.bentrm.datacat.catalog.service.ValueListRecordService;
import de.bentrm.datacat.catalog.service.ValueRecordService;
import de.bentrm.datacat.catalog.service.dto.Relationships.OrderedValueDtoProjection;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.neo4j.core.Neo4jTemplate;
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

@Slf4j
@Service
@Validated
@Transactional(readOnly = true)
public class OrderedValueRecordServiceImpl extends
        AbstractSimpleRecordServiceImpl<XtdOrderedValue, OrderedValueRepository> implements OrderedValueRecordService {

    @Autowired
    private ValueRecordService valueRecordService;

    @Autowired
    @Lazy
    private ValueListRecordService valueListRecordService;

    @Autowired
    @Lazy
    private ObjectRecordService objectRecordService;

    public OrderedValueRecordServiceImpl(Neo4jTemplate neo4jTemplate, OrderedValueRepository repository,
            CatalogCleanupService cleanupService) {
        super(XtdOrderedValue.class, neo4jTemplate, repository, cleanupService);
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.OrderedValue;
    }

    @Override
    public XtdValue getValue(XtdOrderedValue orderedValue) {
        Assert.notNull(orderedValue.getId(), "OrderedValue must be persistent.");
        final String valueId = getRepository().findValueIdAssignedToOrderedValue(orderedValue.getId());
        if (valueId == null) {
            return null;
        }
        final XtdValue value = valueRecordService.findById(valueId).orElseThrow(() -> new IllegalArgumentException("No record with id " + valueId + " found."));

        return value;
    }

    @Override
    public List<XtdValueList> getValueLists(@NotNull XtdOrderedValue orderedValue) {
        Assert.notNull(orderedValue.getId(), "OrderedValue must be persistent.");
        final List<String> valueListIds = getRepository()
                .findAllValueListIdsAssigningOrderedValue(orderedValue.getId());
        final Iterable<XtdValueList> valueLists = valueListRecordService.findAllEntitiesById(valueListIds);

        return StreamSupport.stream(valueLists.spliterator(), false).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public @NotNull XtdOrderedValue setRelatedRecords(@NotBlank String recordId,
            @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {

        final XtdOrderedValue orderedValue = getRepository().findById(recordId).orElseThrow(() -> new IllegalArgumentException("No record with id " + recordId + " found."));

        switch (relationType) {
        case OrderedValue -> {
            if (orderedValue.getOrderedValue() != null) {
                throw new IllegalArgumentException("OrderedValue already has a Value assigned.");
            } else if (relatedRecordIds.size() != 1) {
                throw new IllegalArgumentException("Exactly one Value must be assigned to an OrderedValue.");
            } else {
                final XtdValue value = valueRecordService.findByIdWithDirectRelations(relatedRecordIds.get(0))
                        .orElseThrow(() -> new IllegalArgumentException("No record with id " + relatedRecordIds.get(0) + " found."));
                orderedValue.setOrderedValue(value);
            }
        }
        default -> objectRecordService.setRelatedRecords(recordId, relatedRecordIds, relationType);
        }

        neo4jTemplate.saveAs(orderedValue, OrderedValueDtoProjection.class);
        log.trace("Updated relationship: {}", orderedValue);
        return orderedValue;
    }
}
