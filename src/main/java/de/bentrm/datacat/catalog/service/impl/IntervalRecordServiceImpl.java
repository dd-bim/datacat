package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.domain.XtdInterval;
import de.bentrm.datacat.catalog.domain.XtdValueList;
import de.bentrm.datacat.catalog.repository.IntervalRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.IntervalRecordService;
import de.bentrm.datacat.catalog.service.ValueListRecordService;
import de.bentrm.datacat.catalog.service.dto.Relationships.IntervallMaxMinDtoProjection;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

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

            @Autowired
            private ValueListRecordService valueListRecordService;

    public IntervalRecordServiceImpl(Neo4jTemplate neo4jTemplate,
                                     IntervalRepository repository,
                                     CatalogCleanupService cleanupService) {
        super(XtdInterval.class, neo4jTemplate, repository, cleanupService);
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.Interval;
    }

    @Override
    public Optional<XtdValueList> getMinimum(XtdInterval interval) {
        Assert.notNull(interval.getId(), "Interval must be persistent.");
        final String valueListId = getRepository().findMinValueListIdAssignedToInterval(interval.getId());
        if (valueListId == null) {
            return null;
        }
        return valueListRecordService.findByIdWithDirectRelations(valueListId);
    }

    @Override
    public Optional<XtdValueList> getMaximum(XtdInterval interval) {
        Assert.notNull(interval.getId(), "Interval must be persistent.");
        final String valueListId = getRepository().findMaxValueListIdAssignedToInterval(interval.getId());
        if (valueListId == null) {
            return null;
        }
        return valueListRecordService.findByIdWithDirectRelations(valueListId);
    }

    @Transactional
    @Override
    public @NotNull XtdInterval setRelatedRecords(@NotBlank String recordId,
                                                    @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {

        final XtdInterval interval = getRepository().findByIdWithDirectRelations(recordId).orElseThrow();

        switch (relationType) {
            case Minimum -> {
                if (interval.getMinimum() != null) {
                    throw new IllegalArgumentException("Minimum already set.");
                } else if (relatedRecordIds.size() > 1) {
                    throw new IllegalArgumentException("Maximum one value list must be provided.");
                } else {
                    final XtdValueList valueList = valueListRecordService.findByIdWithDirectRelations(relatedRecordIds.get(0)).orElseThrow();
                    interval.setMinimum(valueList);
                }
                }
            case Maximum -> {
                if (interval.getMaximum() != null) {
                    throw new IllegalArgumentException("Maximum already set.");
                } else if (relatedRecordIds.size() > 1) {
                    throw new IllegalArgumentException("Maximum one value list must be provided.");
                } else {
                    final XtdValueList valueList = valueListRecordService.findByIdWithDirectRelations(relatedRecordIds.get(0)).orElseThrow();
                    interval.setMaximum(valueList);
                }
                }
            default -> log.error("Unsupported relation type: {}", relationType);
        }
        
        neo4jTemplate.saveAs(interval, IntervallMaxMinDtoProjection.class);
        log.trace("Updated interval: {}", interval);
        return interval;
    }
}
