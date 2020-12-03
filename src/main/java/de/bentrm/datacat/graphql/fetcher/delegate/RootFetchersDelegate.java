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

    private final RelationshipFetcher<XtdRelAssociates> associatesFetcher;
    private final RelationshipFetcher<XtdRelAssociates> associatedByFetcher;
    private final RelationshipFetcher<XtdRelCollects> collectedByFetcher;
    private final RelationshipFetcher<XtdRelComposes> composesFetcher;
    private final RelationshipFetcher<XtdRelComposes> composedByFetcher;
    private final RelationshipFetcher<XtdRelDocuments> documentedByFetcher;
    private final RelationshipFetcher<XtdRelClassifies> classifiedByFetcher;

    public RootFetchersDelegate(AssociatesService associatesService,
                                CollectsService collectsService,
                                ComposesService composesService,
                                ClassifiesService classifiesService,
                                DocumentsService documentsService) {
        this.associatesFetcher = new RelationshipFetcher<>(associatesService) {
            @Override
            public Connection<XtdRelAssociates> get(DataFetchingEnvironment environment) {
                final XtdRoot source = environment.getSource();
                final Set<XtdRelAssociates> fieldValues = source.getAssociates();
                return get(fieldValues, environment);
            }
        };

        this.associatedByFetcher = new RelationshipFetcher<>(associatesService) {
            @Override
            public Connection<XtdRelAssociates> get(DataFetchingEnvironment environment) {
                final XtdRoot source = environment.getSource();
                final Set<XtdRelAssociates> fieldValues = source.getAssociatedBy();
                return get(fieldValues, environment);
            }
        };

        this.collectedByFetcher = new RelationshipFetcher<>(collectsService) {
            @Override
            public Connection<XtdRelCollects> get(DataFetchingEnvironment environment) {
                final XtdRoot source = environment.getSource();
                final Set<XtdRelCollects> fieldValues = source.getCollectedBy();
                return get(fieldValues, environment);
            }
        };

        this.composesFetcher = new RelationshipFetcher<>(composesService) {
            @Override
            public Connection<XtdRelComposes> get(DataFetchingEnvironment environment) {
                final XtdCollection source = environment.getSource();
                final Set<XtdRelComposes> fieldValues = source.getComposes();
                return get(fieldValues, environment);
            }
        };

        this.composedByFetcher = new RelationshipFetcher<>(composesService) {
            @Override
            public Connection<XtdRelComposes> get(DataFetchingEnvironment environment) {
                final XtdRoot source = environment.getSource();
                final Set<XtdRelComposes> fieldValues = source.getComposedBy();
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

        this.documentedByFetcher = new RelationshipFetcher<>(documentsService) {
            @Override
            public Connection<XtdRelDocuments> get(DataFetchingEnvironment environment) {
                final XtdRoot source = environment.getSource();
                final Set<XtdRelDocuments> fieldValues = source.getDocumentedBy();
                return get(fieldValues, environment);
            }
        };
    }

    @Override
    public Map<String, DataFetcher> getFetchers() {
        return Map.of(
                "associatedBy", associatedByFetcher,
                "associates", associatesFetcher,
                "classifiedBy", classifiedByFetcher,
                "collectedBy", collectedByFetcher,
                "composedBy", composedByFetcher,
                "composes", composesFetcher,
                "documentedBy", documentedByFetcher
        );
    }

}
