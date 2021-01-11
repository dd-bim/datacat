package de.bentrm.datacat.graphql.fetcher.delegate;

import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.catalog.domain.XtdRelAssignsCollections;
import de.bentrm.datacat.catalog.domain.XtdRelAssignsProperties;
import de.bentrm.datacat.catalog.domain.XtdRelAssignsPropertyWithValues;
import de.bentrm.datacat.catalog.service.AssignsCollectionsRecordService;
import de.bentrm.datacat.catalog.service.AssignsPropertiesRecordService;
import de.bentrm.datacat.catalog.service.AssignsPropertyWithValuesRecordService;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.fetcher.RelationshipFetcher;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ObjectFetchersDelegate implements FetchingDelegate {

    private final RelationshipFetcher<XtdRelAssignsCollections> assignedCollectionsFetcher;
    private final RelationshipFetcher<XtdRelAssignsProperties> assignedPropertiesFetcher;
    private final RelationshipFetcher<XtdRelAssignsPropertyWithValues> assignedPropertiesWithValuesFetcher;

    public ObjectFetchersDelegate(AssignsCollectionsRecordService assignsCollectionsService,
                                  AssignsPropertiesRecordService assignsPropertiesService,
                                  AssignsPropertyWithValuesRecordService assignsPropertyWithValuesService) {
        this.assignedCollectionsFetcher = new RelationshipFetcher<>(assignsCollectionsService) {
            @Override
            public Connection<XtdRelAssignsCollections> get(DataFetchingEnvironment environment) {
                final XtdObject source = environment.getSource();
                final Set<XtdRelAssignsCollections> fieldValues = source.getAssignedCollections();
                return get(fieldValues, environment);
            }
        };

        this.assignedPropertiesFetcher = new RelationshipFetcher<>(assignsPropertiesService) {
            @Override
            public Connection<XtdRelAssignsProperties> get(DataFetchingEnvironment environment) {
                final XtdObject source = environment.getSource();
                final Set<XtdRelAssignsProperties> fieldValues = source.getAssignedProperties();
                return get(fieldValues, environment);
            }
        };

        this.assignedPropertiesWithValuesFetcher = new RelationshipFetcher<>(assignsPropertyWithValuesService) {
            @Override
            public Connection<XtdRelAssignsPropertyWithValues> get(DataFetchingEnvironment environment) {
                final XtdObject source = environment.getSource();
                final Set<XtdRelAssignsPropertyWithValues> fieldValues = source.getAssignedPropertiesWithValues();
                return get(fieldValues, environment);
            }
        };
    }

    @Override
    public Map<String, DataFetcher> getFetchers() {
        return Map.of(
                "assignedCollections", assignedCollectionsFetcher,
                "assignedProperties", assignedPropertiesFetcher,
                "assignedPropertiesWithValues", assignedPropertiesWithValuesFetcher
        );
    }
}
