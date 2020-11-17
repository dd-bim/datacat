package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdRelSpecializes;
import de.bentrm.datacat.catalog.service.CatalogService;
import de.bentrm.datacat.catalog.service.SpecializesService;
import org.springframework.stereotype.Component;

@Component
public class RelSpecializesFetchers extends AssociationFetchers<XtdRelSpecializes> {

    public RelSpecializesFetchers(SpecializesService entityService, CatalogService catalogService) {
        super(entityService, catalogService);
    }

    @Override
    public String getTypeName() {
        return "XtdRelSpecializes";
    }

    @Override
    public String getFetcherName() {
        return "specializesRelation";
    }

    @Override
    public String getListFetcherName() {
        return "specializesRelations";
    }
}
