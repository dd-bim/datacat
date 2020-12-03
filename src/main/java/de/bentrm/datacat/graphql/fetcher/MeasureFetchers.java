package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdMeasureWithUnit;
import de.bentrm.datacat.catalog.domain.XtdRelAssignsMeasures;
import de.bentrm.datacat.catalog.domain.XtdRelAssignsUnits;
import de.bentrm.datacat.catalog.domain.XtdRelAssignsValues;
import de.bentrm.datacat.catalog.service.AssignsMeasuresService;
import de.bentrm.datacat.catalog.service.AssignsUnitsService;
import de.bentrm.datacat.catalog.service.AssignsValuesService;
import de.bentrm.datacat.catalog.service.MeasureService;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.fetcher.delegate.ObjectFetchersDelegate;
import de.bentrm.datacat.graphql.fetcher.delegate.RootFetchersDelegate;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class MeasureFetchers extends AbstractFetchers<XtdMeasureWithUnit> {

    private final RootFetchersDelegate rootFetchersDelegate;
    private final ObjectFetchersDelegate objectFetchersDelegate;

    private final RelationshipFetcher<XtdRelAssignsUnits> assignedUnitsFetcher;
    private final RelationshipFetcher<XtdRelAssignsValues> assignedValuesFetcher;
    private final RelationshipFetcher<XtdRelAssignsMeasures> assignedToFetcher;

    public MeasureFetchers(MeasureService queryService,
                           RootFetchersDelegate rootFetchersDelegate,
                           ObjectFetchersDelegate objectFetchersDelegate,
                           AssignsUnitsService assignsUnitsService,
                           AssignsValuesService assignsValuesService,
                           AssignsMeasuresService assignsMeasuresService) {
        super(queryService);
        this.rootFetchersDelegate = rootFetchersDelegate;
        this.objectFetchersDelegate = objectFetchersDelegate;

        this.assignedUnitsFetcher = new RelationshipFetcher<>(assignsUnitsService) {
            @Override
            public Connection<XtdRelAssignsUnits> get(DataFetchingEnvironment environment) {
                final XtdMeasureWithUnit source = environment.getSource();
                final Set<XtdRelAssignsUnits> fieldValues = source.getAssignedUnits();
                return get(fieldValues, environment);
            }
        };

        this.assignedValuesFetcher = new RelationshipFetcher<>(assignsValuesService) {
            @Override
            public Connection<XtdRelAssignsValues> get(DataFetchingEnvironment environment) throws Exception {
                final XtdMeasureWithUnit source = environment.getSource();
                final Set<XtdRelAssignsValues> fieldValues = source.getAssignedValues();
                return get(fieldValues, environment);
            }
        };

        this.assignedToFetcher = new RelationshipFetcher<>(assignsMeasuresService) {
            @Override
            public Connection<XtdRelAssignsMeasures> get(DataFetchingEnvironment environment) throws Exception {
                final XtdMeasureWithUnit source = environment.getSource();
                log.trace("Current XtdMeasureWithUnit: {}", source);
                final Set<XtdRelAssignsMeasures> fieldValues = source.getAssignedTo();
                log.trace("Fetching measure assignment: {}", fieldValues);
                return get(fieldValues, environment);
            }
        };
    }

    @Override
    public String getTypeName() {
        return "XtdMeasureWithUnit";
    }

    @Override
    public String getFetcherName() {
        return "getMeasure";
    }

    @Override
    public String getListFetcherName() {
        return "findMeasures";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        Map<String, DataFetcher> fetchers = new HashMap<>();

        fetchers.putAll(super.getAttributeFetchers());
        fetchers.putAll(rootFetchersDelegate.getFetchers());
        fetchers.putAll(objectFetchersDelegate.getFetchers());

        fetchers.put("assignedUnits", assignedUnitsFetcher);
        fetchers.put("assignedValues", assignedValuesFetcher);
        fetchers.put("assignedTo", assignedToFetcher);

        return fetchers;
    }
}
