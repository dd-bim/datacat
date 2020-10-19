package de.bentrm.datacat.graphql.fetchers;

import de.bentrm.datacat.catalog.domain.XtdRelDocuments;
import de.bentrm.datacat.catalog.service.DocumentsService;
import org.springframework.stereotype.Component;

@Component
public class RelDocumentsFetchers
        extends AbstractEntityFetchers<XtdRelDocuments, DocumentsService> {

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
}
