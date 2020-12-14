package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.Measure;
import de.bentrm.datacat.catalog.domain.XtdRelAssignsValues;
import de.bentrm.datacat.catalog.domain.XtdValue;
import de.bentrm.datacat.catalog.repository.AssignsValuesRepository;
import de.bentrm.datacat.catalog.repository.MeasureRepository;
import de.bentrm.datacat.catalog.repository.ValueRepository;
import de.bentrm.datacat.catalog.service.AssignsValuesRecordService;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
public class AssignsValuesRecordServiceImpl
        extends AbstractRelationshipRecordServiceImpl<XtdRelAssignsValues>
        implements AssignsValuesRecordService {

    private final MeasureRepository measureWithUnitRepository;
    private final ValueRepository valueRepository;

    public AssignsValuesRecordServiceImpl(SessionFactory sessionFactory,
                                          AssignsValuesRepository repository,
                                          CatalogCleanupService cleanupService,
                                          MeasureRepository measureWithUnitRepository,
                                          ValueRepository valueRepository) {
        super(XtdRelAssignsValues.class, sessionFactory, repository, cleanupService);
        this.measureWithUnitRepository = measureWithUnitRepository;
        this.valueRepository = valueRepository;
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.AssignsValues;
    }

    @Override
    protected void setRelatingRecord(@NotNull XtdRelAssignsValues relationshipRecord, @NotBlank String relatingRecordId) {
        final Measure relating = measureWithUnitRepository
                .findById(relatingRecordId, 0)
                .orElseThrow();
        relationshipRecord.setRelatingMeasure(relating);

    }

    @Override
    protected void setRelatedRecords(@NotNull XtdRelAssignsValues relationshipRecord, @NotEmpty List<@NotBlank String> relatedRecordIds) {
        final Iterable<XtdValue> things = valueRepository.findAllById(relatedRecordIds, 0);
        final List<XtdValue> related = StreamSupport
                .stream(things.spliterator(), false)
                .collect(Collectors.toList());

        relationshipRecord.getRelatedValues().clear();
        relationshipRecord.getRelatedValues().addAll(related);
    }
}
