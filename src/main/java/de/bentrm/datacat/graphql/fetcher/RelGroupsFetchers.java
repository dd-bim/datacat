package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdRelGroups;
import de.bentrm.datacat.catalog.service.CatalogService;
import de.bentrm.datacat.catalog.service.GroupsService;
import org.springframework.stereotype.Component;

@Component
public class RelGroupsFetchers extends AssociationFetchers<XtdRelGroups> {

    public RelGroupsFetchers(GroupsService entityService, CatalogService catalogService) {
        super(entityService, catalogService);
    }

    @Override
    public String getTypeName() {
        return "XtdRelGroups";
    }

    @Override
    public String getFetcherName() {
        return "groupsRelation";
    }

    @Override
    public String getListFetcherName() {
        return "groupsRelations";
    }
}
