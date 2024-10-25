package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdOrderedValue;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdValue;
import de.bentrm.datacat.catalog.domain.XtdValueList;
import de.bentrm.datacat.catalog.service.OrderedValueRecordService;
import de.bentrm.datacat.graphql.fetcher.delegate.ObjectFetchersDelegate;
import de.bentrm.datacat.graphql.fetcher.delegate.RootFetchersDelegate;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class OrderedValueFetchers extends AbstractFetchers<XtdOrderedValue> {

    private final RootFetchersDelegate rootFetchersDelegate;
    private final ObjectFetchersDelegate objectFetchersDelegate;
    private final DataFetcher<XtdValue> value;
    private final DataFetcher<List<XtdValueList>> valueLists;

    public OrderedValueFetchers(OrderedValueRecordService queryService,
                           RootFetchersDelegate rootFetchersDelegate,
                           ObjectFetchersDelegate objectFetchersDelegate) {
        super(queryService);
        this.rootFetchersDelegate = rootFetchersDelegate;
        this.objectFetchersDelegate = objectFetchersDelegate;
        
        this.value = environment -> {
            final XtdOrderedValue source = environment.getSource();
            return queryService.getValue(source);
        };

        this.valueLists = environment -> {
            final XtdOrderedValue source = environment.getSource();
            return queryService.getValueLists(source);
        };
    }

    @Override
    public String getTypeName() {
        return "XtdOrderedValue";
    }

    @Override
    public String getFetcherName() {
        return "getOrderedValue";
    }

    @Override
    public String getListFetcherName() {
        return "findOrderedValues";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        Map<String, DataFetcher> fetchers = new HashMap<>();

        fetchers.putAll(super.getAttributeFetchers());
        // fetchers.putAll(rootFetchersDelegate.getFetchers());
        // fetchers.putAll(objectFetchersDelegate.getFetchers());
        fetchers.put("orderedValue", value);
        fetchers.put("valueLists", valueLists);

        return fetchers;
    }
}
