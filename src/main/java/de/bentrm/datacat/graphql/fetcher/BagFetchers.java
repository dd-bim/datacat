package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdBag;
import de.bentrm.datacat.catalog.service.BagService;
import de.bentrm.datacat.graphql.fetcher.delegate.CollectionFetchersDelegate;
import de.bentrm.datacat.graphql.fetcher.delegate.RootFetchersDelegate;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BagFetchers extends AbstractFetchers<XtdBag> {

    private final RootFetchersDelegate rootFetchersDelegate;
    private final CollectionFetchersDelegate collectionFetchersDelegate;

    public BagFetchers(BagService queryService,
                       RootFetchersDelegate rootFetchersDelegate,
                       CollectionFetchersDelegate collectionFetchersDelegate) {
        super(queryService);
        this.rootFetchersDelegate = rootFetchersDelegate;
        this.collectionFetchersDelegate = collectionFetchersDelegate;
    }

    @Override
    public String getTypeName() {
        return "XtdBag";
    }

    @Override
    public String getFetcherName() {
        return "bag";
    }

    @Override
    public String getListFetcherName() {
        return "bags";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        Map<String, DataFetcher> fetchers = new HashMap<>();

        fetchers.putAll(super.getAttributeFetchers());
        fetchers.putAll(rootFetchersDelegate.getFetchers());
        fetchers.putAll(collectionFetchersDelegate.getFetchers());

        return fetchers;
    }
}
