package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.domain.relationship.XtdRelDocuments;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.DocumentsInput;
import de.bentrm.datacat.graphql.dto.DocumentsUpdateInput;
import de.bentrm.datacat.service.RelDocumentsService;
import de.bentrm.datacat.specification.RootSpecification;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RelDocumentsFetchers
        extends AbstractEntityFetchers<XtdRelDocuments, DocumentsInput, DocumentsUpdateInput, RelDocumentsService> {

    public RelDocumentsFetchers(RelDocumentsService entityService) {
        super(DocumentsInput.class, DocumentsUpdateInput.class, entityService);
    }

    @Override
    public String getTypeName() {
        return "XtdRelDocuments";
    }

    @Override
    public String getQueryName() {
        return "documentsRelations";
    }

    @Override
    public String getMutationNameSuffix() {
        return "DocumentsRelation";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        final Map<String, DataFetcher> fetchers = super.getAttributeFetchers();
        fetchers.put("relatedThings", relatedThings());
        return fetchers;
    }

    public DataFetcher<Connection<XtdRoot>> relatedThings() {
        return env -> {
            XtdRelDocuments source = env.getSource();
            final RootSpecification specification = RootSpecification.builder()
                    .documentedBy(source.getId())
                    .build();
            return fetchRootConnection(env, specification);
        };
    }
}
