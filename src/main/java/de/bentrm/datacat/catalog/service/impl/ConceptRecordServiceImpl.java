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
import de.bentrm.datacat.catalog.domain.XtdConcept;
import de.bentrm.datacat.catalog.domain.XtdCountry;
import de.bentrm.datacat.catalog.domain.XtdExternalDocument;
import de.bentrm.datacat.catalog.domain.XtdLanguage;
import de.bentrm.datacat.catalog.domain.XtdMultiLanguageText;
import de.bentrm.datacat.catalog.domain.XtdText;
import de.bentrm.datacat.catalog.repository.ConceptRepository;
import de.bentrm.datacat.catalog.repository.CountryRepository;
import de.bentrm.datacat.catalog.repository.ExternalDocumentRepository;
import de.bentrm.datacat.catalog.repository.LanguageRepository;
import de.bentrm.datacat.catalog.repository.MultiLanguageTextRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.ConceptRecordService;
import de.bentrm.datacat.catalog.service.ObjectRecordService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Validated
@Transactional(readOnly = true)
public class ConceptRecordServiceImpl
        extends AbstractSimpleRecordServiceImpl<XtdConcept, ConceptRepository>
        implements ConceptRecordService {

    private final ExternalDocumentRepository externalDocumentRepository;
    private final MultiLanguageTextRepository multiLanguageTextRepository;
    private final CountryRepository countryRepository;
    private final ConceptRepository repository;
    private final LanguageRepository languageRepository;
    private final ObjectRecordService objectRecordService;

    public ConceptRecordServiceImpl(Neo4jTemplate neo4jTemplate,
            ConceptRepository repository,
            ExternalDocumentRepository externalDocumentRepository,
            MultiLanguageTextRepository multiLanguageTextRepository,
            CountryRepository countryRepository,
            LanguageRepository languageRepository,
            ObjectRecordService objectRecordService,
            CatalogCleanupService cleanupService) {
        super(XtdConcept.class, neo4jTemplate, repository, cleanupService);
        this.externalDocumentRepository = externalDocumentRepository;
        this.multiLanguageTextRepository = multiLanguageTextRepository;
        this.countryRepository = countryRepository;
        this.repository = repository;
        this.languageRepository = languageRepository;
        this.objectRecordService = objectRecordService;
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.Concept;
    }

    @Override
    public List<XtdExternalDocument> getExternalDocuments(XtdConcept concept) {
        Assert.notNull(concept.getId(), "Concept must be persistent.");
        final List<String> conceptIds = externalDocumentRepository
                .findAllExternalDocumentIdsAssignedToConcept(concept.getId());
        final Iterable<XtdExternalDocument> externalDocuments = externalDocumentRepository.findAllById(conceptIds);

        return StreamSupport
                .stream(externalDocuments.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public List<XtdMultiLanguageText> getMultiLanguageTexts(XtdConcept concept) {
        Assert.notNull(concept.getId(), "Concept must be persistent.");
        final List<String> conceptIds = multiLanguageTextRepository
                .findAllMultiLanguageTextIdsAssignedToConcept(concept.getId());
        final Iterable<XtdMultiLanguageText> multiLanguageTexts = multiLanguageTextRepository.findAllById(conceptIds);

        return StreamSupport
                .stream(multiLanguageTexts.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public XtdCountry getCountry(XtdConcept concept) {
        Assert.notNull(concept.getId(), "Concept must be persistent.");
        final String countryId = countryRepository.findCountryIdAssignedToConcept(concept.getId());
        if (countryId == null) {
            return null;
        }
        final XtdCountry country = countryRepository.findById(countryId).orElse(null);

        return country;
    }

    @Override
    public List<XtdConcept> getSimilarConcepts(XtdConcept concept) {
        Assert.notNull(concept.getId(), "Concept must be persistent.");
        final List<String> similarConceptIds = repository.findAllConceptIdsAssignedToConcept(concept.getId());
        final Iterable<XtdConcept> similarConcepts = repository.findAllById(similarConceptIds);

        return StreamSupport
                .stream(similarConcepts.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public @NotNull XtdConcept setRelatedRecords(@NotBlank String recordId,
            @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {

        final XtdConcept concept = getRepository().findById(recordId).orElseThrow();

        switch (relationType) {
            case Definition -> {
                if (concept.getDefinition() != null) {
                    throw new IllegalArgumentException("Concept already has a definition.");
                } else if (relatedRecordIds.size() != 1) {
                    throw new IllegalArgumentException("Exactly one definition must be given.");
                } else {
                    final XtdMultiLanguageText definition = multiLanguageTextRepository
                            .findById(relatedRecordIds.get(0)).orElseThrow();
                    concept.setDefinition(definition);
                }
            }
            case Examples -> {
                final Iterable<XtdMultiLanguageText> examples = multiLanguageTextRepository
                        .findAllById(relatedRecordIds);
                final List<XtdMultiLanguageText> relatedExamples = StreamSupport
                        .stream(examples.spliterator(), false)
                        .collect(Collectors.toList());

                concept.getExamples().clear();
                concept.getExamples().addAll(relatedExamples);
            }
            case LanguageOfCreator -> {
                if (concept.getLanguageOfCreator() != null) {
                    throw new IllegalArgumentException("Concept already has a language of creator.");
                } else if (relatedRecordIds.size() != 1) {
                    throw new IllegalArgumentException("Exactly one language of creator must be given.");
                } else {
                    final XtdLanguage languageOfCreator = languageRepository.findById(relatedRecordIds.get(0))
                            .orElseThrow();
                    concept.setLanguageOfCreator(languageOfCreator);
                }
            }
            case ReferenceDocuments -> {
                final Iterable<XtdExternalDocument> referenceDocuments = externalDocumentRepository
                        .findAllById(relatedRecordIds);
                final List<XtdExternalDocument> relatedReferenceDocuments = StreamSupport
                        .stream(referenceDocuments.spliterator(), false)
                        .collect(Collectors.toList());

                concept.getDocumentedBy().clear();
                concept.getDocumentedBy().addAll(relatedReferenceDocuments);
            }
            case Descriptions -> {
                final Iterable<XtdMultiLanguageText> descriptions = multiLanguageTextRepository
                        .findAllById(relatedRecordIds);
                final List<XtdMultiLanguageText> relatedDescriptions = StreamSupport
                        .stream(descriptions.spliterator(), false)
                        .collect(Collectors.toList());

                concept.getDescriptions().clear();
                concept.getDescriptions().addAll(relatedDescriptions);
            }
            case SimilarTo -> {
                final Iterable<XtdConcept> similarConcepts = repository.findAllById(relatedRecordIds);
                final List<XtdConcept> relatedSimilarConcepts = StreamSupport
                        .stream(similarConcepts.spliterator(), false)
                        .collect(Collectors.toList());

                concept.getSimilarTo().clear();
                concept.getSimilarTo().addAll(relatedSimilarConcepts);
            }
            case CountryOfOrigin -> {
                if (concept.getCountryOfOrigin() != null) {
                    throw new IllegalArgumentException("Concept already has a country of origin.");
                } else if (relatedRecordIds.size() != 1) {
                    throw new IllegalArgumentException("Exactly one country of origin must be given.");
                } else {
                    final XtdCountry countryOfOrigin = countryRepository.findById(relatedRecordIds.get(0))
                            .orElseThrow();
                    concept.setCountryOfOrigin(countryOfOrigin);
                }
            }
            default -> objectRecordService.setRelatedRecords(recordId, relatedRecordIds, relationType);
        }

        final XtdConcept persistentConcept = getRepository().save(concept);
        log.info("Updated relationship: {}", persistentConcept);
        return persistentConcept;
    }

    @Transactional
    @Override
    public XtdConcept addDescription(String id, String descriptionId, String languageTag, String value) {
        final XtdConcept item = repository.findById(id).orElseThrow();
        final XtdLanguage language = languageRepository.findByCode(languageTag);

        final XtdText text = new XtdText();
        text.setText(value);
        text.setId(descriptionId);
        text.setLanguage(language);

        XtdMultiLanguageText multiLanguage = item.getDescriptions().stream().findFirst().orElse(null);
        if (multiLanguage == null) {
            multiLanguage = new XtdMultiLanguageText();
        }
        multiLanguage.getTexts().add(text);

        item.getDescriptions().clear();
        item.getDescriptions().add(multiLanguage);

        return repository.save(item);
    }
}
