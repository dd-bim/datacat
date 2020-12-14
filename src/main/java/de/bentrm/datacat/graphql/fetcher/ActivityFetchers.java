package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdActivity;
import de.bentrm.datacat.catalog.service.ActivityRecordService;
import de.bentrm.datacat.graphql.fetcher.delegate.ObjectFetchersDelegate;
import de.bentrm.datacat.graphql.fetcher.delegate.RootFetchersDelegate;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ActivityFetchers extends AbstractFetchers<XtdActivity> {

    private final RootFetchersDelegate rootFetchersDelegate;
    private final ObjectFetchersDelegate objectFetchersDelegate;

    public ActivityFetchers(ActivityRecordService queryService,
                            RootFetchersDelegate rootFetchersDelegate,
                            ObjectFetchersDelegate objectFetchersDelegate) {
        super(queryService);
        this.rootFetchersDelegate = rootFetchersDelegate;
        this.objectFetchersDelegate = objectFetchersDelegate;
    }

    @Override
    public String getTypeName() {
        return "XtdActivity";
    }

    @Override
    public String getFetcherName() {
        return "getActivity";
    }

    @Override
    public String getListFetcherName() {
        return "findActivities";
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
