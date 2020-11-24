package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdRelComposes;
import de.bentrm.datacat.catalog.service.CatalogService;
import de.bentrm.datacat.catalog.service.ComposesService;
import org.springframework.stereotype.Component;

@Component
public class ComposesFetchers extends AssociationFetchers<XtdRelComposes> {

    public ComposesFetchers(ComposesService entityService, CatalogService catalogService) {
        super(entityService, catalogService);
    }

    @Override
    public String getTypeName() {
        return "XtdRelComposes";
    }

    @Override
    public String getFetcherName() {
        return "composesRelation";
    }

    @Override
    public String getListFetcherName() {
        return "composesRelations";
    }
}
