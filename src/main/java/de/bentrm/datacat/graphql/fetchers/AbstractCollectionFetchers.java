package de.bentrm.datacat.graphql.fetchers;

import de.bentrm.datacat.catalog.domain.XtdCollection;
import de.bentrm.datacat.catalog.service.AssignsCollectionsService;
import de.bentrm.datacat.catalog.service.QueryService;
import de.bentrm.datacat.graphql.fetcher.AssignsCollectionsFetcher;
import de.bentrm.datacat.graphql.fetcher.TagsFetcher;
import graphql.schema.DataFetcher;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@Getter
@Slf4j
public abstract class AbstractCollectionFetchers<T extends XtdCollection, S extends QueryService<T>>
        extends AbstractRootFetchers<T, S> {

    private AssignsCollectionsFetcher assignsCollectionsFetcher;


    public AbstractCollectionFetchers(S service) {
        super(service);
    }

    @Autowired
    public void setAssignsCollectionsFetcher(AssignsCollectionsService service) {
        this.assignsCollectionsFetcher = new AssignsCollectionsFetcher(service);
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        final Map<String, DataFetcher> fetchers = super.getAttributeFetchers();
        fetchers.put("tags", new TagsFetcher());
        fetchers.put("collects", getCollectsFetcher().collects());
        fetchers.put("collectedBy", getCollectsFetcher().collectedBy());
        fetchers.put("assignedTo", assignsCollectionsFetcher.assignedTo());
        return fetchers;
    }
}
