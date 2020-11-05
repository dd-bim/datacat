package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdCollection;
import de.bentrm.datacat.catalog.domain.XtdRelComposes;
import de.bentrm.datacat.catalog.domain.XtdRoot;
import de.bentrm.datacat.catalog.service.ComposesService;
import de.bentrm.datacat.graphql.Connection;
import graphql.schema.DataFetcher;

import java.util.Set;

public class ComposesFetcher extends AbstractRelationshipFetcher<XtdRelComposes> {

    public ComposesFetcher(ComposesService service) {
        super(service);
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
}
