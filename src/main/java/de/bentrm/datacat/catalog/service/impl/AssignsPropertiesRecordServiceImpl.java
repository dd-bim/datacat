package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdRelAssignsProperties;
import de.bentrm.datacat.catalog.repository.AssignsPropertiesRepository;
import de.bentrm.datacat.catalog.repository.ObjectRepository;
import de.bentrm.datacat.catalog.repository.PropertyRepository;
import de.bentrm.datacat.catalog.service.AssignsPropertiesRecordService;
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
public class AssignsPropertiesRecordServiceImpl extends AbstractRelationshipRecordServiceImpl<XtdRelAssignsProperties> implements AssignsPropertiesRecordService {

    private final ObjectRepository objectRepository;
    private final PropertyRepository propertyRepository;

    public AssignsPropertiesRecordServiceImpl(SessionFactory sessionFactory,
                                              AssignsPropertiesRepository repository,
                                              CatalogCleanupService catalogCleanupService,
                                              ObjectRepository objectRepository,
                                              PropertyRepository propertyRepository) {
        super(XtdRelAssignsProperties.class, sessionFactory, repository, catalogCleanupService);
        this.objectRepository = objectRepository;
        this.propertyRepository = propertyRepository;
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.AssignsProperties;
    }

    @Override
    protected void setRelatingRecord(@NotNull XtdRelAssignsProperties relationshipRecord, @NotBlank String relatingRecordId) {
        final XtdObject relating = objectRepository
                .findById(relatingRecordId, 0)
                .orElseThrow();
        relationshipRecord.setRelatingObject(relating);
    }

    @Override
    protected void setRelatedRecords(@NotNull XtdRelAssignsProperties relationshipRecord, @NotEmpty List<@NotBlank String> relatedRecordIds) {
        final Iterable<XtdProperty> things = propertyRepository.findAllById(relatedRecordIds, 0);
        final List<XtdProperty> relatedRecords = StreamSupport
                .stream(things.spliterator(), false)
                .collect(Collectors.toList());

        relationshipRecord.getRelatedProperties().clear();
        relationshipRecord.getRelatedProperties().addAll(relatedRecords);
    }
}
