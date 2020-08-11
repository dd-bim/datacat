package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.domain.relationship.XtdRelCollects;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.CollectsInput;
import de.bentrm.datacat.graphql.dto.CollectsUpdateInput;
import de.bentrm.datacat.service.RelCollectsService;
import de.bentrm.datacat.specification.RootSpecification;
import graphql.schema.DataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class RelCollectsFetchers
        extends AbstractEntityFetchers<XtdRelCollects, CollectsInput, CollectsUpdateInput, RelCollectsService> {

    public RelCollectsFetchers(RelCollectsService entityService) {
        super(CollectsInput.class, CollectsUpdateInput.class, entityService);
    }

    @Override
    public String getTypeName() {
        return "XtdRelCollects";
    }

    @Override
    public String getQueryName() {
        return "collectsRelations";
    }

    @Override
    public String getMutationNameSuffix() {
        return "CollectsRelation";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        final Map<String, DataFetcher> fetchers = super.getAttributeFetchers();
        fetchers.put("relatedThings", relatedThings());
        return fetchers;
    }

    public DataFetcher<Connection<XtdRoot>> relatedThings() {
        return env -> {
            XtdRelCollects source = env.getSource();
            final RootSpecification specification = RootSpecification.builder()
                    .collectedBy(source.getId())
                    .build();
            return fetchRootConnection(env, specification);
        };
    }
}
