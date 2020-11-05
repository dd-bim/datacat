package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdExternalDocument;
import de.bentrm.datacat.catalog.domain.XtdRelDocuments;
import de.bentrm.datacat.catalog.domain.XtdRoot;
import de.bentrm.datacat.catalog.service.DocumentsService;
import de.bentrm.datacat.graphql.Connection;
import graphql.schema.DataFetcher;

import java.util.Set;

public class DocumentsFetcher extends AbstractRelationshipFetcher<XtdRelDocuments> {

    public DocumentsFetcher(DocumentsService service) {
        super(service);
    }

    public DataFetcher<Connection<XtdRelDocuments>> documents() {
        return environment -> {
            final XtdExternalDocument source = environment.getSource();
            final Set<XtdRelDocuments> fieldValues = source.getDocuments();
            return get(fieldValues, environment);
        };
    }

    public DataFetcher<Connection<XtdRelDocuments>> documentedBy() {
        return environment -> {
            final XtdRoot source = environment.getSource();
            final Set<XtdRelDocuments> fieldValues = source.getDocumentedBy();
            return get(fieldValues, environment);
        };
    }
}
