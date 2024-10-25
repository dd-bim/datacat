package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdExternalDocument;
import de.bentrm.datacat.catalog.domain.XtdLanguage;
import de.bentrm.datacat.catalog.domain.XtdConcept;
import de.bentrm.datacat.catalog.service.ExternalDocumentRecordService;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.fetcher.delegate.ObjectFetchersDelegate;
import de.bentrm.datacat.graphql.fetcher.delegate.RootFetchersDelegate;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Component
public class ExternalDocumentFetchers extends AbstractFetchers<XtdExternalDocument> {

    private final RootFetchersDelegate rootFetchersDelegate;
    private final ObjectFetchersDelegate objectFetchersDelegate;
    private final DataFetcher<List<XtdConcept>> concepts;
    private final DataFetcher<List<XtdLanguage>> languages;

    public ExternalDocumentFetchers(ExternalDocumentRecordService queryService,
                            RootFetchersDelegate rootFetchersDelegate,
                            ObjectFetchersDelegate objectFetchersDelegate) {
        super(queryService);

        this.rootFetchersDelegate = rootFetchersDelegate;
        this.objectFetchersDelegate = objectFetchersDelegate;

        this.concepts = environment -> {
            final XtdExternalDocument source = environment.getSource();
            return queryService.getConcepts(source);
        };

        this.languages = environment -> {
            final XtdExternalDocument source = environment.getSource();
            return queryService.getLanguages(source);
        };
    }

    @Override
    public String getTypeName() {
        return "XtdExternalDocument";
    }

    @Override
    public String getFetcherName() {
        return "getExternalDocument";
    }

    @Override
    public String getListFetcherName() {
        return "findExternalDocuments";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        Map<String, DataFetcher> fetchers = new HashMap<>();

        fetchers.putAll(super.getAttributeFetchers());
        // fetchers.putAll(rootFetchersDelegate.getFetchers());
        // fetchers.putAll(objectFetchersDelegate.getFetchers());
        fetchers.put("documents", concepts);
        fetchers.put("languages", languages);
        
        return fetchers;
    }
}
