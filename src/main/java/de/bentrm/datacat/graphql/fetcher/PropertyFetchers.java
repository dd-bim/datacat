package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdDimension;
import de.bentrm.datacat.catalog.domain.XtdInterval;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdQuantityKind;
import de.bentrm.datacat.catalog.domain.XtdRelationshipToProperty;
import de.bentrm.datacat.catalog.domain.XtdSubject;
import de.bentrm.datacat.catalog.domain.XtdSymbol;
import de.bentrm.datacat.catalog.domain.XtdValueList;
import de.bentrm.datacat.catalog.domain.XtdUnit;
import de.bentrm.datacat.catalog.service.PropertyRecordService;
import de.bentrm.datacat.graphql.fetcher.delegate.ObjectFetchersDelegate;
import de.bentrm.datacat.graphql.fetcher.delegate.RootFetchersDelegate;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Component
public class PropertyFetchers extends AbstractFetchers<XtdProperty> {

    private final RootFetchersDelegate rootFetchersDelegate;
    private final ObjectFetchersDelegate objectFetchersDelegate;
    private final DataFetcher<List<XtdSubject>> subjects;
    private final DataFetcher<List<XtdValueList>> valueLists;
    private final DataFetcher<List<XtdUnit>> units;
    private final DataFetcher<List<XtdRelationshipToProperty>> connectedProperties;
    private final DataFetcher<List<XtdRelationshipToProperty>> connectingProperties;
    private final DataFetcher<XtdDimension> dimension;
    private final DataFetcher<List<XtdSymbol>> symbols;
    private final DataFetcher<List<XtdInterval>> intervals;
    private final DataFetcher<List<XtdQuantityKind>> quantityKinds;

    public PropertyFetchers(PropertyRecordService queryService,
                            RootFetchersDelegate rootFetchersDelegate,
                            ObjectFetchersDelegate objectFetchersDelegate) {
        super(queryService);
        this.rootFetchersDelegate = rootFetchersDelegate;
        this.objectFetchersDelegate = objectFetchersDelegate;

        this.subjects = environment -> {
            final XtdProperty source = environment.getSource();
            return queryService.getSubjects(source);
        };

        this.valueLists = environment -> {
            final XtdProperty source = environment.getSource();
            return queryService.getValueLists(source);
        };

        this.units = environment -> {
            final XtdProperty source = environment.getSource();
            return queryService.getUnits(source);
        };

        this.connectedProperties = environment -> {
            final XtdProperty source = environment.getSource();
            return queryService.getConnectedProperties(source);
        };

        this.connectingProperties = environment -> {
            final XtdProperty source = environment.getSource();
            return queryService.getConnectingProperties(source);
        };

        this.dimension = environment -> {
            final XtdProperty source = environment.getSource();
            return queryService.getDimension(source);
        };

        this.symbols = environment -> {
            final XtdProperty source = environment.getSource();
            return queryService.getSymbols(source);
        };

        this.intervals = environment -> {
            final XtdProperty source = environment.getSource();
            return queryService.getIntervals(source);
        };

        this.quantityKinds = environment -> {
            final XtdProperty source = environment.getSource();
            return queryService.getQuantityKinds(source);
        };
    }

    @Override
    public String getTypeName() {
        return "XtdProperty";
    }

    @Override
    public String getFetcherName() {
        return "getProperty";
    }

    @Override
    public String getListFetcherName() {
        return "findProperties";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        Map<String, DataFetcher> fetchers = new HashMap<>();

        fetchers.putAll(super.getAttributeFetchers());
        // fetchers.putAll(rootFetchersDelegate.getFetchers());
        // fetchers.putAll(objectFetchersDelegate.getFetchers());
        fetchers.put("subjects", subjects);
        fetchers.put("possibleValues", valueLists);
        fetchers.put("units", units);
        fetchers.put("connectedProperties", connectedProperties);
        fetchers.put("connectingProperties", connectingProperties);
        fetchers.put("dimension", dimension);
        fetchers.put("symbols", symbols);
        fetchers.put("boundaryValues", intervals);
        fetchers.put("quantityKinds", quantityKinds);

        return fetchers;
    }
}
