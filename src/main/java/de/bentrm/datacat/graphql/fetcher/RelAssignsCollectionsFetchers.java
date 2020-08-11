package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.domain.relationship.XtdRelAssignsCollections;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.AssignsCollectionsInput;
import de.bentrm.datacat.graphql.dto.AssignsCollectionsUpdateInput;
import de.bentrm.datacat.service.RelAssignsCollectionsService;
import de.bentrm.datacat.specification.RootSpecification;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RelAssignsCollectionsFetchers
        extends AbstractEntityFetchers<XtdRelAssignsCollections, AssignsCollectionsInput, AssignsCollectionsUpdateInput, RelAssignsCollectionsService> {

    public RelAssignsCollectionsFetchers(RelAssignsCollectionsService entityService) {
        super(AssignsCollectionsInput.class, AssignsCollectionsUpdateInput.class, entityService);
    }

    @Override
    public String getTypeName() {
        return "XtdRelAssignsCollections";
    }

    @Override
    public String getQueryName() {
        return "assignsCollectionsRelations";
    }

    @Override
    public String getMutationNameSuffix() {
        return "AssignsCollectionsRelation";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        final Map<String, DataFetcher> fetchers = super.getAttributeFetchers();
        fetchers.put("relatedThings", relatedThings());
        return fetchers;
    }

    public DataFetcher<Connection<XtdRoot>> relatedThings() {
        return env -> {
            XtdRelAssignsCollections source = env.getSource();
            final RootSpecification specification = RootSpecification.builder()
                    .actedUponBy(source.getId())
                    .build();
            return fetchRootConnection(env, specification);
        };
    }
}
