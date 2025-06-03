package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.*;
import de.bentrm.datacat.catalog.repository.ValueRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.ObjectRecordService;
import de.bentrm.datacat.catalog.service.OrderedValueRecordService;
import de.bentrm.datacat.catalog.service.ValueRecordService;

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
import org.springframework.data.neo4j.core.Neo4jTemplate;

@Service
@Validated
@Transactional(readOnly = true)
public class ValueRecordServiceImpl extends AbstractSimpleRecordServiceImpl<XtdValue, ValueRepository>
        implements ValueRecordService {

    @Autowired
    private ObjectRecordService objectRecordService;

    @Autowired
    @Lazy
    private OrderedValueRecordService orderedValueRecordService;

    public ValueRecordServiceImpl(ValueRepository repository, Neo4jTemplate neo4jTemplate,
            CatalogCleanupService cleanupService) {
        super(XtdValue.class, neo4jTemplate, repository, cleanupService);
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.Value;
    }

    @Override
    public Optional<List<XtdOrderedValue>> getOrderedValues(@NotNull XtdValue value) {
        Assert.notNull(value.getId(), "Value must be persistent.");
        final List<String> orderedValueIds = getRepository().findOrderedValueIdByValueId(value.getId());
        final Iterable<XtdOrderedValue> orderedValues = orderedValueRecordService.findAllEntitiesById(orderedValueIds);
        if (!orderedValues.iterator().hasNext()) {
            return Optional.empty();
        } else {

            return Optional.of(StreamSupport.stream(orderedValues.spliterator(), false).collect(Collectors.toList()));
        }
    }

    @Transactional
    @Override
    public @NotNull XtdValue setRelatedRecords(@NotBlank String recordId,
            @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {

        final XtdValue value = getRepository().findByIdWithDirectRelations(recordId)
                .orElseThrow(() -> new IllegalArgumentException("No record with id " + recordId + " found."));
        objectRecordService.setRelatedRecords(recordId, relatedRecordIds, relationType);
        return value;
    }
}
