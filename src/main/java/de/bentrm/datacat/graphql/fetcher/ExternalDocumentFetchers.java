package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdExternalDocument;
import de.bentrm.datacat.catalog.service.ExternalDocumentRecordService;
import de.bentrm.datacat.graphql.Connection;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class ExternalDocumentFetchers extends AbstractFetchers<XtdExternalDocument> {

    // private final RelationshipFetcher<XtdRelDocuments> documents;

    public ExternalDocumentFetchers(ExternalDocumentRecordService entityService) {  // , DocumentsRecordService documentsService
        super(entityService);

        // this.documents = new RelationshipFetcher<>(documentsService) {
        //     @Override
        //     public Connection<XtdRelDocuments> get(DataFetchingEnvironment environment) throws Exception {
        //         final XtdExternalDocument source = environment.getSource();
        //         final Set<XtdRelDocuments> fieldValues = source.getDocuments();
        //         return get(fieldValues, environment);
        //     }
        // };
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

    // @Override
    // public Map<String, DataFetcher> getAttributeFetchers() {
    //     final Map<String, DataFetcher> fetchers = new HashMap<>(super.getAttributeFetchers());
    //     fetchers.put("documents", documents);
    //     return fetchers;
    // }
}
