package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.domain.XtdInterval;
import de.bentrm.datacat.catalog.domain.XtdValueList;
import de.bentrm.datacat.catalog.repository.IntervalRepository;
import de.bentrm.datacat.catalog.repository.ValueListRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.IntervalRecordService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.util.Assert;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Slf4j
@Service
@Validated
@Transactional(readOnly = true)
public class IntervalRecordServiceImpl
        extends AbstractSimpleRecordServiceImpl<XtdInterval, IntervalRepository>
        implements IntervalRecordService {

        private final ValueListRepository valueListRepository;

    public IntervalRecordServiceImpl(Neo4jTemplate neo4jTemplate,
                                     IntervalRepository repository,
                                     ValueListRepository valueListRepository,
                                     CatalogCleanupService cleanupService) {
        super(XtdInterval.class, neo4jTemplate, repository, cleanupService);
        this.valueListRepository = valueListRepository;
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.Interval;
    }

    @Override
    public XtdValueList getMinimum(XtdInterval interval) {
        Assert.notNull(interval.getId(), "Interval must be persistent.");
        final String valueListId = valueListRepository.findMinValueListIdAssignedToInterval(interval.getId());
        if (valueListId == null) {
            return null;
        }
        return valueListRepository.findById(valueListId).orElse(null);
    }

    @Override
    public XtdValueList getMaximum(XtdInterval interval) {
        Assert.notNull(interval.getId(), "Interval must be persistent.");
        final String valueListId = valueListRepository.findMaxValueListIdAssignedToInterval(interval.getId());
        if (valueListId == null) {
            return null;
        }
        return valueListRepository.findById(valueListId).orElse(null);
    }

    @Transactional
    @Override
    public @NotNull XtdInterval setRelatedRecords(@NotBlank String recordId,
                                                    @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {

        final XtdInterval interval = getRepository().findById(recordId).orElseThrow();

        switch (relationType) {
            case Minimum -> {
                if (interval.getMinimum() != null) {
                    throw new IllegalArgumentException("Minimum already set.");
                } else if (relatedRecordIds.size() != 1) {
                    throw new IllegalArgumentException("Exactly one value list must be provided.");
                } else {
                    final XtdValueList valueList = valueListRepository.findById(relatedRecordIds.get(0)).orElseThrow();
                    interval.setMinimum(valueList);
                }
                }
            case Maximum -> {
                if (interval.getMaximum() != null) {
                    throw new IllegalArgumentException("Maximum already set.");
                } else if (relatedRecordIds.size() != 1) {
                    throw new IllegalArgumentException("Exactly one value list must be provided.");
                } else {
                    final XtdValueList valueList = valueListRepository.findById(relatedRecordIds.get(0)).orElseThrow();
                    interval.setMaximum(valueList);
                }
                }
            default -> log.error("Unsupported relation type: {}", relationType);
        }
        final XtdInterval persistentInterval = getRepository().save(interval);
        log.trace("Updated interval: {}", persistentInterval);
        return persistentInterval;
    }
}
