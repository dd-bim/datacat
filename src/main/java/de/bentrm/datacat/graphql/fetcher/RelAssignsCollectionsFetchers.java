package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.CatalogItem;
import de.bentrm.datacat.catalog.domain.XtdCollection;
import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.catalog.domain.XtdRelAssignsCollections;
import de.bentrm.datacat.catalog.service.AssignsCollectionsService;
import de.bentrm.datacat.catalog.service.CatalogService;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RelAssignsCollectionsFetchers extends AbstractFetchers<XtdRelAssignsCollections> {

    private final DataFetcher<XtdObject> relatingObject;
    private final DataFetcher<List<XtdCollection>> relatedCollections;

    public RelAssignsCollectionsFetchers(AssignsCollectionsService entityService,
                                         CatalogService catalogService) {
        super(entityService);

        this.relatingObject = environment -> {
            final XtdRelAssignsCollections source = environment.getSource();
            final String id = source.getRelatingObject().getId();
            return catalogService.getObject(id).orElseThrow();
        };

        this.relatedCollections = environment -> {
            final XtdRelAssignsCollections source = environment.getSource();
            final List<String> relatedCollectionsIds = source.getRelatedCollections().stream()
                    .map(CatalogItem::getId)
                    .collect(Collectors.toList());
            return catalogService.getAllCollectionsById(relatedCollectionsIds);
        };
    }

    @Override
    public String getTypeName() {
        return "XtdRelAssignsCollections";
    }

    @Override
    public String getFetcherName() {
        return "assignsCollectionsRelation";
    }

    @Override
    public String getListFetcherName() {
        return "assignsCollectionsRelations";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        final Map<String, DataFetcher> fetchers = new HashMap<>(super.getAttributeFetchers());
        fetchers.put("relatingObject", relatingObject);
        fetchers.put("relatedCollections", relatedCollections);
        return fetchers;
    }
}
