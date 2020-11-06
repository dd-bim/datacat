package de.bentrm.datacat.graphql.fetchers;

import de.bentrm.datacat.catalog.domain.CatalogItem;
import de.bentrm.datacat.catalog.domain.XtdExternalDocument;
import de.bentrm.datacat.catalog.domain.XtdRelDocuments;
import de.bentrm.datacat.catalog.domain.XtdRoot;
import de.bentrm.datacat.catalog.service.DocumentsService;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RelDocumentsFetchers
        extends AbstractFetchers<XtdRelDocuments, DocumentsService> {

    public RelDocumentsFetchers(DocumentsService entityService) {
        super(entityService);
    }

    @Override
    public String getTypeName() {
        return "XtdRelDocuments";
    }

    @Override
    public String getFetcherName() {
        return "documentsRelation";
    }

    @Override
    public String getListFetcherName() {
        return "documentsRelations";
    }

    @Override
    public String getMutationNameSuffix() {
        return "DocumentsRelation";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        final Map<String, DataFetcher> fetchers = super.getAttributeFetchers();
        fetchers.put("relatingDocument", relatingDocument());
        fetchers.put("relatedThings", relatedThings());
        return fetchers;
    }

    private DataFetcher<XtdExternalDocument> relatingDocument() {
        return environment -> {
            final XtdRelDocuments source = environment.getSource();
            final String id = source.getRelatingDocument().getId();
            final CatalogItem collection = getCatalogService().getEntryById(id).orElseThrow();
            return (XtdExternalDocument) collection;
        };
    }

    private DataFetcher<List<CatalogItem>> relatedThings() {
        return environment -> {
            final XtdRelDocuments source = environment.getSource();
            final List<String> relatedThingsIds = source.getRelatedThings().stream()
                    .map(XtdRoot::getId)
                    .collect(Collectors.toList());
            return getCatalogService().getAllEntriesById(relatedThingsIds);
        };
    }
}
