package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.*;
import de.bentrm.datacat.catalog.repository.AssignsPropertyWithValuesRepository;
import de.bentrm.datacat.catalog.repository.ObjectRepository;
import de.bentrm.datacat.catalog.repository.PropertyRepository;
import de.bentrm.datacat.catalog.repository.ValueRepository;
import de.bentrm.datacat.catalog.service.AssignsPropertyWithValuesRecordService;
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
public class AssignsPropertyWithValuesRecordServiceImpl
        extends AbstractRelationshipRecordServiceImpl<XtdRelAssignsPropertyWithValues>
        implements AssignsPropertyWithValuesRecordService {

    private final ObjectRepository objectRepository;
    private final PropertyRepository propertyRepository;
    private final ValueRepository valueRepository;

    public AssignsPropertyWithValuesRecordServiceImpl(SessionFactory sessionFactory,
                                                      AssignsPropertyWithValuesRepository repository,
                                                      CatalogCleanupService cleanupService,
                                                      ObjectRepository objectRepository,
                                                      PropertyRepository propertyRepository,
                                                      ValueRepository valueRepository) {
        super(XtdRelAssignsPropertyWithValues.class, sessionFactory, repository, cleanupService);
        this.objectRepository = objectRepository;
        this.propertyRepository = propertyRepository;
        this.valueRepository = valueRepository;
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.AssignsPropertyWithValues;
    }

    @Override
    protected void setRelatingRecord(@NotNull XtdRelAssignsPropertyWithValues relationshipRecord, @NotBlank String relatingRecordId) {
        final XtdObject relating = objectRepository
                .findById(relatingRecordId, 0)
                .orElseThrow();
        relationshipRecord.setRelatingObject(relating);
    }

    @Override
    protected void setRelatedRecords(@NotNull XtdRelAssignsPropertyWithValues relationshipRecord, @NotEmpty List<@NotBlank String> relatedRecordIds) {
//        Assert.isTrue(relatedRecordIds.size() > 1, "at least 2 related ids must be provided.");

        final XtdProperty relatedProperty = propertyRepository
                .findById(relatedRecordIds.get(0), 0)
                .orElseThrow();
        relationshipRecord.setRelatedProperty(relatedProperty);

        final List<@NotBlank String> relatedValueIds = relatedRecordIds.subList(1, relatedRecordIds.size());
        final Iterable<XtdValue> values = valueRepository.findAllById(relatedValueIds, 0);
        final List<XtdValue> relatedValues = StreamSupport
                .stream(values.spliterator(), false)
                .collect(Collectors.toList());
        relationshipRecord.getRelatedValues().clear();
        relationshipRecord.getRelatedValues().addAll(relatedValues);
    }
}
