package de.bentrm.datacat.graphql.fetchers;

import de.bentrm.datacat.catalog.domain.XtdRelComposes;
import de.bentrm.datacat.catalog.domain.XtdRoot;
import de.bentrm.datacat.catalog.service.ComposesService;
import de.bentrm.datacat.catalog.specification.RootSpecification;
import de.bentrm.datacat.graphql.Connection;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RelComposesFetchers
        extends AbstractFetchers<XtdRelComposes, ComposesService> {

    public RelComposesFetchers(ComposesService entityService) {
        super(entityService);
    }

    @Override
    public String getTypeName() {
        return "XtdRelComposes";
    }

    @Override
    public String getFetcherName() {
        return "composesRelation";
    }

    @Override
    public String getListFetcherName() {
        return "composesRelations";
    }

    @Override
    public String getMutationNameSuffix() {
        return "ComposesRelation";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        final Map<String, DataFetcher> fetchers = super.getAttributeFetchers();
        fetchers.put("relatedThings", relatedThings());
        return fetchers;
    }

    public DataFetcher<Connection<XtdRoot>> relatedThings() {
        return env -> {
            XtdRelComposes source = env.getSource();
            final RootSpecification specification = RootSpecification.builder()
                    .composedBy(source.getId())
                    .build();
            return fetchRootConnection(env, specification);
        };
    }
}
