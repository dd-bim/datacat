package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.Tag;
import de.bentrm.datacat.catalog.domain.XtdConcept;
import de.bentrm.datacat.catalog.domain.XtdCountry;
import de.bentrm.datacat.catalog.domain.XtdExternalDocument;
import de.bentrm.datacat.catalog.domain.XtdMultiLanguageText;
import de.bentrm.datacat.catalog.service.CatalogService;
import de.bentrm.datacat.catalog.service.ConceptRecordService;
import de.bentrm.datacat.catalog.specification.CatalogRecordSpecification;
import de.bentrm.datacat.catalog.specification.TagSpecification;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.FilterInput;
import de.bentrm.datacat.graphql.dto.SpecificationMapper;
import de.bentrm.datacat.graphql.fetcher.delegate.ObjectFetchersDelegate;
import de.bentrm.datacat.graphql.fetcher.delegate.RootFetchersDelegate;
import graphql.schema.DataFetcher;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Controller
public class ConceptController {

    @Autowired
    private ConceptRecordService queryService;

    // @Autowired
    // private CatalogService catalogService;

    @Autowired
    private SpecificationMapper specificationMapper;

    // @QueryMapping
    // public List<XtdExternalDocument> getExternalDocuments(@Argument String conceptId) {
    //     return queryService.findExternalDocumentsByConceptId(conceptId);
    // }

    // @QueryMapping
    // public List<XtdMultiLanguageText> getMultiLanguageTexts(@Argument String conceptId) {
    //     return queryService.findMultiLanguageTextsByConceptId(conceptId);
    // }

    // @QueryMapping
    // public XtdCountry getCountry(@Argument String conceptId) {
    //     return queryService.findCountryByConceptId(conceptId);
    // }

    // @QueryMapping
    // public List<XtdConcept> getSimilarConcepts(@Argument String conceptId) {
    //     return queryService.findSimilarConceptsByConceptId(conceptId);
    // }

    @QueryMapping
    public Optional<XtdConcept> getConcept(@Argument String id) {
        return queryService.findById(id);
    }

    @QueryMapping
    public Connection<XtdConcept> findConcepts(@Argument FilterInput input) {
        if (input == null) input = new FilterInput();
        final CatalogRecordSpecification specification = specificationMapper.toCatalogRecordSpecification(input);
        final Page<XtdConcept> page = queryService.findAll(specification);
        return Connection.of(page);
    }

    // public ConceptController(ConceptRecordService queryService,
    //                        RootFetchersDelegate rootFetchersDelegate,
    //                        ObjectFetchersDelegate objectFetchersDelegate,
    //                        CatalogService catalogService) {
    //     super(queryService);

    //     this.externalDocuments = environment -> {
    //         final XtdConcept source = environment.getSource();
    //         return queryService.getExternalDocuments(source);
    //     };
    //     // this.externalDocuments = environment -> {
    //     //     final XtdConcept source = environment.getSource();
    //     //     final List<String> id = source.getDocumentedBy().stream().map(XtdExternalDocument::getId).collect(Collectors.toList());
    //     //     return catalogService.getAllExternalDocumentsById(id);
    //     // };

    //     this.multiLanguageTexts = environment -> {
    //         final XtdConcept source = environment.getSource();
    //         return queryService.getMultiLanguageTexts(source);
    //     };

    //     this.country = environment -> {
    //         final XtdConcept source = environment.getSource();
    //         return queryService.getCountry(source);
    //     };

    //     this.similarConcepts = environment -> {
    //         final XtdConcept source = environment.getSource();
    //         return queryService.getSimilarConcepts(source);
    //     };

    // }

    // @Override
    // public String getTypeName() {
    //     return "XtdConcept";
    // }

    // @Override
    // public String getFetcherName() {
    //     return "getConcept";
    // }

    // @Override
    // public String getListFetcherName() {
    //     return "findConcepts";
    // }

    // @Override
    // public Map<String, DataFetcher> getAttributeFetchers() {
    //     Map<String, DataFetcher> fetchers = new HashMap<>();
    //     fetchers.putAll(super.getAttributeFetchers());
    //     fetchers.put("documentedBy", externalDocuments);
    //     fetchers.put("examples", multiLanguageTexts);
    //     fetchers.put("countryOfOrigin", country);
    //     fetchers.put("similarTo", similarConcepts);
        
    //     return fetchers;
    // }
}
