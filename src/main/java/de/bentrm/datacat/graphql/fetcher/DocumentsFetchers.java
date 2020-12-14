package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdExternalDocument;
import de.bentrm.datacat.catalog.domain.XtdRelDocuments;
import de.bentrm.datacat.catalog.domain.XtdRoot;
import de.bentrm.datacat.catalog.service.CatalogService;
import de.bentrm.datacat.catalog.service.DocumentsRecordService;
import de.bentrm.datacat.catalog.service.ExternalDocumentRecordService;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DocumentsFetchers extends AbstractFetchers<XtdRelDocuments> {

    private final DataFetcher<XtdExternalDocument> relatingDocument;
    private final DataFetcher<List<XtdRoot>> relatedThings;

    public DocumentsFetchers(DocumentsRecordService entityService,
                             ExternalDocumentRecordService externalDocumentService,
                             CatalogService catalogService) {
        super(entityService);

        this.relatingDocument = environment -> {
            final XtdRelDocuments source = environment.getSource();
            final String id = source.getRelatingDocument().getId();
            return externalDocumentService.findById(id).orElseThrow();
        };

        this.relatedThings = environment -> {
            final XtdRelDocuments source = environment.getSource();
            final List<String> relatedThingsIds = source.getRelatedThings().stream()
                    .map(XtdRoot::getId)
                    .collect(Collectors.toList());
            return catalogService.getAllRootItemsById(relatedThingsIds);
        };
    }

    @Override
    public String getTypeName() {
        return "XtdRelDocuments";
    }

    @Override
    public String getFetcherName() {
        return "getDocuments";
    }

    @Override
    public String getListFetcherName() {
        return "findDocuments";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        final Map<String, DataFetcher> fetchers = new HashMap<>(super.getAttributeFetchers());
        fetchers.put("relatingDocument", relatingDocument);
        fetchers.put("relatedThings", relatedThings);
        return fetchers;
    }
}
