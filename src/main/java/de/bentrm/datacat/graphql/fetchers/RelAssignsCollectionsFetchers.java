package de.bentrm.datacat.graphql.fetchers;

import de.bentrm.datacat.catalog.domain.CatalogItem;
import de.bentrm.datacat.catalog.domain.XtdCollection;
import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.catalog.domain.XtdRelAssignsCollections;
import de.bentrm.datacat.catalog.service.AssignsCollectionsService;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RelAssignsCollectionsFetchers
        extends AbstractFetchers<XtdRelAssignsCollections, AssignsCollectionsService> {

    public RelAssignsCollectionsFetchers(AssignsCollectionsService entityService) {
        super(entityService);
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
    public String getMutationNameSuffix() {
        return "AssignsCollectionsRelation";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        final Map<String, DataFetcher> fetchers = super.getAttributeFetchers();
        fetchers.put("relatingObject", relatingObject());
        fetchers.put("relatedCollections", relatedCollections());
        return fetchers;
    }

    private DataFetcher<XtdObject> relatingObject() {
        return environment -> {
            final XtdRelAssignsCollections source = environment.getSource();
            final String id = source.getRelatingObject().getId();
            return getCatalogService().getObject(id).orElseThrow();
        };
    }

    public DataFetcher<List<XtdCollection>> relatedCollections() {
        return environment -> {
            final XtdRelAssignsCollections source = environment.getSource();
            final List<String> relatedCollectionsIds = source.getRelatedCollections().stream()
                    .map(CatalogItem::getId)
                    .collect(Collectors.toList());
            return getCatalogService().getAllCollectionsById(relatedCollectionsIds);
        };
    }
}
