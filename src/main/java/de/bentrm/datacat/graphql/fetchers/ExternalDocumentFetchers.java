package de.bentrm.datacat.graphql.fetchers;

import de.bentrm.datacat.catalog.domain.XtdExternalDocument;
import de.bentrm.datacat.catalog.service.DocumentsService;
import de.bentrm.datacat.catalog.service.ExternalDocumentService;
import de.bentrm.datacat.graphql.fetcher.DocumentsFetcher;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ExternalDocumentFetchers extends AbstractEntityFetchers<XtdExternalDocument, ExternalDocumentService> {

    private final DocumentsFetcher documentsFetcher;

    public ExternalDocumentFetchers(ExternalDocumentService entityService, DocumentsService documentsService) {
        super(entityService);
        this.documentsFetcher = new DocumentsFetcher(documentsService);
    }

    @Override
    public String getTypeName() {
        return "XtdExternalDocument";
    }

    @Override
    public String getFetcherName() {
        return "externalDocument";
    }

    @Override
    public String getListFetcherName() {
        return "externalDocuments";
    }

    @Override
    public String getMutationNameSuffix() {
        return "ExternalDocument";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        final Map<String, DataFetcher> fetchers = super.getAttributeFetchers();
        fetchers.put("documents", documentsFetcher.documents());
        return fetchers;
    }
}
