package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdRelActsUpon;
import de.bentrm.datacat.catalog.service.ActsUponService;
import de.bentrm.datacat.catalog.service.CatalogService;
import org.springframework.stereotype.Component;

@Component
public class RelActsUponFetchers extends AssociationFetchers<XtdRelActsUpon> {

    public RelActsUponFetchers(ActsUponService entityService, CatalogService catalogService) {
        super(entityService, catalogService);
    }

    @Override
    public String getTypeName() {
        return "XtdRelActsUpon";
    }

    @Override
    public String getFetcherName() {
        return "actsUponRelation";
    }

    @Override
    public String getListFetcherName() {
        return "actsUponRelations";
    }
}
