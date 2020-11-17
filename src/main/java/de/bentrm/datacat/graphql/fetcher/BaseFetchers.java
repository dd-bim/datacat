package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.CatalogItem;
import de.bentrm.datacat.catalog.domain.XtdCollection;
import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.catalog.domain.XtdRelationship;
import de.bentrm.datacat.catalog.service.CatalogService;
import de.bentrm.datacat.graphql.dto.CatalogStatistics;
import graphql.schema.DataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class BaseFetchers implements QueryFetchers {

    @Autowired
    private CatalogService catalogService;


    @Override
    public Map<String, DataFetcher> getQueryFetchers() {
        return Map.ofEntries(
                Map.entry("statistics", statistics()),
                Map.entry("node", node()),
                Map.entry("objectById", objectById()),
                Map.entry("collectionById", collectionById()),
                Map.entry("relationshipById", relationshipById())
        );
    }

    public DataFetcher<CatalogStatistics> statistics() {
        return environment -> catalogService.getStatistics();
    }

    public DataFetcher<Optional<CatalogItem>> node() {
        return environment -> {
            String id = environment.getArgument("id");
            return catalogService.getEntryById(id);
        };
    }

    public DataFetcher<Optional<XtdObject>> objectById() {
        return environment -> {
            String id = environment.getArgument("id");
            return catalogService.getObject(id);
        };
    }

    public DataFetcher<Optional<XtdCollection>> collectionById() {
        return environment -> {
            String id = environment.getArgument("id");
            return catalogService.getCollection(id);
        };
    }

    public DataFetcher<Optional<XtdRelationship>> relationshipById() {
        return environment -> {
            String id = environment.getArgument("id");
            return catalogService.getRelationship(id);
        };
    }
}
