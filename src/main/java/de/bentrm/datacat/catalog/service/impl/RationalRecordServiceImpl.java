package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.domain.XtdRational;
import de.bentrm.datacat.catalog.repository.RationalRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.RationalRecordService;

import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Service
@Validated
@Transactional(readOnly = true)
public class RationalRecordServiceImpl
        extends AbstractSimpleRecordServiceImpl<XtdRational, RationalRepository>
        implements RationalRecordService {

    public RationalRecordServiceImpl(Neo4jTemplate neo4jTemplate,
                                     RationalRepository repository,
                                     CatalogCleanupService cleanupService) {
        super(XtdRational.class, neo4jTemplate, repository, cleanupService);
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.Rational;
    }

    @Transactional
    @Override
    public @NotNull XtdRational setRelatedRecords(@NotBlank String recordId,
                                                    @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {

        final XtdRational rational = getRepository().findByIdWithDirectRelations(recordId).orElseThrow();

        return rational;
    }
}
