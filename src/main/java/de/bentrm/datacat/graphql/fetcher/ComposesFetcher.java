package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.base.domain.Entity;
import de.bentrm.datacat.catalog.domain.XtdCollection;
import de.bentrm.datacat.catalog.domain.XtdRelComposes;
import de.bentrm.datacat.catalog.domain.XtdRoot;
import de.bentrm.datacat.catalog.service.ComposesService;
import de.bentrm.datacat.graphql.Connection;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ComposesFetcher {

    private final ComposesService service;

    public ComposesFetcher(ComposesService service) {
        this.service = service;
    }

    public DataFetcher<Connection<XtdRelComposes>> composes() {
        return environment -> {
            final XtdCollection source = environment.getSource();
            final Set<XtdRelComposes> fieldValues = source.getComposes();
            return get(fieldValues, environment);
        };
    }

    public DataFetcher<Connection<XtdRelComposes>> composedBy() {
        return environment -> {
            final XtdRoot source = environment.getSource();
            final Set<XtdRelComposes> fieldValues = source.getComposedBy();
            return get(fieldValues, environment);
        };
    }

    private Connection<XtdRelComposes> get(Set<XtdRelComposes> fieldValues, DataFetchingEnvironment environment) {
        // only already populated fields are accessed
        if (fieldValues.isEmpty() || !environment.getSelectionSet().contains("nodes/*/*")) {
            return Connection.of(fieldValues);
        }

        // the properties of the collection items need to be populated
        final List<String> ids = fieldValues.stream()
                .map(Entity::getId)
                .collect(Collectors.toList());
        @NotNull final List<XtdRelComposes> items = service.findAllByIds(ids);
        return Connection.of(items);
    }
}
