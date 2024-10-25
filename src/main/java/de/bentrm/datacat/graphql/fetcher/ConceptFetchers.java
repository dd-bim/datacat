package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdConcept;
import de.bentrm.datacat.catalog.domain.XtdCountry;
import de.bentrm.datacat.catalog.domain.XtdExternalDocument;
import de.bentrm.datacat.catalog.domain.XtdMultiLanguageText;
import de.bentrm.datacat.catalog.service.CatalogService;
import de.bentrm.datacat.catalog.service.ConceptRecordService;
import de.bentrm.datacat.graphql.fetcher.delegate.ObjectFetchersDelegate;
import de.bentrm.datacat.graphql.fetcher.delegate.RootFetchersDelegate;
import graphql.schema.DataFetcher;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ConceptFetchers extends AbstractFetchers<XtdConcept> {

    private final RootFetchersDelegate rootFetchersDelegate;
    private final ObjectFetchersDelegate objectFetchersDelegate;
    private final DataFetcher<List<XtdExternalDocument>> externalDocuments;
    private final DataFetcher<List<XtdMultiLanguageText>> multiLanguageTexts;
    private final DataFetcher<XtdCountry> country;
    private final DataFetcher<List<XtdConcept>> similarConcepts;

    public ConceptFetchers(ConceptRecordService queryService,
                           RootFetchersDelegate rootFetchersDelegate,
                           ObjectFetchersDelegate objectFetchersDelegate,
                           CatalogService catalogService) {
        super(queryService);
        this.rootFetchersDelegate = rootFetchersDelegate;
        this.objectFetchersDelegate = objectFetchersDelegate;

        this.externalDocuments = environment -> {
            final XtdConcept source = environment.getSource();
            return queryService.getExternalDocuments(source);
        };
        // this.externalDocuments = environment -> {
        //     final XtdConcept source = environment.getSource();
        //     final List<String> id = source.getDocumentedBy().stream().map(XtdExternalDocument::getId).collect(Collectors.toList());
        //     return catalogService.getAllExternalDocumentsById(id);
        // };

        this.multiLanguageTexts = environment -> {
            final XtdConcept source = environment.getSource();
            return queryService.getMultiLanguageTexts(source);
        };

        this.country = environment -> {
            final XtdConcept source = environment.getSource();
            return queryService.getCountry(source);
        };

        this.similarConcepts = environment -> {
            final XtdConcept source = environment.getSource();
            return queryService.getSimilarConcepts(source);
        };

    }

    @Override
    public String getTypeName() {
        return "XtdConcept";
    }

    @Override
    public String getFetcherName() {
        return "getConcept";
    }

    @Override
    public String getListFetcherName() {
        return "findConcepts";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        Map<String, DataFetcher> fetchers = new HashMap<>();
        fetchers.putAll(super.getAttributeFetchers());
        // fetchers.putAll(rootFetchersDelegate.getFetchers());
        // fetchers.putAll(objectFetchersDelegate.getFetchers());
        fetchers.put("documentedBy", externalDocuments);
        fetchers.put("examples", multiLanguageTexts);
        fetchers.put("countryOfOrigin", country);
        fetchers.put("similarTo", similarConcepts);
        
        return fetchers;
    }
}
