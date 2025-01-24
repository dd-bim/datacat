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
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

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

    @SchemaMapping(typeName = "XtdConcept", field = "definition")
    public Optional<XtdMultiLanguageText> getDefinition(XtdConcept concept) {
        return service.getDefinition(concept);
    }

    @SchemaMapping(typeName = "XtdConcept", field = "examples")
    public List<XtdMultiLanguageText> getExamples(XtdConcept concept) {
        return service.getExamples(concept);
    }

    @SchemaMapping(typeName = "XtdConcept", field = "languageOfCreator")
    public Optional<XtdLanguage> getLanguageOfCreator(XtdConcept concept) {
        return service.getLanguageOfCreator(concept);
    }

    @SchemaMapping(typeName = "XtdConcept", field = "referenceDocuments")
    public List<XtdExternalDocument> getReferenceDocuments(XtdConcept concept) {
        return service.getReferenceDocuments(concept);
    }

    @SchemaMapping(typeName = "XtdConcept", field = "countryOfOrigin")
    public Optional<XtdCountry> getCountryOfOrigin(XtdConcept concept) {
        return service.getCountryOfOrigin(concept);
    }

    @SchemaMapping(typeName = "XtdConcept", field = "similarTo")
    public List<XtdConcept> getSimilarConcepts(XtdConcept concept) {
        return service.getSimilarConcepts(concept);
    }

    @SchemaMapping(typeName = "XtdConcept", field = "descriptions")
    public List<XtdMultiLanguageText> getDescriptions(XtdConcept concept) {
        return service.getDescriptions(concept);
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
