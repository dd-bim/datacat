package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdRelSpecializes;
import de.bentrm.datacat.catalog.service.CatalogService;
import de.bentrm.datacat.catalog.service.SpecializesService;
import org.springframework.stereotype.Component;

@Component
public class SpecializesFetchers extends AssociationFetchers<XtdRelSpecializes> {

    public SpecializesFetchers(SpecializesService entityService, CatalogService catalogService) {
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
