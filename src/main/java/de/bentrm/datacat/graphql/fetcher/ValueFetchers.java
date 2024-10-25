package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdOrderedValue;
import de.bentrm.datacat.catalog.domain.XtdValue;
import de.bentrm.datacat.catalog.domain.XtdValueList;
import de.bentrm.datacat.catalog.service.ValueRecordService;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.fetcher.delegate.ObjectFetchersDelegate;
import de.bentrm.datacat.graphql.fetcher.delegate.RootFetchersDelegate;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ValueFetchers extends AbstractFetchers<XtdValue> {

    private final RootFetchersDelegate rootFetchersDelegate;
    private final ObjectFetchersDelegate objectFetchersDelegate;
    // private final DataFetcher<List<XtdOrderedValue>> orderedValues;

    public ValueFetchers(ValueRecordService queryService,
                         RootFetchersDelegate rootFetchersDelegate,
                         ObjectFetchersDelegate objectFetchersDelegate) {
        super(queryService);
        this.rootFetchersDelegate = rootFetchersDelegate;
        this.objectFetchersDelegate = objectFetchersDelegate;

        // this.orderedValues = environment -> {
        //     final XtdValue source = environment.getSource();
        //     return queryService.getOrderedValues(source);
        // };
    }

    @Override
    public String getTypeName() {
        return "XtdValue";
    }

    @Override
    public String getFetcherName() {
        return "getValue";
    }

    @Override
    public String getListFetcherName() {
        return "findValues";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        Map<String, DataFetcher> fetchers = new HashMap<>();

        fetchers.putAll(super.getAttributeFetchers());
        // fetchers.putAll(rootFetchersDelegate.getFetchers());
        // fetchers.putAll(objectFetchersDelegate.getFetchers());
        // fetchers.put("orderedValues", orderedValues);
        
        return fetchers;
    }
}
