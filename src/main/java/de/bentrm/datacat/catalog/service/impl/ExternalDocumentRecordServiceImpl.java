package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.domain.XtdExternalDocument;
import de.bentrm.datacat.catalog.domain.XtdConcept;
import de.bentrm.datacat.catalog.domain.XtdLanguage;
import de.bentrm.datacat.catalog.domain.XtdSubject;
import de.bentrm.datacat.catalog.repository.ConceptRepository;
import de.bentrm.datacat.catalog.repository.ExternalDocumentRepository;
import de.bentrm.datacat.catalog.repository.LanguageRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.ExternalDocumentRecordService;
import lombok.extern.slf4j.Slf4j;
import de.bentrm.datacat.catalog.service.ConceptRecordService;

import org.intellij.lang.annotations.Language;
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

@Slf4j
@Service
@Validated
@Transactional(readOnly = true)
public class ExternalDocumentRecordServiceImpl
        extends AbstractSimpleRecordServiceImpl<XtdExternalDocument, ExternalDocumentRepository>
        implements ExternalDocumentRecordService {

    private final ConceptRepository conceptRepository;
    private final LanguageRepository languageRepository;
    private final ConceptRecordService conceptRecordService;

    public ExternalDocumentRecordServiceImpl(SessionFactory sessionFactory,
                                             ExternalDocumentRepository repository,
                                             ConceptRepository conceptRepository,
                                             LanguageRepository languageRepository,
                                             ConceptRecordService conceptRecordService,
                                             CatalogCleanupService cleanupService) {
        super(XtdExternalDocument.class, sessionFactory, repository, cleanupService);
        this.conceptRepository = conceptRepository;
        this.languageRepository = languageRepository;
        this.conceptRecordService = conceptRecordService;
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.ExternalDocument;
    }

    @Override
    public List<XtdConcept> getConcepts(XtdExternalDocument externalDocument) {
        Assert.notNull(externalDocument.getId(), "External document must be persistent.");
        final List<String> conceptIds = conceptRepository
                .findAllConceptIdsAssignedToExternalDocument(externalDocument.getId());
        final Iterable<XtdConcept> concepts = conceptRepository.findAllById(conceptIds);

        return StreamSupport
                .stream(concepts.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public List<XtdLanguage> getLanguages(XtdExternalDocument externalDocument) {
        Assert.notNull(externalDocument.getId(), "External document must be persistent.");
        final List<String> languageIds = languageRepository
                .findAllLanguageIdsAssignedToExternalDocument(externalDocument.getId());
        final Iterable<XtdLanguage> languages = languageRepository.findAllById(languageIds);

        return StreamSupport
                .stream(languages.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public @NotNull XtdExternalDocument setRelatedRecords(@NotBlank String recordId,
            @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {

        final XtdExternalDocument externalDocument = getRepository().findById(recordId, 0).orElseThrow();

        switch (relationType) {
            case Languages:
                final Iterable<XtdLanguage> items = languageRepository.findAllById(relatedRecordIds);
                final List<XtdLanguage> related = StreamSupport
                        .stream(items.spliterator(), false)
                        .collect(Collectors.toList());

                externalDocument.getLanguages().clear();
                externalDocument.getLanguages().addAll(related);
            default:
                conceptRecordService.setRelatedRecords(recordId, relatedRecordIds, relationType);
                break;
        }

        final XtdExternalDocument persistentExternalDocument = getRepository().save(externalDocument);
        log.trace("Updated external document: {}", persistentExternalDocument);
        return persistentExternalDocument;
    }
}
