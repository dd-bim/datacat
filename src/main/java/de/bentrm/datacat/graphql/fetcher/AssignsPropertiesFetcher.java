package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.base.domain.Entity;
import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdRelAssignsProperties;
import de.bentrm.datacat.catalog.service.AssignsPropertiesService;
import de.bentrm.datacat.graphql.Connection;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AssignsPropertiesFetcher {

    private final AssignsPropertiesService service;

    public AssignsPropertiesFetcher(AssignsPropertiesService service) {
        this.service = service;
    }

    public DataFetcher<Connection<XtdRelAssignsProperties>> assignedProperties() {
        return environment -> {
            final XtdObject source = environment.getSource();
            final Set<XtdRelAssignsProperties> fieldValues = source.getAssignedProperties();
            return get(fieldValues, environment);
        };
    }

    public DataFetcher<Connection<XtdRelAssignsProperties>> assignedTo() {
        return environment -> {
            final XtdProperty source = environment.getSource();
            final Set<XtdRelAssignsProperties> fieldValues = source.getAssignedProperties();
            return get(fieldValues, environment);
        };
    }

    private Connection<XtdRelAssignsProperties> get(Set<XtdRelAssignsProperties> fieldValues, DataFetchingEnvironment environment) {
        // only already populated fields are accessed
        if (fieldValues.isEmpty() || !environment.getSelectionSet().contains("nodes/*/*")) {
            return Connection.of(fieldValues);
        }

        // the properties of the collection items need to be populated
        final List<String> ids = fieldValues.stream()
                .map(Entity::getId)
                .collect(Collectors.toList());
        @NotNull final List<XtdRelAssignsProperties> items = service.findAllByIds(ids);
        return Connection.of(items);
    }
}
