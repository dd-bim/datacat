package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdCollection;
import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.catalog.domain.XtdRelAssignsCollections;
import de.bentrm.datacat.catalog.service.AssignsCollectionsService;
import de.bentrm.datacat.graphql.Connection;
import graphql.schema.DataFetcher;

import java.util.Set;

public class AssignsCollectionsFetcher extends AbstractRelationshipFetcher<XtdRelAssignsCollections> {

    public AssignsCollectionsFetcher(AssignsCollectionsService service) {
        super(service);
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
}
