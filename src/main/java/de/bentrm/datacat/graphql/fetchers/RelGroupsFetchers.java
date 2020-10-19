package de.bentrm.datacat.graphql.fetchers;

import de.bentrm.datacat.catalog.domain.XtdRelGroups;
import de.bentrm.datacat.catalog.domain.XtdRoot;
import de.bentrm.datacat.catalog.service.GroupsService;
import de.bentrm.datacat.catalog.specification.RootSpecification;
import de.bentrm.datacat.graphql.Connection;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RelGroupsFetchers
        extends AbstractEntityFetchers<XtdRelGroups, GroupsService> {

    public RelGroupsFetchers(GroupsService entityService) {
        super(entityService);
    }

    @Override
    public String getTypeName() {
        return "XtdRelGroups";
    }

    @Override
    public String getFetcherName() {
        return "groupsRelation";
    }

    @Override
    public String getListFetcherName() {
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
