package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.Measure;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdRelAssignsMeasures;
import de.bentrm.datacat.catalog.repository.AssignsMeasuresRepository;
import de.bentrm.datacat.catalog.repository.MeasureRepository;
import de.bentrm.datacat.catalog.repository.PropertyRepository;
import de.bentrm.datacat.catalog.service.AssignsMeasuresRecordService;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.session.SessionFactory;
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

@Slf4j
@Service
@Validated
@Transactional(readOnly = true)
public class AssignsMeasuresRecordServiceImpl
        extends AbstractRelationshipRecordServiceImpl<XtdRelAssignsMeasures>
        implements AssignsMeasuresRecordService {

    private final PropertyRepository propertyRepository;
    private final MeasureRepository measureRepository;

    public AssignsMeasuresRecordServiceImpl(SessionFactory sessionFactory,
                                            AssignsMeasuresRepository repository,
                                            CatalogCleanupService cleanupService,
                                            PropertyRepository propertyRepository,
                                            MeasureRepository measureRepository) {
        super(XtdRelAssignsMeasures.class, sessionFactory, repository, cleanupService);
        this.propertyRepository = propertyRepository;
        this.measureRepository = measureRepository;
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.AssignsMeasures;
    }

    @Override
    protected void setRelatingRecord(@NotNull XtdRelAssignsMeasures relationshipRecord, @NotBlank String relatingRecordId) {
        final XtdProperty relating = propertyRepository
                .findById(relatingRecordId)
                .orElseThrow();
        relationshipRecord.setRelatingProperty(relating);
    }

    @Override
    protected void setRelatedRecords(@NotNull XtdRelAssignsMeasures relationshipRecord, @NotEmpty List<@NotBlank String> relatedRecordIds) {
        final Iterable<Measure> things = measureRepository
                .findAllById(relatedRecordIds, 0);
        final List<Measure> related = StreamSupport
                .stream(things.spliterator(), false)
                .collect(Collectors.toList());

        Assert.isTrue(relatedRecordIds.size() == related.size(), "not all related records have been found");

        relationshipRecord.getRelatedMeasures().clear();
        relationshipRecord.getRelatedMeasures().addAll(related);
    }
}
