package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.domain.XtdExternalDocument;
import de.bentrm.datacat.catalog.domain.XtdConcept;
import de.bentrm.datacat.catalog.domain.XtdLanguage;
import de.bentrm.datacat.catalog.repository.ExternalDocumentRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.ExternalDocumentRecordService;
import de.bentrm.datacat.catalog.service.LanguageRecordService;
import de.bentrm.datacat.catalog.service.dto.Relationships.LanguagesDtoProjection;
import lombok.extern.slf4j.Slf4j;
import de.bentrm.datacat.catalog.service.ConceptRecordService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

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

        @Autowired
        private ConceptRecordService conceptRecordService;

        @Autowired
        private LanguageRecordService languageRecordService;

    public ExternalDocumentRecordServiceImpl(Neo4jTemplate neo4jTemplate,
                                             ExternalDocumentRepository repository,
                                             CatalogCleanupService cleanupService) {
        super(XtdExternalDocument.class, neo4jTemplate, repository, cleanupService);
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.ExternalDocument;
    }

    @Override
    public List<XtdConcept> getConcepts(XtdExternalDocument externalDocument) {
        Assert.notNull(externalDocument.getId(), "External document must be persistent.");
        final List<String> conceptIds = getRepository()
                .findAllConceptIdsAssignedToExternalDocument(externalDocument.getId());
        final Iterable<XtdConcept> concepts = conceptRecordService.findAllEntitiesById(conceptIds);

        return StreamSupport
                .stream(concepts.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public List<XtdLanguage> getLanguages(XtdExternalDocument externalDocument) {
        Assert.notNull(externalDocument.getId(), "External document must be persistent.");
        final List<String> languageIds = getRepository()
                .findAllLanguageIdsAssignedToExternalDocument(externalDocument.getId());
        final Iterable<XtdLanguage> languages = languageRecordService.findAllEntitiesById(languageIds);

        return StreamSupport
                .stream(languages.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public @NotNull XtdExternalDocument setRelatedRecords(@NotBlank String recordId,
            @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {

        final XtdExternalDocument externalDocument = getRepository().findByIdWithDirectRelations(recordId).orElseThrow(() -> new IllegalArgumentException("No record with id " + recordId + " found."));

        switch (relationType) {
            default -> conceptRecordService.setRelatedRecords(recordId, relatedRecordIds, relationType);
        }

        neo4jTemplate.saveAs(externalDocument, LanguagesDtoProjection.class);
        log.trace("Updated external document: {}", externalDocument);
        return externalDocument;
    }
}
