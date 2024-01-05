package de.bentrm.datacat.graphql.fetcher.delegate;

import de.bentrm.datacat.catalog.domain.*;
import de.bentrm.datacat.catalog.service.*;
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
public class RootFetchersDelegate implements FetchingDelegate {

    private final RelationshipFetcher<XtdRelCollects> collectedByFetcher;
    private final RelationshipFetcher<XtdRelClassifies> classifiedByFetcher;

    public RootFetchersDelegate(CollectsRecordService collectsService,
                                ClassifiesService classifiesService) {

        this.collectedByFetcher = new RelationshipFetcher<>(collectsService) {
            @Override
            public Connection<XtdRelCollects> get(DataFetchingEnvironment environment) {
                final XtdRoot source = environment.getSource();
                final Set<XtdRelCollects> fieldValues = source.getCollectedBy();
                return get(fieldValues, environment);
            }
        };

        this.classifiedByFetcher = new RelationshipFetcher<>(classifiesService) {
            @Override
            public Connection<XtdRelClassifies> get(DataFetchingEnvironment environment) {
                final XtdRoot source = environment.getSource();
                final Set<XtdRelClassifies> fieldValues = source.getClassifiedBy();
                return get(fieldValues, environment);
            }
        };

    }

    @Override
    public Map<String, DataFetcher> getFetchers() {
        return Map.of(
                "classifiedBy", classifiedByFetcher,
                "collectedBy", collectedByFetcher
        );
    }

}
