package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.domain.Entity;
import de.bentrm.datacat.domain.XtdExternalDocument;
import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.domain.relationship.XtdRelDocuments;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.PageInfo;
import de.bentrm.datacat.graphql.dto.EntityInput;
import de.bentrm.datacat.graphql.dto.EntityUpdateInput;
import de.bentrm.datacat.service.ExternalDocumentService;
import de.bentrm.datacat.specification.RootSpecification;
import graphql.schema.DataFetcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ExternalDocumentFetchers extends AbstractEntityFetchers<XtdExternalDocument, EntityInput, EntityUpdateInput, ExternalDocumentService> {

    public ExternalDocumentFetchers(ExternalDocumentService entityService) {
        super(EntityInput.class, EntityUpdateInput.class, entityService);
    }

    @Override
    public String getTypeName() {
        return "XtdExternalDocument";
    }

    @Override
    public String getQueryName() {
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
            XtdExternalDocument source = env.getSource();
            final RootSpecification specification = RootSpecification.builder()
                    .entityTypeIn(List.of("XtdRelDocuments"))
                    .idIn(source.getDocuments().stream().map(Entity::getId).collect(Collectors.toList()))
                    .build();
            if (!env.getSelectionSet().containsAnyOf("nodes/*", "pageInfo/*")) {
                final @NotNull long count = catalogService.countRootItems(specification);
                return Connection.empty(count);
            }
            @NotNull final Page<XtdRoot> page = catalogService.findAllRootItems(specification);
            final List<XtdRelDocuments> content = page.get().map(x -> (XtdRelDocuments) x).collect(Collectors.toList());
            return new Connection<>(content, PageInfo.of(page), (long) page.getSize());
        };
    }
}
