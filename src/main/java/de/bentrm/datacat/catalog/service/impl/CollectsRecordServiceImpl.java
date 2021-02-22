package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.XtdCollection;
import de.bentrm.datacat.catalog.domain.XtdRelCollects;
import de.bentrm.datacat.catalog.domain.XtdRoot;
import de.bentrm.datacat.catalog.repository.CollectionRepository;
import de.bentrm.datacat.catalog.repository.CollectsRepository;
import de.bentrm.datacat.catalog.repository.RootRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.CollectsRecordService;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
@Validated
@Transactional(readOnly = true)
public class CollectsRecordServiceImpl
        extends AbstractRelationshipRecordServiceImpl<XtdRelCollects, CollectsRepository>
        implements CollectsRecordService {

    private final CollectionRepository collectionRepository;
    private final RootRepository rootRepository;

    public CollectsRecordServiceImpl(SessionFactory sessionFactory,
                                     CollectsRepository repository,
                                     CatalogCleanupService cleanupService,
                                     CollectionRepository collectionRepository,
                                     RootRepository rootRepository) {
        super(XtdRelCollects.class, sessionFactory, repository, cleanupService);
        this.collectionRepository = collectionRepository;
        this.rootRepository = rootRepository;
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.Collects;
    }

//    @Transactional
//    @Override
//    public @NotNull XtdRelCollects setRelatedRecords(@NotBlank String relationshipId,
//                                                     @NotEmpty List<@NotBlank String> relatedRecordIds) {
//
//        final XtdRelCollects relationship = getRepository().findById(relationshipId, 0).orElseThrow();
//
//        final Iterable<XtdRoot> items = rootRepository.findAllById(relatedRecordIds, 0);
//        final List<XtdRoot> related = StreamSupport
//                .stream(items.spliterator(), false)
//                .collect(Collectors.toList());
//
//        relationship.getRelatedThings().clear();
//        relationship.getRelatedThings().addAll(related);
//
//        final XtdRelCollects persistentRelationship = getRepository().save(relationship);
//        log.trace("Updated relationship: {}", persistentRelationship);
//        return persistentRelationship;
//    }

    @Override
    protected void setRelatingRecord(@NotNull XtdRelCollects relationshipRecord,
                                     @NotBlank String relatingRecordId) {
        final XtdCollection relatingCatalogItem = collectionRepository
                .findById(relatingRecordId, 0)
                .orElseThrow();
        relationshipRecord.setRelatingCollection(relatingCatalogItem);
    }

    @Override
    protected void setRelatedRecords(@NotNull XtdRelCollects relationshipRecord,
                                     @NotEmpty List<@NotBlank String> relatedRecordIds) {
        final Iterable<XtdRoot> items = rootRepository.findAllById(relatedRecordIds, 0);
        final List<XtdRoot> related = StreamSupport
                .stream(items.spliterator(), false)
                .collect(Collectors.toList());
        relationshipRecord.getRelatedThings().clear();
        relationshipRecord.getRelatedThings().addAll(related);
    }
}
