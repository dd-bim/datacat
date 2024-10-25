package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.*;
import de.bentrm.datacat.catalog.repository.OrderedValueRepository;
import de.bentrm.datacat.catalog.repository.ValueListRepository;
import de.bentrm.datacat.catalog.repository.ValueRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.ValueRecordService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.core.Ordered;
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

@Service
@Validated
@Transactional(readOnly = true)
public class ValueRecordServiceImpl
        extends AbstractSimpleRecordServiceImpl<XtdValue, ValueRepository>
        implements ValueRecordService {

            // private final OrderedValueRepository orderedValueRepository;

    public ValueRecordServiceImpl(SessionFactory sessionFactory,
                                  ValueRepository repository,
                                //   OrderedValueRepository orderedValueRepository,
                                  CatalogCleanupService cleanupService) {
        super(XtdValue.class, sessionFactory, repository, cleanupService);
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

        final XtdValue value = getRepository().findById(recordId, 0).orElseThrow();

       return value;                                                 
    }
}
