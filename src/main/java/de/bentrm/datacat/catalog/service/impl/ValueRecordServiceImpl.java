package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.*;
import de.bentrm.datacat.catalog.repository.ValueRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.ValueRecordService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

import org.springframework.data.neo4j.core.Neo4jTemplate;

@Service
@Validated
@Transactional(readOnly = true)
public class ValueRecordServiceImpl
        extends AbstractSimpleRecordServiceImpl<XtdValue, ValueRepository>
        implements ValueRecordService {

            // private final OrderedValueRepository orderedValueRepository;

    public ValueRecordServiceImpl(ValueRepository repository,
                                  Neo4jTemplate neo4jTemplate,
                                //   OrderedValueRepository orderedValueRepository,
                                  CatalogCleanupService cleanupService) {
        super(XtdValue.class, neo4jTemplate, repository, cleanupService);
        // this.orderedValueRepository = orderedValueRepository;
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.Value;
    }

    // @Override
    // public List<XtdOrderedValue> getOrderedValues(@NotNull XtdValue value) {
    //     Assert.notNull(value.getId(), "Value must be persistent.");
    //     final List<String> orderedValueIds = orderedValueRepository.findAllOrderedValueIdsAssignedToValue(value.getId());
    //     final Iterable<XtdOrderedValue> orderedValues = orderedValueRepository.findAllById(orderedValueIds);

    //     return StreamSupport
    //             .stream(orderedValues.spliterator(), false)
    //             .collect(Collectors.toList());
    // }

    @Transactional
    @Override
    public @NotNull XtdValue setRelatedRecords(@NotBlank String recordId,
                                                    @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {

        final XtdValue value = getRepository().findById(recordId).orElseThrow();

       return value;                                                 
    }
}
