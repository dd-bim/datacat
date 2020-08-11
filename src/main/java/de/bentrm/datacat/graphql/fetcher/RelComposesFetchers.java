package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.domain.relationship.XtdRelComposes;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.AssociationInput;
import de.bentrm.datacat.graphql.dto.AssociationUpdateInput;
import de.bentrm.datacat.service.RelComposesService;
import de.bentrm.datacat.specification.RootSpecification;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RelComposesFetchers
        extends AbstractEntityFetchers<XtdRelComposes, AssociationInput, AssociationUpdateInput, RelComposesService> {

    public RelComposesFetchers(RelComposesService entityService) {
        super(AssociationInput.class, AssociationUpdateInput.class, entityService);
    }

    @Override
    public String getTypeName() {
        return "XtdRelComposes";
    }

    @Override
    public String getQueryName() {
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
