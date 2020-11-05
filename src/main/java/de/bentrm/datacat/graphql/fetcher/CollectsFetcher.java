package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdCollection;
import de.bentrm.datacat.catalog.domain.XtdRelCollects;
import de.bentrm.datacat.catalog.domain.XtdRoot;
import de.bentrm.datacat.catalog.service.CollectsService;
import de.bentrm.datacat.graphql.Connection;
import graphql.schema.DataFetcher;

import java.util.Set;

public class CollectsFetcher extends AbstractRelationshipFetcher<XtdRelCollects> {

    public CollectsFetcher(CollectsService service) {
        super(service);
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
}
