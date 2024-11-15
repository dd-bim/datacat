package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.domain.XtdLanguage;
import de.bentrm.datacat.catalog.repository.LanguageRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.LanguageRecordService;

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
public class LanguageRecordServiceImpl
        extends AbstractSimpleRecordServiceImpl<XtdLanguage, LanguageRepository>
        implements LanguageRecordService {

    public LanguageRecordServiceImpl(Neo4jTemplate neo4jTemplate,
                                     LanguageRepository repository,
                                     CatalogCleanupService cleanupService) {
        super(XtdLanguage.class, neo4jTemplate, repository, cleanupService);
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.Language;
    }

    @Transactional
    @Override
    public @NotNull XtdLanguage setRelatedRecords(@NotBlank String recordId,
                                                    @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {

        final XtdLanguage language = getRepository().findById(recordId).orElseThrow();

        return language;
    }
}
