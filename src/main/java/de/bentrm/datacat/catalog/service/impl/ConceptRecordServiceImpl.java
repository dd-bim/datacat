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

import de.bentrm.datacat.catalog.domain.*;
import de.bentrm.datacat.catalog.repository.ConceptRepository;
import de.bentrm.datacat.catalog.repository.CountryRepository;
import de.bentrm.datacat.catalog.repository.MultiLanguageTextRepository;
import de.bentrm.datacat.catalog.repository.RootRepository;
import de.bentrm.datacat.catalog.repository.TextRepository;
import de.bentrm.datacat.catalog.service.*;
import de.bentrm.datacat.catalog.service.dto.Relationships.*;
import de.bentrm.datacat.graphql.input.AddCountryInput;
import de.bentrm.datacat.graphql.input.AddTextInput;
import de.bentrm.datacat.graphql.input.DeleteCountryOfOriginInput;
import de.bentrm.datacat.graphql.input.TranslationInput;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Validated
@Transactional(readOnly = true)
public class ConceptRecordServiceImpl extends AbstractSimpleRecordServiceImpl<XtdConcept, ConceptRepository>
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

    @Autowired
    @Lazy
    private CountryRepository countryRepository;

    @Autowired
    @Lazy
    private RootRepository rootRepository;

    public ConceptRecordServiceImpl(Neo4jTemplate neo4jTemplate, ConceptRepository repository,
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
        final List<String> conceptIds = getRepository().findAllExternalDocumentIdsAssignedToConcept(concept.getId());
        final Iterable<XtdExternalDocument> externalDocuments = externalDocumentRecordService
                .findAllEntitiesById(conceptIds);

        return StreamSupport.stream(externalDocuments.spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public List<XtdMultiLanguageText> getExamples(XtdConcept concept) {
        Assert.notNull(concept.getId(), "Concept must be persistent.");
        final List<String> conceptIds = getRepository().findAllExampleIdsAssignedToConcept(concept.getId());
        final Iterable<XtdMultiLanguageText> multiLanguageTexts = multiLanguageTextRecordService
                .findAllEntitiesById(conceptIds);

        return StreamSupport.stream(multiLanguageTexts.spliterator(), false).collect(Collectors.toList());
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

        return StreamSupport.stream(similarConcepts.spliterator(), false).collect(Collectors.toList());
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
        final Iterable<XtdMultiLanguageText> descriptions = multiLanguageTextRecordService
                .findAllEntitiesById(descriptionIds);

        return StreamSupport.stream(descriptions.spliterator(), false).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public @NotNull XtdConcept setRelatedRecords(@NotBlank String recordId,
            @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {

        final XtdConcept concept = getRepository().findByIdWithDirectRelations(recordId)
                .orElseThrow(() -> new IllegalArgumentException("No record with id " + recordId + " found."));

        switch (relationType) {
        case ReferenceDocuments -> {
            final Iterable<XtdExternalDocument> referenceDocuments = externalDocumentRecordService
                    .findAllEntitiesById(relatedRecordIds);
            final List<XtdExternalDocument> relatedReferenceDocuments = StreamSupport
                    .stream(referenceDocuments.spliterator(), false).collect(Collectors.toList());

            concept.getReferenceDocuments().clear();
            concept.getReferenceDocuments().addAll(relatedReferenceDocuments);
            neo4jTemplate.saveAs(concept, ReferenceDocumentsDtoProjection.class);
        }
        case SimilarTo -> {
            final Iterable<XtdConcept> similarConcepts = getRepository().findAllEntitiesById(relatedRecordIds);
            final List<XtdConcept> relatedSimilarConcepts = StreamSupport.stream(similarConcepts.spliterator(), false)
                    .collect(Collectors.toList());

            concept.getSimilarTo().clear();
            concept.getSimilarTo().addAll(relatedSimilarConcepts);
            neo4jTemplate.saveAs(concept, SimilarToDtoProjection.class);
        }
        default -> objectRecordService.setRelatedRecords(recordId, relatedRecordIds, relationType);
        }

        log.trace("Updated relationship: {}", concept);
        return concept;
    }

    @Transactional
    @Override
    public XtdConcept addDescription(AddTextInput input) {
        final XtdConcept item = getRepository().findByIdWithDirectRelations(input.getCatalogEntryId()).orElseThrow(
                () -> new IllegalArgumentException("No record with id " + input.getCatalogEntryId() + " found."));
        TranslationInput translation = input.getText();

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
    @Override
    public XtdConcept addExample(AddTextInput input) {
        final XtdConcept item = getRepository().findByIdWithDirectRelations(input.getCatalogEntryId()).orElseThrow(
                () -> new IllegalArgumentException("No record with id " + input.getCatalogEntryId() + " found."));
        TranslationInput translation = input.getText();

        XtdText text = createText(translation);

        XtdMultiLanguageText multiLanguage = item.getExamples().stream().findFirst().orElse(null);
        if (multiLanguage == null) {
            multiLanguage = new XtdMultiLanguageText();
            multiLanguage = multiLanguageTextRepository.save(multiLanguage);
            item.getExamples().add(multiLanguage);
            neo4jTemplate.saveAs(item, ExamplesDtoProjection.class);
        } else {
            multiLanguage = multiLanguageTextRecordService.findByIdWithDirectRelations(multiLanguage.getId())
                    .orElse(null);
        }
        multiLanguage.getTexts().add(text);

        neo4jTemplate.saveAs(multiLanguage, TextsDtoProjection.class);

        return item;
    }

    @Transactional
    @Override
    public XtdConcept addDefinition(AddTextInput input) {
        final XtdConcept item = getRepository().findByIdWithDirectRelations(input.getCatalogEntryId()).orElseThrow(
                () -> new IllegalArgumentException("No record with id " + input.getCatalogEntryId() + " found."));
        TranslationInput translation = input.getText();

        if (item.getDefinition() != null) {
            throw new IllegalArgumentException("Object already has a definition assigned.");
        } else {
            XtdText text = createText(translation);

            XtdMultiLanguageText multiLanguage = new XtdMultiLanguageText();
            multiLanguage = multiLanguageTextRepository.save(multiLanguage);
            item.setDefinition(multiLanguage);
            neo4jTemplate.saveAs(item, DefinitionDtoProjection.class);

            multiLanguage.getTexts().add(text);

            neo4jTemplate.saveAs(multiLanguage, TextsDtoProjection.class);
        }
        return item;
    }

    @Transactional
    public XtdText createText(TranslationInput translation) {

        final XtdLanguage language = languageRecordService.findByCode(translation.getLanguageTag())
                .orElseThrow(() -> new IllegalArgumentException(
                        "No language record with id " + translation.getLanguageTag() + " found."));

        XtdText text = new XtdText();
        text.setText(translation.getValue());
        text.setId(translation.getId());
        text.setLanguage(language);

        text = textRepository.save(text);
        return text;
    }

    @Transactional
    @Override
    public XtdConcept addCountryOfOrigin(AddCountryInput input) {
        final XtdConcept item = getRepository().findByIdWithDirectRelations(input.getCatalogEntryId()).orElseThrow(
                () -> new IllegalArgumentException("No record with id " + input.getCatalogEntryId() + " found."));
        final XtdCountry country = countryRepository.findByCode(input.getCountryCode()).orElseThrow(
                () -> new IllegalArgumentException("No country record with id " + input.getCountryCode() + " found."));
        if (item.getCountryOfOrigin() != null) {
            throw new IllegalArgumentException("Object already has a country of origin assigned.");
        } else {
            item.setCountryOfOrigin(country);
        }

        neo4jTemplate.saveAs(item, CountryOfOriginDtoProjection.class);

        return item;
    }

    @Transactional
    @Override
    public XtdConcept deleteCountryOfOrigin(DeleteCountryOfOriginInput input) {
        final XtdConcept item = getRepository().findByIdWithDirectRelations(input.getCatalogEntryId()).orElseThrow(
                () -> new IllegalArgumentException("No record with id " + input.getCatalogEntryId() + " found."));
        if (item.getCountryOfOrigin() == null) {
            throw new IllegalArgumentException("Object has no country of origin assigned.");
        } else {
           removeRelationship(input.getCatalogEntryId(),
                    item.getId(), SimpleRelationType.CountryOfOrigin);
        }
        return item;
    }
}
