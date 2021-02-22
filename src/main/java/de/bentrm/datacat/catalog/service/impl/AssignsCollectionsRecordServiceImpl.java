package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.XtdCollection;
import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.catalog.domain.XtdRelAssignsCollections;
import de.bentrm.datacat.catalog.repository.AssignsCollectionsRepository;
import de.bentrm.datacat.catalog.repository.CollectionRepository;
import de.bentrm.datacat.catalog.repository.ObjectRepository;
import de.bentrm.datacat.catalog.service.AssignsCollectionsRecordService;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
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

@Service
@Validated
@Transactional(readOnly = true)
public class AssignsCollectionsRecordServiceImpl
        extends AbstractRelationshipRecordServiceImpl<XtdRelAssignsCollections, AssignsCollectionsRepository>
        implements AssignsCollectionsRecordService {

    private final ObjectRepository objectRepository;
    private final CollectionRepository collectionRepository;

    public AssignsCollectionsRecordServiceImpl(SessionFactory sessionFactory,
                                               AssignsCollectionsRepository repository,
                                               CatalogCleanupService cleanupService,
                                               ObjectRepository objectRepository,
                                               CollectionRepository collectionRepository) {
        super(XtdRelAssignsCollections.class, sessionFactory, repository, cleanupService);
        this.objectRepository = objectRepository;
        this.collectionRepository = collectionRepository;
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.AssignsCollections;
    }

    @Override
    protected void setRelatingRecord(@NotNull XtdRelAssignsCollections relationshipRecord,
                                     @NotBlank String relatingRecordId) {
        final XtdObject relating = objectRepository
                .findById(relatingRecordId, 0)
                .orElseThrow();
        relationshipRecord.setRelatingObject(relating);
    }

    @Override
    protected void setRelatedRecords(@NotNull XtdRelAssignsCollections relationshipRecord,
                                     @NotEmpty List<@NotBlank String> relatedRecordIds) {
        Assert.notEmpty(relatedRecordIds, "a relationship record must related with at least one other concept");

        final Iterable<XtdCollection> things = collectionRepository.findAllById(relatedRecordIds, 0);
        final List<XtdCollection> relatedRecords = StreamSupport
                .stream(things.spliterator(), false)
                .collect(Collectors.toList());

        Assert.isTrue(relatedRecordIds.size() == relatedRecords.size(), "not all related records have been found");

        relationshipRecord.getRelatedCollections().clear();
        relationshipRecord.getRelatedCollections().addAll(relatedRecords);
    }
}
