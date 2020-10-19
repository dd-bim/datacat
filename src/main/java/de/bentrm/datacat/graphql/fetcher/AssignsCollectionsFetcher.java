package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.base.domain.Entity;
import de.bentrm.datacat.catalog.domain.XtdCollection;
import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.catalog.domain.XtdRelAssignsCollections;
import de.bentrm.datacat.catalog.service.AssignsCollectionsService;
import de.bentrm.datacat.graphql.Connection;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AssignsCollectionsFetcher {

    private final AssignsCollectionsService service;

    public AssignsCollectionsFetcher(AssignsCollectionsService service) {
        this.service = service;
    }

    public DataFetcher<Connection<XtdRelAssignsCollections>> assignedCollections() {
        return environment -> {
            final XtdObject source = environment.getSource();
            final Set<XtdRelAssignsCollections> fieldValues = source.getAssignedCollections();
            return get(fieldValues, environment);
        };
    }

    public DataFetcher<Connection<XtdRelAssignsCollections>> assignedTo() {
        return environment -> {
            final XtdCollection source = environment.getSource();
            final Set<XtdRelAssignsCollections> fieldValues = source.getAssignedTo();
            return get(fieldValues, environment);
        };
    }

    private Connection<XtdRelAssignsCollections> get(Set<XtdRelAssignsCollections> fieldValues, DataFetchingEnvironment environment) {
        // only already populated fields are accessed
        if (fieldValues.isEmpty() || !environment.getSelectionSet().contains("nodes/*/*")) {
            return Connection.of(fieldValues);
        }

        // the properties of the collection items need to be populated
        final List<String> ids = fieldValues.stream()
                .map(Entity::getId)
                .collect(Collectors.toList());
        @NotNull final List<XtdRelAssignsCollections> items = service.findAllByIds(ids);
        return Connection.of(items);
    }
}
