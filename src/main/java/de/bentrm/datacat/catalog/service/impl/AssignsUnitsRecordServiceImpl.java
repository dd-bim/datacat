package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.Measure;
import de.bentrm.datacat.catalog.domain.XtdRelAssignsUnits;
import de.bentrm.datacat.catalog.domain.XtdUnit;
import de.bentrm.datacat.catalog.repository.AssignsUnitsRepository;
import de.bentrm.datacat.catalog.repository.MeasureRepository;
import de.bentrm.datacat.catalog.repository.UnitRepository;
import de.bentrm.datacat.catalog.service.AssignsUnitsRecordService;
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
public class AssignsUnitsRecordServiceImpl
        extends AbstractRelationshipRecordServiceImpl<XtdRelAssignsUnits>
        implements AssignsUnitsRecordService {

    private final MeasureRepository measureWithUnitRepository;
    private final UnitRepository unitRepository;

    public AssignsUnitsRecordServiceImpl(SessionFactory sessionFactory,
                                         AssignsUnitsRepository repository,
                                         CatalogCleanupService cleanupService,
                                         MeasureRepository measureWithUnitRepository,
                                         UnitRepository unitRepository) {
        super(XtdRelAssignsUnits.class, sessionFactory, repository, cleanupService);
        this.measureWithUnitRepository = measureWithUnitRepository;
        this.unitRepository = unitRepository;
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.AssignsUnits;
    }

    @Override
    protected void setRelatingRecord(@NotNull XtdRelAssignsUnits relationshipRecord, @NotBlank String relatingRecordId) {
        final Measure relating = measureWithUnitRepository
                .findById(relatingRecordId, 0)
                .orElseThrow();
        relationshipRecord.setRelatingMeasure(relating);
    }

    @Override
    protected void setRelatedRecords(@NotNull XtdRelAssignsUnits relationshipRecord, @NotEmpty List<@NotBlank String> relatedRecordIds) {
        final Iterable<XtdUnit> things = unitRepository.findAllById(relatedRecordIds, 0);
        final List<XtdUnit> related = StreamSupport
                .stream(things.spliterator(), false)
                .collect(Collectors.toList());

        relationshipRecord.getRelatedUnits().clear();
        relationshipRecord.getRelatedUnits().addAll(related);
    }
}
