package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.domain.relationship.XtdRelGroups;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.AssociationInput;
import de.bentrm.datacat.graphql.dto.AssociationUpdateInput;
import de.bentrm.datacat.service.RelGroupsService;
import de.bentrm.datacat.specification.RootSpecification;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RelGroupsFetchers
        extends AbstractEntityFetchers<XtdRelGroups, AssociationInput, AssociationUpdateInput, RelGroupsService> {

    public RelGroupsFetchers(RelGroupsService entityService) {
        super(AssociationInput.class, AssociationUpdateInput.class, entityService);
    }

    @Override
    public String getTypeName() {
        return "XtdRelGroups";
    }

    @Override
    public String getQueryName() {
        return "groupsRelations";
    }

    @Override
    public String getMutationNameSuffix() {
        return "GroupsRelation";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        final Map<String, DataFetcher> fetchers = super.getAttributeFetchers();
        fetchers.put("relatedThings", relatedThings());
        return fetchers;
    }

    public DataFetcher<Connection<XtdRoot>> relatedThings() {
        return env -> {
            XtdRelGroups source = env.getSource();
            final RootSpecification specification = RootSpecification.builder()
                    .groupedBy(source.getId())
                    .build();
            return fetchRootConnection(env, specification);
        };
    }
}
