package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdRelActsUpon;
import de.bentrm.datacat.catalog.service.ActsUponService;
import de.bentrm.datacat.catalog.service.CatalogService;
import org.springframework.stereotype.Component;

@Component
public class ActsUponFetchers extends AssociationFetchers<XtdRelActsUpon> {

    public ActsUponFetchers(ActsUponService entityService, CatalogService catalogService) {
        super(entityService, catalogService);
    }

    @Override
    public String getTypeName() {
        return "XtdRelActsUpon";
    }

    @Override
    public String getFetcherName() {
        return "getActsUpon";
    }

    @Override
    public String getListFetcherName() {
        return "findActsUpon";
    }
}
