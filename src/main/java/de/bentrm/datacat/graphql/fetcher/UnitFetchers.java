package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdRelAssignsUnits;
import de.bentrm.datacat.catalog.domain.XtdUnit;
import de.bentrm.datacat.catalog.service.AssignsUnitsRelationshipService;
import de.bentrm.datacat.catalog.service.UnitService;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.fetcher.delegate.ObjectFetchersDelegate;
import de.bentrm.datacat.graphql.fetcher.delegate.RootFetchersDelegate;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class UnitFetchers extends AbstractFetchers<XtdUnit> {

    private final RootFetchersDelegate rootFetchersDelegate;
    private final ObjectFetchersDelegate objectFetchersDelegate;
    private final RelationshipFetcher<XtdRelAssignsUnits> assignsUnitsFetcher;

    public UnitFetchers(UnitService entityService,
                        RootFetchersDelegate rootFetchersDelegate,
                        ObjectFetchersDelegate objectFetchersDelegate,
                        AssignsUnitsRelationshipService assignsUnitsService) {
        super(entityService);
        this.rootFetchersDelegate = rootFetchersDelegate;
        this.objectFetchersDelegate = objectFetchersDelegate;

        this.assignsUnitsFetcher = new RelationshipFetcher<>(assignsUnitsService) {
            @Override
            public Connection<XtdRelAssignsUnits> get(DataFetchingEnvironment environment) {
                final XtdUnit source = environment.getSource();
                final Set<XtdRelAssignsUnits> fieldValues = source.getAssignedTo();
                return get(fieldValues, environment);
            }
        };
    }

    @Override
    public String getTypeName() {
        return "XtdUnit";
    }

    @Override
    public String getFetcherName() {
        return "unit";
    }

    @Override
    public String getListFetcherName() {
        return "units";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        final Map<String, DataFetcher> fetchers = new HashMap<>(super.getAttributeFetchers());

        fetchers.putAll(super.getAttributeFetchers());
        fetchers.putAll(rootFetchersDelegate.getFetchers());
        fetchers.putAll(objectFetchersDelegate.getFetchers());

        fetchers.put("assignedTo", assignsUnitsFetcher);

        return fetchers;
    }
}
