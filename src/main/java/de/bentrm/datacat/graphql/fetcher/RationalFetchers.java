package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdRational;
import de.bentrm.datacat.catalog.service.RationalRecordService;
import de.bentrm.datacat.graphql.fetcher.delegate.ObjectFetchersDelegate;
import de.bentrm.datacat.graphql.fetcher.delegate.RootFetchersDelegate;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RationalFetchers extends AbstractFetchers<XtdRational> {

    private final RootFetchersDelegate rootFetchersDelegate;
    private final ObjectFetchersDelegate objectFetchersDelegate;

    public RationalFetchers(RationalRecordService queryService,
                           RootFetchersDelegate rootFetchersDelegate,
                           ObjectFetchersDelegate objectFetchersDelegate) {
        super(queryService);
        this.rootFetchersDelegate = rootFetchersDelegate;
        this.objectFetchersDelegate = objectFetchersDelegate;
    }

    @Override
    public String getTypeName() {
        return "XtdRational";
    }

    @Override
    public String getFetcherName() {
        return "getRational";
    }

    @Override
    public String getListFetcherName() {
        return "findRationals";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        Map<String, DataFetcher> fetchers = new HashMap<>();
        fetchers.putAll(super.getAttributeFetchers());
        // fetchers.putAll(rootFetchersDelegate.getFetchers());
        // fetchers.putAll(objectFetchersDelegate.getFetchers());
        return fetchers;
    }
}
