package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdInterval;
import de.bentrm.datacat.catalog.domain.XtdValueList;
import de.bentrm.datacat.catalog.service.IntervalRecordService;
import de.bentrm.datacat.graphql.fetcher.delegate.ObjectFetchersDelegate;
import de.bentrm.datacat.graphql.fetcher.delegate.RootFetchersDelegate;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class IntervalFetchers extends AbstractFetchers<XtdInterval> {

    private final RootFetchersDelegate rootFetchersDelegate;
    private final ObjectFetchersDelegate objectFetchersDelegate;
    private final DataFetcher<XtdValueList> minimum;
    private final DataFetcher<XtdValueList> maximum;

    public IntervalFetchers(IntervalRecordService queryService,
                           RootFetchersDelegate rootFetchersDelegate,
                           ObjectFetchersDelegate objectFetchersDelegate) {
        super(queryService);
        this.rootFetchersDelegate = rootFetchersDelegate;
        this.objectFetchersDelegate = objectFetchersDelegate;

        this.minimum = environment -> {
            XtdInterval interval = environment.getSource();
            return queryService.getMinimum(interval);
        };

        this.maximum = environment -> {
            XtdInterval interval = environment.getSource();
            return queryService.getMaximum(interval);
        };
    }

    @Override
    public String getTypeName() {
        return "XtdInterval";
    }

    @Override
    public String getFetcherName() {
        return "getInterval";
    }

    @Override
    public String getListFetcherName() {
        return "findIntervals";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        Map<String, DataFetcher> fetchers = new HashMap<>();
        fetchers.putAll(super.getAttributeFetchers());
        // fetchers.putAll(rootFetchersDelegate.getFetchers());
        // fetchers.putAll(objectFetchersDelegate.getFetchers());
        fetchers.put("minimum", minimum);
        fetchers.put("maximum", maximum);
        
        return fetchers;
    }
}
