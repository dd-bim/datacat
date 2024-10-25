package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.domain.XtdLanguage;
import de.bentrm.datacat.catalog.domain.XtdSubject;
import de.bentrm.datacat.catalog.repository.LanguageRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.LanguageRecordService;

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
public class LanguageRecordServiceImpl
        extends AbstractSimpleRecordServiceImpl<XtdLanguage, LanguageRepository>
        implements LanguageRecordService {

    public LanguageRecordServiceImpl(SessionFactory sessionFactory,
                                     LanguageRepository repository,
                                     CatalogCleanupService cleanupService) {
        super(XtdLanguage.class, sessionFactory, repository, cleanupService);
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.Language;
    }

    @Transactional
    @Override
    public @NotNull XtdLanguage setRelatedRecords(@NotBlank String recordId,
                                                    @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {

        final XtdLanguage language = getRepository().findById(recordId, 0).orElseThrow();

        return language;
    }
}
