package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdActor;
import de.bentrm.datacat.catalog.service.ActorRecordService;
import de.bentrm.datacat.graphql.fetcher.delegate.ObjectFetchersDelegate;
import de.bentrm.datacat.graphql.fetcher.delegate.RootFetchersDelegate;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ActorFetchers extends AbstractFetchers<XtdActor> {

    private final RootFetchersDelegate rootFetchersDelegate;
    private final ObjectFetchersDelegate objectFetchersDelegate;

    public ActorFetchers(ActorRecordService queryService,
                         RootFetchersDelegate rootFetchersDelegate,
                         ObjectFetchersDelegate objectFetchersDelegate) {
        super(queryService);
        this.rootFetchersDelegate = rootFetchersDelegate;
        this.objectFetchersDelegate = objectFetchersDelegate;
    }

    @Override
    public String getTypeName() {
        return "XtdActor";
    }

    @Override
    public String getFetcherName() {
        return "getActor";
    }

    @Override
    public String getListFetcherName() {
        return "findActors";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        Map<String, DataFetcher> fetchers = new HashMap<>();

        fetchers.putAll(super.getAttributeFetchers());
        fetchers.putAll(rootFetchersDelegate.getFetchers());
        fetchers.putAll(objectFetchersDelegate.getFetchers());

        return fetchers;
    }
}
