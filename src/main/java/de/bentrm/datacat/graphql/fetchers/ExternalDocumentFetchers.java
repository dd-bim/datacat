package de.bentrm.datacat.graphql.fetchers;

import de.bentrm.datacat.base.domain.Entity;
import de.bentrm.datacat.catalog.domain.XtdExternalDocument;
import de.bentrm.datacat.catalog.domain.XtdRelDocuments;
import de.bentrm.datacat.catalog.service.DocumentsService;
import de.bentrm.datacat.catalog.service.ExternalDocumentService;
import de.bentrm.datacat.graphql.Connection;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ExternalDocumentFetchers extends AbstractEntityFetchers<XtdExternalDocument, ExternalDocumentService> {

    private final DocumentsService documentsService;

    public ExternalDocumentFetchers(ExternalDocumentService entityService, DocumentsService documentsService) {
        super(entityService);
        this.documentsService = documentsService;
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
        final HashMap<String, DataFetcher> fetchers = new HashMap<>(super.getAttributeFetchers());
        fetchers.put("documents", documents());
        return fetchers;
    }

    public DataFetcher<Connection<XtdRelDocuments>> documents() {
        return env -> {
            final XtdExternalDocument source = env.getSource();
            final Set<XtdRelDocuments> fieldValues = source.getDocuments();

            // only populated fields are accessed
            if (fieldValues.isEmpty() || !env.getSelectionSet().contains("nodes/*/*")) {
                return Connection.of(fieldValues);
            }

            // the properties of the collection items need to be populated
            final List<String> ids = fieldValues.stream()
                    .map(Entity::getId)
                    .collect(Collectors.toList());
            @NotNull final List<XtdRelDocuments> items = documentsService.findAllByIds(ids);
            return Connection.of(items);
        };
    }
}
