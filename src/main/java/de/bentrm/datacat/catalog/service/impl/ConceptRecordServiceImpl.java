package de.bentrm.datacat.catalog.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
import de.bentrm.datacat.catalog.repository.MultiLanguageTextRepository;
import de.bentrm.datacat.catalog.repository.TextRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.ConceptRecordService;
import de.bentrm.datacat.catalog.service.CountryRecordService;
import de.bentrm.datacat.catalog.service.ExternalDocumentRecordService;
import de.bentrm.datacat.catalog.service.LanguageRecordService;
import de.bentrm.datacat.catalog.service.MultiLanguageTextRecordService;
import de.bentrm.datacat.catalog.service.ObjectRecordService;
import de.bentrm.datacat.catalog.service.dto.Relationships.CountryOfOriginDtoProjection;
import de.bentrm.datacat.catalog.service.dto.Relationships.DefinitionDtoProjection;
import de.bentrm.datacat.catalog.service.dto.Relationships.DescriptionsDtoProjection;
import de.bentrm.datacat.catalog.service.dto.Relationships.ExamplesDtoProjection;
import de.bentrm.datacat.catalog.service.dto.Relationships.LanguageOfCreatorDtoProjection;
import de.bentrm.datacat.catalog.service.dto.Relationships.ReferenceDocumentsDtoProjection;
import de.bentrm.datacat.catalog.service.dto.Relationships.SimilarToDtoProjection;
import de.bentrm.datacat.catalog.service.dto.Relationships.TextsDtoProjection;
import de.bentrm.datacat.graphql.input.AddDescriptionInput;
import de.bentrm.datacat.graphql.input.TranslationInput;
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

    @Autowired
    @Lazy
    private ExternalDocumentRecordService externalDocumentRecordService;

    @Autowired
    @Lazy
    private MultiLanguageTextRecordService multiLanguageTextRecordService;

    @Autowired
    @Lazy
    private CountryRecordService countryRecordService;

    @Autowired
    @Lazy
    private LanguageRecordService languageRecordService;

    @Autowired
    @Lazy
    private ObjectRecordService objectRecordService;

    @Autowired
    @Lazy
    private TextRepository textRepository;

    @Autowired
    @Lazy
    private MultiLanguageTextRepository multiLanguageTextRepository;


    public ConceptRecordServiceImpl(Neo4jTemplate neo4jTemplate,
            ConceptRepository repository,
            CatalogCleanupService cleanupService) {
        super(XtdConcept.class, neo4jTemplate, repository, cleanupService);
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.Concept;
    }

    @Override
    public Optional<XtdMultiLanguageText> getDefinition(XtdConcept concept) {
        Assert.notNull(concept.getId(), "Concept must be persistent.");
        final String definitionId = getRepository().findDefinitionIdAssignedToConcept(concept.getId());
        if (definitionId == null) {
            return null;
        }
        final Optional<XtdMultiLanguageText> definition = multiLanguageTextRecordService.findById(definitionId);

        return definition;
    }

    @Override
    public List<XtdExternalDocument> getReferenceDocuments(XtdConcept concept) {
        Assert.notNull(concept.getId(), "Concept must be persistent.");
        final List<String> conceptIds = getRepository()
                .findAllExternalDocumentIdsAssignedToConcept(concept.getId());
        final Iterable<XtdExternalDocument> externalDocuments = externalDocumentRecordService.findAllEntitiesById(conceptIds);

        return StreamSupport
                .stream(externalDocuments.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public List<XtdMultiLanguageText> getExamples(XtdConcept concept) {
        Assert.notNull(concept.getId(), "Concept must be persistent.");
        final List<String> conceptIds = getRepository()
                .findAllExampleIdsAssignedToConcept(concept.getId());
        final Iterable<XtdMultiLanguageText> multiLanguageTexts = multiLanguageTextRecordService.findAllEntitiesById(conceptIds);

        return StreamSupport
                .stream(multiLanguageTexts.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<XtdCountry> getCountryOfOrigin(XtdConcept concept) {
        Assert.notNull(concept.getId(), "Concept must be persistent.");
        final String countryId = getRepository().findCountryIdAssignedToConcept(concept.getId());
        if (countryId == null) {
            return null;
        }
        final Optional<XtdCountry> country = countryRecordService.findByIdWithDirectRelations(countryId);

        return country;
    }

    @Override
    public List<XtdConcept> getSimilarConcepts(XtdConcept concept) {
        Assert.notNull(concept.getId(), "Concept must be persistent.");
        final List<String> similarConceptIds = getRepository().findAllConceptIdsAssignedToConcept(concept.getId());
        final Iterable<XtdConcept> similarConcepts = getRepository().findAllEntitiesById(similarConceptIds);

        return StreamSupport
                .stream(similarConcepts.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<XtdLanguage> getLanguageOfCreator(XtdConcept concept) {
        Assert.notNull(concept.getId(), "Concept must be persistent.");
        final String languageId = getRepository().findLanguageIdAssignedToConcept(concept.getId());
        if (languageId == null) {
            return null;
        }
        final Optional<XtdLanguage> languageOfCreator = languageRecordService.findByIdWithDirectRelations(languageId);

        return languageOfCreator;
    }

    @Override
    public List<XtdMultiLanguageText> getDescriptions(XtdConcept concept) {
        Assert.notNull(concept.getId(), "Concept must be persistent.");
        final List<String> descriptionIds = getRepository().findAllDescriptionIdsAssignedToConcept(concept.getId());
        final Iterable<XtdMultiLanguageText> descriptions = multiLanguageTextRecordService.findAllEntitiesById(descriptionIds);

        return StreamSupport
                .stream(descriptions.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public @NotNull XtdConcept setRelatedRecords(@NotBlank String recordId,
            @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {

        final XtdConcept concept = getRepository().findByIdWithDirectRelations(recordId).orElseThrow(() -> new IllegalArgumentException("No record with id " + recordId + " found."));

        switch (relationType) {
            case Definition -> {
                if (concept.getDefinition() != null) {
                    throw new IllegalArgumentException("Concept already has a definition.");
                } else if (relatedRecordIds.size() != 1) {
                    throw new IllegalArgumentException("Exactly one definition must be given.");
                } else {
                    final XtdMultiLanguageText definition = multiLanguageTextRecordService
                            .findByIdWithDirectRelations(relatedRecordIds.get(0)).orElseThrow(() -> new IllegalArgumentException("No record with id " + relatedRecordIds.get(0) + " found."));
                    concept.setDefinition(definition);
                }
                neo4jTemplate.saveAs(concept, DefinitionDtoProjection.class);
            }
            case Examples -> {
                final Iterable<XtdMultiLanguageText> examples = multiLanguageTextRecordService
                        .findAllEntitiesById(relatedRecordIds);
                final List<XtdMultiLanguageText> relatedExamples = StreamSupport
                        .stream(examples.spliterator(), false)
                        .collect(Collectors.toList());

                concept.getExamples().clear();
                concept.getExamples().addAll(relatedExamples);
                neo4jTemplate.saveAs(concept, ExamplesDtoProjection.class);
            }
            case LanguageOfCreator -> {
                if (concept.getLanguageOfCreator() != null) {
                    throw new IllegalArgumentException("Concept already has a language of creator.");
                } else if (relatedRecordIds.size() != 1) {
                    throw new IllegalArgumentException("Exactly one language of creator must be given.");
                } else {
                    final XtdLanguage languageOfCreator = languageRecordService.findByIdWithDirectRelations(relatedRecordIds.get(0))
                            .orElseThrow(() -> new IllegalArgumentException("No record with id " + relatedRecordIds.get(0) + " found."));
                    concept.setLanguageOfCreator(languageOfCreator);
                }
                neo4jTemplate.saveAs(concept, LanguageOfCreatorDtoProjection.class);
            }
            case ReferenceDocuments -> {
                final Iterable<XtdExternalDocument> referenceDocuments = externalDocumentRecordService
                        .findAllEntitiesById(relatedRecordIds);
                final List<XtdExternalDocument> relatedReferenceDocuments = StreamSupport
                        .stream(referenceDocuments.spliterator(), false)
                        .collect(Collectors.toList());

                concept.getReferenceDocuments().clear();
                concept.getReferenceDocuments().addAll(relatedReferenceDocuments);
                neo4jTemplate.saveAs(concept, ReferenceDocumentsDtoProjection.class);
            }
            case Descriptions -> {
                final Iterable<XtdMultiLanguageText> descriptions = multiLanguageTextRecordService
                        .findAllEntitiesById(relatedRecordIds);
                final List<XtdMultiLanguageText> relatedDescriptions = StreamSupport
                        .stream(descriptions.spliterator(), false)
                        .collect(Collectors.toList());

                concept.getDescriptions().clear();
                concept.getDescriptions().addAll(relatedDescriptions);
                neo4jTemplate.saveAs(concept, DescriptionsDtoProjection.class);
            }
            case SimilarTo -> {
                final Iterable<XtdConcept> similarConcepts = getRepository().findAllEntitiesById(relatedRecordIds);
                final List<XtdConcept> relatedSimilarConcepts = StreamSupport
                        .stream(similarConcepts.spliterator(), false)
                        .collect(Collectors.toList());

                concept.getSimilarTo().clear();
                concept.getSimilarTo().addAll(relatedSimilarConcepts);
                neo4jTemplate.saveAs(concept, SimilarToDtoProjection.class);
            }
            case CountryOfOrigin -> {
                if (concept.getCountryOfOrigin() != null) {
                    throw new IllegalArgumentException("Concept already has a country of origin.");
                } else if (relatedRecordIds.size() != 1) {
                    throw new IllegalArgumentException("Exactly one country of origin must be given.");
                } else {
                    final XtdCountry countryOfOrigin = countryRecordService.findByIdWithDirectRelations(relatedRecordIds.get(0))
                            .orElseThrow(() -> new IllegalArgumentException("No record with id " + relatedRecordIds.get(0) + " found."));
                    concept.setCountryOfOrigin(countryOfOrigin);
                }
                neo4jTemplate.saveAs(concept, CountryOfOriginDtoProjection.class);
            }
            default -> objectRecordService.setRelatedRecords(recordId, relatedRecordIds, relationType);
        }

        log.trace("Updated relationship: {}", concept);
        return concept;
    }

    @Transactional
    @Override
    public XtdConcept addDescription(AddDescriptionInput input) {
        final XtdConcept item = getRepository().findByIdWithDirectRelations(input.getCatalogEntryId()).orElseThrow(() -> new IllegalArgumentException("No record with id " + input.getCatalogEntryId() + " found."));
        TranslationInput translation = input.getDescription();

        XtdText text = createText(translation);       

        XtdMultiLanguageText multiLanguage = item.getDescriptions().stream().findFirst().orElse(null);
        if (multiLanguage == null) {
            multiLanguage = new XtdMultiLanguageText();
            multiLanguage = multiLanguageTextRepository.save(multiLanguage);
            item.getDescriptions().add(multiLanguage);
            neo4jTemplate.saveAs(item, DescriptionsDtoProjection.class);
        } else {
            multiLanguage = multiLanguageTextRecordService.findByIdWithDirectRelations(multiLanguage.getId())
                    .orElse(null);
        }
        multiLanguage.getTexts().add(text);

        neo4jTemplate.saveAs(multiLanguage, TextsDtoProjection.class);

        return item;
    }

    @Transactional
    public XtdText createText(TranslationInput translation) {

        final XtdLanguage language = languageRecordService.findByCode(translation.getLanguageTag()).orElseThrow(() -> new IllegalArgumentException("No language record with id " + translation.getLanguageTag() + " found."));

        XtdText text = new XtdText();
        text.setText(translation.getValue());
        text.setId(translation.getId());
        text.setLanguage(language);

        text = textRepository.save(text);
        return text;
    }
}
