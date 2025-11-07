package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdConcept;
import de.bentrm.datacat.catalog.domain.XtdCountry;
import de.bentrm.datacat.catalog.domain.XtdExternalDocument;
import de.bentrm.datacat.catalog.domain.XtdLanguage;
import de.bentrm.datacat.catalog.domain.XtdMultiLanguageText;
import de.bentrm.datacat.catalog.domain.XtdText;
import de.bentrm.datacat.catalog.service.ConceptRecordService;
import de.bentrm.datacat.catalog.specification.CatalogRecordSpecification;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.FilterInput;
import de.bentrm.datacat.graphql.dto.SpecificationMapper;
import de.bentrm.datacat.graphql.input.LocalizationInput;
import de.bentrm.datacat.util.LocalizationUtils;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class ConceptController {

    @Autowired
    private ConceptRecordService service;

    @Autowired
    private SpecificationMapper specificationMapper;

    @QueryMapping
    public Optional<XtdConcept> getConcept(@Argument String id) {
        return service.findByIdWithDirectRelations(id, XtdConcept.class.getSimpleName());
    }

    @QueryMapping
    public Connection<XtdConcept> findConcepts(@Argument FilterInput input) {
        if (input == null)
            input = new FilterInput();
        final CatalogRecordSpecification specification = specificationMapper.toCatalogRecordSpecification(input);
        final Page<XtdConcept> page = service.findAll(specification);
        return Connection.of(page);
    }

    @BatchMapping(typeName = "XtdConcept", field = "definition")
    public Map<XtdConcept, Optional<XtdMultiLanguageText>> getDefinition(List<XtdConcept> concepts) {
        return concepts.stream()
                .filter(concept -> concept != null)  // Filter out null concepts
                .collect(Collectors.toMap(
                        concept -> concept,
                        concept -> {
                            Optional<XtdMultiLanguageText> result = service.getDefinition(concept);
                            return result != null ? result : Optional.empty();  // Handle null Optional
                        }
                ));                
    }

    @BatchMapping(typeName = "XtdConcept", field = "examples")
    public Map<XtdConcept, List<XtdMultiLanguageText>> getExamples(List<XtdConcept> concepts) {
        return concepts.stream()
                .filter(concept -> concept != null)  // Filter out null concepts
                .collect(Collectors.toMap(
                        concept -> concept,
                        concept -> {
                            List<XtdMultiLanguageText> result = service.getExamples(concept);
                            return result != null ? result : new ArrayList<>();  // Handle null result
                        }
                ));                
    }

    @BatchMapping(typeName = "XtdConcept", field = "languageOfCreator")
    public Map<XtdConcept, Optional<XtdLanguage>> getLanguageOfCreator(List<XtdConcept> concepts) {
        return concepts.stream()
                .filter(concept -> concept != null)  // Filter out null concepts
                .collect(Collectors.toMap(
                        concept -> concept,
                        concept -> {
                            Optional<XtdLanguage> result = service.getLanguageOfCreator(concept);
                            return result != null ? result : Optional.empty();  // Handle null Optional
                        }
                ));                
    }

    @BatchMapping(typeName = "XtdConcept", field = "referenceDocuments")
    public Map<XtdConcept, List<XtdExternalDocument>> getReferenceDocuments(List<XtdConcept> concepts) {
        return concepts.stream()
                .filter(concept -> concept != null)  // Filter out null concepts
                .collect(Collectors.toMap(
                        concept -> concept,
                        concept -> {
                            List<XtdExternalDocument> result = service.getReferenceDocuments(concept);
                            return result != null ? result : new ArrayList<>();  // Handle null result
                        }
                ));                
    }

    @BatchMapping(typeName = "XtdConcept", field = "countryOfOrigin")
    public Map<XtdConcept, Optional<XtdCountry>> getCountryOfOrigin(List<XtdConcept> concepts) {
        return concepts.stream()
                .filter(concept -> concept != null)  // Filter out null concepts
                .collect(Collectors.toMap(
                        concept -> concept,
                        concept -> {
                            Optional<XtdCountry> result = service.getCountryOfOrigin(concept);
                            return result != null ? result : Optional.empty();  // Handle null Optional
                        }
                ));                
    }

    @BatchMapping(typeName = "XtdConcept", field = "similarTo")
    public Map<XtdConcept, List<XtdConcept>> getSimilarConcepts(List<XtdConcept> concepts) {
        return concepts.stream()
                .filter(concept -> concept != null)  // Filter out null concepts
                .collect(Collectors.toMap(
                        concept -> concept,
                        concept -> {
                            List<XtdConcept> result = service.getSimilarConcepts(concept);
                            return result != null ? result : new ArrayList<>();  // Handle null result
                        }
                ));                
    }

    @BatchMapping(typeName = "XtdConcept", field = "descriptions")
    public Map<XtdConcept, List<XtdMultiLanguageText>> getDescriptions(List<XtdConcept> concepts) {
        return concepts.stream()
                .filter(concept -> concept != null)  // Filter out null concepts
                .collect(Collectors.toMap(
                        concept -> concept,
                        concept -> {
                            List<XtdMultiLanguageText> result = service.getDescriptions(concept);
                            return result != null ? result : new ArrayList<>();  // Handle null result
                        }
                ));                
    }

    @SchemaMapping(typeName = "XtdConcept", field = "description")
    public Optional<String> getDescription(XtdConcept concept, @Argument LocalizationInput input) {

        XtdMultiLanguageText description = service.getDescriptions(concept).stream().findFirst().orElse(null);
        if (description == null) {
            return null;
        }

        XtdText translation = null;
        if (input != null && input.getPriorityList() != null) {
            translation = LocalizationUtils.getTranslation(input.getPriorityList(), description.getId());
        } else {
            translation = LocalizationUtils.getTranslation(description.getId());
        }

        return Optional.ofNullable(translation != null ? translation.getText() : null);
    }
}
