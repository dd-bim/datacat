package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdUnit;
import de.bentrm.datacat.catalog.domain.XtdOrderedValue;
import de.bentrm.datacat.catalog.domain.XtdValueList;
import de.bentrm.datacat.catalog.service.ValueListRecordService;
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
public class ValueListFetchers extends AbstractFetchers<XtdValueList> {

    private final RootFetchersDelegate rootFetchersDelegate;
    private final ObjectFetchersDelegate objectFetchersDelegate;
    private final DataFetcher<List<XtdOrderedValue>> orderedValues;
    private final DataFetcher<List<XtdProperty>> properties;
    private final DataFetcher<XtdUnit> unit;

    public ValueListFetchers(ValueListRecordService queryService,
                           RootFetchersDelegate rootFetchersDelegate,
                           ObjectFetchersDelegate objectFetchersDelegate) {
        super(queryService);
        this.rootFetchersDelegate = rootFetchersDelegate;
        this.objectFetchersDelegate = objectFetchersDelegate;
        
        this.orderedValues = environment -> {
            final XtdValueList source = environment.getSource();
            return queryService.getOrderedValues(source);
        };

        this.properties = environment -> {
            final XtdValueList source = environment.getSource();
            return queryService.getProperties(source);
        };

        this.unit = environment -> {
            final XtdValueList source = environment.getSource();
            return queryService.getUnit(source);
        };
    }

    @Override
    public String getTypeName() {
        return "XtdValueList";
    }

    @Override
    public String getFetcherName() {
        return "getValueList";
    }

    @Override
    public String getListFetcherName() {
        return "findValueLists";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        Map<String, DataFetcher> fetchers = new HashMap<>();

        fetchers.putAll(super.getAttributeFetchers());
        // fetchers.putAll(rootFetchersDelegate.getFetchers());
        // fetchers.putAll(objectFetchersDelegate.getFetchers());
        fetchers.put("values", orderedValues);
        fetchers.put("properties", properties);
        fetchers.put("unit", unit);

        return fetchers;
    }
}
