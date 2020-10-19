package de.bentrm.datacat.graphql.fetchers;

import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.catalog.service.AssignsCollectionsService;
import de.bentrm.datacat.catalog.service.AssignsPropertiesService;
import de.bentrm.datacat.catalog.service.QueryService;
import de.bentrm.datacat.graphql.fetcher.AssignsCollectionsFetcher;
import de.bentrm.datacat.graphql.fetcher.AssignsPropertiesFetcher;
import de.bentrm.datacat.graphql.fetcher.TagsFetcher;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public abstract class ObjectFetchers<T extends XtdObject, S extends QueryService<T>>
        extends AbstractRootFetchers<T, S> {

    private AssignsCollectionsFetcher assignsCollectionsFetcher;
    private AssignsPropertiesFetcher assignsPropertiesFetcher;

    public ObjectFetchers(S entityService) {
        super(entityService);
    }

    @Autowired
    public void setAssignsCollectionsFetcher(AssignsCollectionsService service) {
        this.assignsCollectionsFetcher = new AssignsCollectionsFetcher(service);
    }

    @Autowired
    public void setAssignsPropertiesFetcher(AssignsPropertiesService service) {
        this.assignsPropertiesFetcher = new AssignsPropertiesFetcher(service);
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        final Map<String, DataFetcher> fetchers = super.getAttributeFetchers();
        fetchers.put("tags", new TagsFetcher());
        fetchers.put("assignedCollections", assignsCollectionsFetcher.assignedCollections());
        fetchers.put("assignedProperties", assignsPropertiesFetcher.assignedProperties());
        return fetchers;
    }
}
