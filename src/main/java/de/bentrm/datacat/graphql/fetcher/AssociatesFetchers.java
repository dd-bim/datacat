package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdRelAssociates;
import de.bentrm.datacat.catalog.service.AssociatesService;
import de.bentrm.datacat.catalog.service.CatalogService;
import org.springframework.stereotype.Component;

@Component
public class AssociatesFetchers extends AssociationFetchers<XtdRelAssociates> {

    public AssociatesFetchers(AssociatesService entityService, CatalogService catalogService) {
        super(entityService, catalogService);
    }

    @Override
    public String getTypeName() {
        return "XtdRelAssociates";
    }

    @Override
    public String getFetcherName() {
        return "getAssociates";
    }

    @Override
    public String getListFetcherName() {
        return "findAssociates";
    }
}
