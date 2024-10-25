package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.domain.XtdRelationshipType;
import de.bentrm.datacat.catalog.domain.XtdSubject;
import de.bentrm.datacat.catalog.repository.RelationshipTypeRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.ConceptRecordService;
import de.bentrm.datacat.catalog.service.RelationshipTypeRecordService;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Service
@Validated
@Transactional(readOnly = true)
public class RelationshipTypeRecordServiceImpl
        extends AbstractSimpleRecordServiceImpl<XtdRelationshipType, RelationshipTypeRepository>
        implements RelationshipTypeRecordService {

    private final ConceptRecordService conceptRecordService;

    public RelationshipTypeRecordServiceImpl(SessionFactory sessionFactory,
                                     RelationshipTypeRepository repository,
                                     ConceptRecordService conceptRecordService,
                                     CatalogCleanupService cleanupService) {
        super(XtdRelationshipType.class, sessionFactory, repository, cleanupService);
        this.conceptRecordService = conceptRecordService;
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.RelationshipType;
    }

    @Transactional
    @Override
    public @NotNull XtdRelationshipType setRelatedRecords(@NotBlank String recordId,
                                                    @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {

        final XtdRelationshipType relationshipType = getRepository().findById(recordId, 0).orElseThrow();

        switch (relationType) {
            default:
                conceptRecordService.setRelatedRecords(recordId, relatedRecordIds, relationType);
                break;
        }
        return relationshipType;
    }
}
