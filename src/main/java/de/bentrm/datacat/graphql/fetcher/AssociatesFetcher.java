package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdCollection;
import de.bentrm.datacat.catalog.domain.XtdRelAssociates;
import de.bentrm.datacat.catalog.domain.XtdRoot;
import de.bentrm.datacat.catalog.service.AssociatesService;
import de.bentrm.datacat.graphql.Connection;
import graphql.schema.DataFetcher;

import java.util.Set;

public class AssociatesFetcher extends AbstractRelationshipFetcher<XtdRelAssociates> {

    public AssociatesFetcher(AssociatesService service) {
        super(service);
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
}
