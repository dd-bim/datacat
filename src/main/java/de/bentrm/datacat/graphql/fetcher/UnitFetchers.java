package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdDimension;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdUnit;
import de.bentrm.datacat.catalog.service.UnitRecordService;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.fetcher.delegate.ObjectFetchersDelegate;
import de.bentrm.datacat.graphql.fetcher.delegate.RootFetchersDelegate;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class UnitFetchers extends AbstractFetchers<XtdUnit> {

    private final RootFetchersDelegate rootFetchersDelegate;
    private final ObjectFetchersDelegate objectFetchersDelegate;
    private final DataFetcher<List<XtdProperty>> properties;
    private final DataFetcher<XtdDimension> dimension;

    public UnitFetchers(UnitRecordService entityService,
                        RootFetchersDelegate rootFetchersDelegate,
                        ObjectFetchersDelegate objectFetchersDelegate) {
        super(entityService);
        this.rootFetchersDelegate = rootFetchersDelegate;
        this.objectFetchersDelegate = objectFetchersDelegate;

        this.properties = environment -> {
            final XtdUnit source = environment.getSource();
            return entityService.getProperties(source);
        };

        this.dimension = environment -> {
            final XtdUnit source = environment.getSource();
            return entityService.getDimension(source);
        };
    }

    @Override
    public String getTypeName() {
        return "XtdUnit";
    }

    @Override
    public String getFetcherName() {
        return "getUnit";
    }

    @Override
    public String getListFetcherName() {
        return "findUnits";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        final Map<String, DataFetcher> fetchers = new HashMap<>(super.getAttributeFetchers());

        fetchers.putAll(super.getAttributeFetchers());
        // fetchers.putAll(rootFetchersDelegate.getFetchers());
        // fetchers.putAll(objectFetchersDelegate.getFetchers());
        fetchers.put("properties", properties);
        fetchers.put("dimension", dimension);

        return fetchers;
    }
}
