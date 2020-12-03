package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdRelAssignsValues;
import de.bentrm.datacat.catalog.domain.XtdValue;
import de.bentrm.datacat.catalog.service.AssignsValuesService;
import de.bentrm.datacat.catalog.service.ValueService;
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
    private final RelationshipFetcher<XtdRelAssignsValues> assignsValuesFetcher;

    public ValueFetchers(ValueService queryService,
                         RootFetchersDelegate rootFetchersDelegate,
                         ObjectFetchersDelegate objectFetchersDelegate,
                         AssignsValuesService assignsValuesService) {
        super(queryService);
        this.rootFetchersDelegate = rootFetchersDelegate;
        this.objectFetchersDelegate = objectFetchersDelegate;

        this.assignsValuesFetcher = new RelationshipFetcher<>(assignsValuesService) {
            @Override
            public Connection<XtdRelAssignsValues> get(DataFetchingEnvironment environment) {
                final XtdValue source = environment.getSource();
                final List<XtdRelAssignsValues> fieldValues = source.getAssignedTo();
                return get(fieldValues, environment);
            }
        };
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
        fetchers.putAll(rootFetchersDelegate.getFetchers());
        fetchers.putAll(objectFetchersDelegate.getFetchers());

        fetchers.put("assignedTo", assignsValuesFetcher);

        return fetchers;
    }
}
