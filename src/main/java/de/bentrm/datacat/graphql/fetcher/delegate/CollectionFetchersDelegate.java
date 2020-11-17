package de.bentrm.datacat.graphql.fetcher.delegate;

import de.bentrm.datacat.catalog.domain.XtdCollection;
import de.bentrm.datacat.catalog.domain.XtdRelAssignsCollections;
import de.bentrm.datacat.catalog.domain.XtdRelCollects;
import de.bentrm.datacat.catalog.service.AssignsCollectionsService;
import de.bentrm.datacat.catalog.service.CollectsService;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.fetcher.RelationshipFetcher;
import de.bentrm.datacat.graphql.fetcher.TagsFetcher;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CollectionFetchersDelegate implements FetchingDelegate {

    private final RelationshipFetcher<XtdRelCollects> collectsDataFetcher;
    private final RelationshipFetcher<XtdRelAssignsCollections> assignedToFetcher;

    public CollectionFetchersDelegate(CollectsService collectsService,
                                      AssignsCollectionsService assignsCollectionsService) {
        this.collectsDataFetcher = new RelationshipFetcher<>(collectsService) {
            @Override
            public Connection<XtdRelCollects> get(DataFetchingEnvironment environment) throws Exception {
                final XtdCollection source = environment.getSource();
                final Set<XtdRelCollects> fieldValues = source.getCollects();
                return get(fieldValues, environment);
            }
        };

        this.assignedToFetcher = new RelationshipFetcher<>(assignsCollectionsService) {
            @Override
            public Connection<XtdRelAssignsCollections> get(DataFetchingEnvironment environment) throws Exception {
                final XtdCollection source = environment.getSource();
                final Set<XtdRelAssignsCollections> fieldValues = source.getAssignedTo();
                return get(fieldValues, environment);
            }
        };
    }

    @Override
    public Map<String, DataFetcher> getFetchers() {
        return Map.of(
                "tags", new TagsFetcher(),
                "collects", collectsDataFetcher,
                "assignedTo", assignedToFetcher
        );
    }
}
