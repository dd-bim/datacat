package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdQuantityKind;
import de.bentrm.datacat.catalog.domain.XtdUnit;
import de.bentrm.datacat.catalog.domain.XtdDimension;
import de.bentrm.datacat.catalog.service.QuantityKindRecordService;
import de.bentrm.datacat.graphql.fetcher.delegate.ObjectFetchersDelegate;
import de.bentrm.datacat.graphql.fetcher.delegate.RootFetchersDelegate;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class QuantityKindFetchers extends AbstractFetchers<XtdQuantityKind> {

    private final RootFetchersDelegate rootFetchersDelegate;
    private final ObjectFetchersDelegate objectFetchersDelegate;

    private final DataFetcher<List<XtdUnit>> units;
    private final DataFetcher<XtdDimension> dimension;

    public QuantityKindFetchers(QuantityKindRecordService queryService,
                           RootFetchersDelegate rootFetchersDelegate,
                           ObjectFetchersDelegate objectFetchersDelegate) {
        super(queryService);
        this.rootFetchersDelegate = rootFetchersDelegate;
        this.objectFetchersDelegate = objectFetchersDelegate;

        this.units = environment -> {
            XtdQuantityKind quantityKind = environment.getSource();
            return queryService.getUnits(quantityKind);
        };

        this.dimension = environment -> {
            XtdQuantityKind quantityKind = environment.getSource();
            return queryService.getDimension(quantityKind);
        };
    }

    @Override
    public String getTypeName() {
        return "XtdQuantityKind";
    }

    @Override
    public String getFetcherName() {
        return "getQuantityKind";
    }

    @Override
    public String getListFetcherName() {
        return "findQuantityKinds";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        Map<String, DataFetcher> fetchers = new HashMap<>();
        fetchers.putAll(super.getAttributeFetchers());
        // fetchers.putAll(rootFetchersDelegate.getFetchers());
        // fetchers.putAll(objectFetchersDelegate.getFetchers());
        fetchers.put("units", units);
        fetchers.put("dimension", dimension);
        return fetchers;
    }
}
