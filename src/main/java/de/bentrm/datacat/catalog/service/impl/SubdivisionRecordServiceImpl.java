package de.bentrm.datacat.catalog.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.domain.XtdSubdivision;
import de.bentrm.datacat.catalog.repository.SubdivisionRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.ConceptRecordService;
import de.bentrm.datacat.catalog.service.SubdivisionRecordService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Validated
@Transactional(readOnly = true)
public class SubdivisionRecordServiceImpl
        extends AbstractSimpleRecordServiceImpl<XtdSubdivision, SubdivisionRepository>
        implements SubdivisionRecordService {

            private final SubdivisionRepository repository;
            private final ConceptRecordService conceptRecordService;

    public SubdivisionRecordServiceImpl(Neo4jTemplate neo4jTemplate,
                                     SubdivisionRepository repository,
                                     ConceptRecordService conceptRecordService,
                                     CatalogCleanupService cleanupService) {
        super(XtdSubdivision.class, neo4jTemplate, repository, cleanupService);
        this.repository = repository;
        this.conceptRecordService = conceptRecordService;
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.Subdivision;
    }

    @Override
    public List<XtdSubdivision> getSubdivisions(XtdSubdivision subdivision) {
        Assert.notNull(subdivision.getId(), "Subdivision must be persistent.");
        final List<String> subdivisionIds = repository.findAllSubdivisionIdsAssignedToSubdivision(subdivision.getId());
        final Iterable<XtdSubdivision> subdivisions = repository.findAllById(subdivisionIds);

        return StreamSupport
                .stream(subdivisions.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public @NotNull XtdSubdivision setRelatedRecords(@NotBlank String recordId,
                                                    @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {

        final XtdSubdivision subdivision = getRepository().findById(recordId).orElseThrow();

        switch (relationType) {
            case Subdivisions -> {
                final Iterable<XtdSubdivision> items = repository.findAllById(relatedRecordIds);
                final List<XtdSubdivision> related = StreamSupport
                        .stream(items.spliterator(), false)
                        .collect(Collectors.toList());

                subdivision.getSubdivisions().clear();
                subdivision.getSubdivisions().addAll(related);
                    }
        
            default -> conceptRecordService.setRelatedRecords(recordId, relatedRecordIds, relationType);
        }

        final XtdSubdivision persistentSubdivision = getRepository().save(subdivision);
        log.trace("Updated relationship: {}", persistentSubdivision);
        return persistentSubdivision;
    }
}
