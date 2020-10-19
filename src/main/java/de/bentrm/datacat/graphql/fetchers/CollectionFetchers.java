package de.bentrm.datacat.graphql.fetchers;

import de.bentrm.datacat.catalog.domain.XtdCollection;
import de.bentrm.datacat.catalog.service.AssignsCollectionsService;
import de.bentrm.datacat.catalog.service.CollectsService;
import de.bentrm.datacat.catalog.service.QueryService;
import de.bentrm.datacat.graphql.fetcher.AssignsCollectionsFetcher;
import de.bentrm.datacat.graphql.fetcher.CollectsFetcher;
import de.bentrm.datacat.graphql.fetcher.TagsFetcher;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public abstract class CollectionFetchers<T extends XtdCollection, S extends QueryService<T>>
        extends AbstractEntityFetchers<T, S> {

    private CollectsFetcher collectsFetcher;
    private AssignsCollectionsFetcher assignsCollectionsFetcher;


    public CollectionFetchers(S entityService) {
        super(entityService);
    }

    @Autowired
    public void setCollectsFetcher(CollectsService collectsService) {
        this.collectsFetcher = new CollectsFetcher(collectsService);
    }

    @Autowired
    public void setAssignsCollectionsFetcher(AssignsCollectionsService service) {
        this.assignsCollectionsFetcher = new AssignsCollectionsFetcher(service);
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        final Map<String, DataFetcher> fetchers = super.getAttributeFetchers();
        fetchers.put("tags", new TagsFetcher());
        fetchers.put("collects", collectsFetcher.collects());
        fetchers.put("collectedBy", collectsFetcher.collectedBy());
        fetchers.put("assignedTo", assignsCollectionsFetcher.assignedTo());
        return fetchers;
    }
}
