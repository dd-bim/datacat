package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.domain.relationship.XtdRelSpecializes;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.AssociationInput;
import de.bentrm.datacat.graphql.dto.AssociationUpdateInput;
import de.bentrm.datacat.service.RelSpecializesService;
import de.bentrm.datacat.specification.RootSpecification;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RelSpecializesFetchers
        extends AbstractEntityFetchers<XtdRelSpecializes, AssociationInput, AssociationUpdateInput, RelSpecializesService> {

    public RelSpecializesFetchers(RelSpecializesService entityService) {
        super(AssociationInput.class, AssociationUpdateInput.class, entityService);
    }

    @Override
    public String getTypeName() {
        return "XtdRelSpecializes";
    }

    @Override
    public String getQueryName() {
        return "specializesRelations";
    }

    @Override
    public String getMutationNameSuffix() {
        return "SpecializesRelation";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        final Map<String, DataFetcher> fetchers = super.getAttributeFetchers();
        fetchers.put("relatedThings", relatedThings());
        return fetchers;
    }

    public DataFetcher<Connection<XtdRoot>> relatedThings() {
        return env -> {
            XtdRelSpecializes source = env.getSource();
            final RootSpecification specification = RootSpecification.builder()
                    .specializedBy(source.getId())
                    .build();
            return fetchRootConnection(env, specification);
        };
    }
}
