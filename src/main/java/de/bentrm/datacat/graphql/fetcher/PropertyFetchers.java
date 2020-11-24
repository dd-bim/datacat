package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdRelAssignsMeasures;
import de.bentrm.datacat.catalog.domain.XtdRelAssignsProperties;
import de.bentrm.datacat.catalog.service.AssignsMeasuresRelationshipService;
import de.bentrm.datacat.catalog.service.AssignsPropertiesService;
import de.bentrm.datacat.catalog.service.PropertyService;
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
public class PropertyFetchers extends AbstractFetchers<XtdProperty> {

    private final RootFetchersDelegate rootFetchersDelegate;
    private final ObjectFetchersDelegate objectFetchersDelegate;

    private final RelationshipFetcher<XtdRelAssignsMeasures> assignedMeasuresFetcher;
    private final RelationshipFetcher<XtdRelAssignsProperties> assignedToFetcher;

    public PropertyFetchers(PropertyService queryService,
                            RootFetchersDelegate rootFetchersDelegate,
                            ObjectFetchersDelegate objectFetchersDelegate,
                            AssignsPropertiesService assignsPropertiesService,
                            AssignsMeasuresRelationshipService assignsMeasuresRelationshipService) {
        super(queryService);

        this.rootFetchersDelegate = rootFetchersDelegate;
        this.objectFetchersDelegate = objectFetchersDelegate;


        this.assignedMeasuresFetcher = new RelationshipFetcher<>(assignsMeasuresRelationshipService) {
            @Override
            public Connection<XtdRelAssignsMeasures> get(DataFetchingEnvironment environment) {
                final XtdProperty source = environment.getSource();
                final Set<XtdRelAssignsMeasures> fieldValues = source.getAssignedMeasures();
                return get(fieldValues, environment);
            }
        };

        this.assignedToFetcher = new RelationshipFetcher<>(assignsPropertiesService) {
            @Override
            public Connection<XtdRelAssignsProperties> get(DataFetchingEnvironment environment) {
                final XtdProperty source = environment.getSource();
                final Set<XtdRelAssignsProperties> fieldValues = source.getAssignedProperties();
                return get(fieldValues, environment);
            }
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
        fetchers.putAll(rootFetchersDelegate.getFetchers());
        fetchers.putAll(objectFetchersDelegate.getFetchers());

        fetchers.put("assignedMeasures", assignedMeasuresFetcher);
        fetchers.put("assignedTo", assignedToFetcher);

        return fetchers;
    }
}
