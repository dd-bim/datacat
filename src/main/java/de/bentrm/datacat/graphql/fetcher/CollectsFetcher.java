package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.base.domain.Entity;
import de.bentrm.datacat.catalog.domain.XtdCollection;
import de.bentrm.datacat.catalog.domain.XtdRelCollects;
import de.bentrm.datacat.catalog.domain.XtdRoot;
import de.bentrm.datacat.catalog.service.CollectsService;
import de.bentrm.datacat.graphql.Connection;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CollectsFetcher {

    private final CollectsService collectsService;

    public CollectsFetcher(CollectsService collectsService) {
        this.collectsService = collectsService;
    }

    public DataFetcher<Connection<XtdRelCollects>> collects() {
        return environment -> {
            final XtdCollection source = environment.getSource();
            final Set<XtdRelCollects> fieldValues = source.getCollects();
            return get(fieldValues, environment);
        };
    }

    public DataFetcher<Connection<XtdRelCollects>> collectedBy() {
        return environment -> {
            final XtdRoot source = environment.getSource();
            final Set<XtdRelCollects> fieldValues = source.getCollectedBy();
            return get(fieldValues, environment);
        };
    }

    private Connection<XtdRelCollects> get(Set<XtdRelCollects> fieldValues, DataFetchingEnvironment environment) {
        // only already populated fields are accessed
        if (fieldValues.isEmpty() || !environment.getSelectionSet().contains("nodes/*/*")) {
            return Connection.of(fieldValues);
        }

        // the properties of the collection items need to be populated
        final List<String> ids = fieldValues.stream()
                .map(Entity::getId)
                .collect(Collectors.toList());
        @NotNull final List<XtdRelCollects> items = collectsService.findAllByIds(ids);
        return Connection.of(items);
    }
}
