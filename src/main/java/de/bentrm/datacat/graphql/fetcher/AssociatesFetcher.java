package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.base.domain.Entity;
import de.bentrm.datacat.catalog.domain.XtdCollection;
import de.bentrm.datacat.catalog.domain.XtdRelAssociates;
import de.bentrm.datacat.catalog.domain.XtdRoot;
import de.bentrm.datacat.catalog.service.AssociatesService;
import de.bentrm.datacat.graphql.Connection;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AssociatesFetcher {

    private final AssociatesService service;

    public AssociatesFetcher(AssociatesService service) {
        this.service = service;
    }

    public DataFetcher<Connection<XtdRelAssociates>> associates() {
        return environment -> {
            final XtdCollection source = environment.getSource();
            final Set<XtdRelAssociates> fieldValues = source.getAssociates();
            return get(fieldValues, environment);
        };
    }

    public DataFetcher<Connection<XtdRelAssociates>> associatedBy() {
        return environment -> {
            final XtdRoot source = environment.getSource();
            final Set<XtdRelAssociates> fieldValues = source.getAssociatedBy();
            return get(fieldValues, environment);
        };
    }

    private Connection<XtdRelAssociates> get(Set<XtdRelAssociates> fieldValues, DataFetchingEnvironment environment) {
        // only already populated fields are accessed
        if (fieldValues.isEmpty() || !environment.getSelectionSet().contains("nodes/*/*")) {
            return Connection.of(fieldValues);
        }

        // the properties of the collection items need to be populated
        final List<String> ids = fieldValues.stream()
                .map(Entity::getId)
                .collect(Collectors.toList());
        @NotNull final List<XtdRelAssociates> items = service.findAllByIds(ids);
        return Connection.of(items);
    }
}
