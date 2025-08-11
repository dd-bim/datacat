package de.bentrm.datacat.catalog.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.domain.XtdRelationshipType;
import de.bentrm.datacat.catalog.repository.RelationshipTypeRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.ConceptRecordService;
import de.bentrm.datacat.catalog.service.RelationshipTypeRecordService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Service
@Validated
@Transactional(readOnly = true)
public class RelationshipTypeRecordServiceImpl
        extends AbstractSimpleRecordServiceImpl<XtdRelationshipType, RelationshipTypeRepository>
        implements RelationshipTypeRecordService {

    @Autowired
    private ConceptRecordService conceptRecordService;

    public RelationshipTypeRecordServiceImpl(Neo4jTemplate neo4jTemplate,
                                     RelationshipTypeRepository repository,
                                     CatalogCleanupService cleanupService) {
        super(XtdRelationshipType.class, neo4jTemplate, repository, cleanupService);
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.RelationshipType;
    }

    @Transactional
    @Override
    public @NotNull XtdRelationshipType setRelatedRecords(@NotBlank String recordId,
                                                    @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {

        final XtdRelationshipType relationshipType = getRepository().findByIdWithDirectRelations(recordId).orElseThrow(() -> new IllegalArgumentException("No record with id " + recordId + " found."));

        switch (relationType) {
            default -> conceptRecordService.setRelatedRecords(recordId, relatedRecordIds, relationType);
        }
        return relationshipType;
    }
}
