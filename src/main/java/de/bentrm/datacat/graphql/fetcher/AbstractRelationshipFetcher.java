package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.base.domain.Entity;
import de.bentrm.datacat.catalog.domain.XtdRelationship;
import de.bentrm.datacat.catalog.service.QueryService;
import de.bentrm.datacat.graphql.Connection;
import graphql.schema.DataFetchingEnvironment;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractRelationshipFetcher<T extends XtdRelationship> {

    protected final QueryService<T> service;

    public AbstractRelationshipFetcher(QueryService<T> service) {
        this.service = service;
    }

    protected Connection<T> get(Set<T> fieldValues, DataFetchingEnvironment environment) {
        // only already populated fields are accessed
        if (fieldValues.isEmpty() || !environment.getSelectionSet().contains("nodes/*/*")) {
            return Connection.of(fieldValues);
        }

        // the properties of the collection items need to be populated
        final List<String> ids = fieldValues.stream()
                .map(Entity::getId)
                .collect(Collectors.toList());
        @NotNull final List<T> items = service.findAllByIds(ids);
        return Connection.of(items);
    }

}
