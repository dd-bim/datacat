package de.bentrm.datacat.graphql.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bentrm.datacat.domain.XtdExternalDocument;
import de.bentrm.datacat.domain.relationship.XtdRelDocuments;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.PageInfo;
import de.bentrm.datacat.graphql.dto.EntityInput;
import de.bentrm.datacat.graphql.dto.EntityUpdateInput;
import de.bentrm.datacat.graphql.dto.PagingOptions;
import de.bentrm.datacat.service.ExternalDocumentService;
import de.bentrm.datacat.service.RelDocumentsService;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ExternalDocumentDataFetcherProvider
        extends EntityDataFetcherProviderImpl<XtdExternalDocument, EntityInput, EntityUpdateInput, ExternalDocumentService>
        implements QueryDataFetcherProvider, MutationDataFetcherProvider {

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private RelDocumentsService relDocumentsService;

    public ExternalDocumentDataFetcherProvider(ExternalDocumentService entityService) {
        super(EntityInput.class, EntityUpdateInput.class, entityService);
    }

    @Override
    public Map<String, DataFetcher> getDataFetchers() {
        return Map.ofEntries(
                Map.entry("documents", documents())
        );
    }

    @Override
    public Map<String, DataFetcher> getQueryDataFetchers() {
        return Map.ofEntries(
                Map.entry("externalDocuments", getAll())
        );
    }

    @Override
    public Map<String, DataFetcher> getMutationDataFetchers() {
        return Map.ofEntries(
                Map.entry("createExternalDocument", add()),
                Map.entry("updateExternalDocument", update()),
                Map.entry("deleteExternalDocument", remove())
        );
    }

    public DataFetcher<Connection<XtdRelDocuments>> documents() {
        return environment -> {
            XtdExternalDocument document = environment.getSource();
            Map<String, Object> input = environment.getArgument("options");
            PagingOptions dto = mapper.convertValue(input, PagingOptions.class);

            if (dto == null) dto = PagingOptions.defaults();

            List<String> ids = document.getDocuments().stream().map(XtdRelDocuments::getId).collect(Collectors.toList());
            Page<XtdRelDocuments> page = relDocumentsService.findByIds(ids, dto.getPageble());

            return new Connection<>(page.getContent(), PageInfo.of(page), page.getTotalElements());
        };
    }
}
