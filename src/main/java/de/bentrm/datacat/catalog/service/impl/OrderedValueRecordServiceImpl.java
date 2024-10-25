package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.domain.XtdOrderedValue;
import de.bentrm.datacat.catalog.domain.XtdSubject;
import de.bentrm.datacat.catalog.domain.XtdValueList;
import de.bentrm.datacat.catalog.domain.XtdValue;
import de.bentrm.datacat.catalog.repository.ValueListRepository;
import de.bentrm.datacat.catalog.repository.ValueRepository;
import de.bentrm.datacat.catalog.repository.OrderedValueRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.OrderedValueRecordService;
import de.bentrm.datacat.catalog.service.ValueListRecordService;
import lombok.extern.slf4j.Slf4j;

import org.neo4j.ogm.session.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JacksonInject.Value;
import com.fasterxml.jackson.databind.annotation.JsonAppend.Prop;

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
public class OrderedValueRecordServiceImpl
        extends AbstractSimpleRecordServiceImpl<XtdOrderedValue, OrderedValueRepository>
        implements OrderedValueRecordService {

            private final ValueRepository valueRepository;
            private final ValueListRepository valueListRepository;

    public OrderedValueRecordServiceImpl(SessionFactory sessionFactory,
                                    OrderedValueRepository repository,
                                    ValueListRepository valueListRepository,
                                    ValueRepository valueRepository,
                                    CatalogCleanupService cleanupService) {
        super(XtdOrderedValue.class, sessionFactory, repository, cleanupService);
        this.valueRepository = valueRepository;
        this.valueListRepository = valueListRepository;
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.OrderedValue;
    }

    @Override
    public XtdValue getValue(XtdOrderedValue orderedValue) {
        Assert.notNull(orderedValue.getId(), "OrderedValue must be persistent.");
        final String valueId = valueRepository.findValueIdAssignedToOrderedValue(orderedValue.getId());
        if (valueId == null) {
            return null;
        }
        final XtdValue value = valueRepository.findById(valueId).orElse(null);

        return value;
    }

    @Override
    public List<XtdValueList> getValueLists(@NotNull XtdOrderedValue orderedValue) {
        Assert.notNull(orderedValue.getId(), "OrderedValue must be persistent.");
        final List<String> valueListIds = valueListRepository.findAllValueListIdsAssignedToOrderedValue(orderedValue.getId());
        final Iterable<XtdValueList> valueLists = valueListRepository.findAllById(valueListIds);

        return StreamSupport
                .stream(valueLists.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public @NotNull XtdOrderedValue setRelatedRecords(@NotBlank String recordId,
                                                    @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {

        final XtdOrderedValue orderedValue = getRepository().findById(recordId, 0).orElseThrow();

        switch (relationType) {
            case Value:
                if (orderedValue.getOrderedValue() != null) {
                    throw new IllegalArgumentException("OrderedValue already has a Value assigned.");
                } else if (relatedRecordIds.size() != 1) {
                    throw new IllegalArgumentException("Exactly one Value must be assigned to an OrderedValue.");
                } else {
                    final XtdValue value = valueRepository.findById(relatedRecordIds.get(0)).orElseThrow();
                    orderedValue.setOrderedValue(value);
                }
            default:
                log.error("Unsupported relation type: {}", relationType);
                break;
        }   
        
        final XtdOrderedValue persistentOrderedValue = getRepository().save(orderedValue);
        log.trace("Updated relationship: {}", persistentOrderedValue);
        return persistentOrderedValue;
    }
}
