package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdRelAssignsProperties;
import de.bentrm.datacat.catalog.service.AssignsPropertiesService;
import de.bentrm.datacat.graphql.Connection;
import graphql.schema.DataFetcher;

import java.util.Set;

public class AssignsPropertiesFetcher extends AbstractRelationshipFetcher<XtdRelAssignsProperties> {

    public AssignsPropertiesFetcher(AssignsPropertiesService service) {
        super(service);
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
}
