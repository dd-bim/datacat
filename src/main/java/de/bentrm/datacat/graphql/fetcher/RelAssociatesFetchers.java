package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.domain.relationship.XtdRelAssociates;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.AssociationInput;
import de.bentrm.datacat.graphql.dto.AssociationUpdateInput;
import de.bentrm.datacat.service.RelAssociatesService;
import de.bentrm.datacat.specification.RootSpecification;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RelAssociatesFetchers
        extends AbstractEntityFetchers<XtdRelAssociates, AssociationInput, AssociationUpdateInput, RelAssociatesService> {

    @Autowired
    private RelAssociatesService relAssociatesService;

    public RelAssociatesFetchers(RelAssociatesService entityService) {
        super(AssociationInput.class, AssociationUpdateInput.class, entityService);
    }

    @Override
    public String getTypeName() {
        return "XtdRelAssociates";
    }

    @Override
    public String getQueryName() {
        return "associatesRelations";
    }

    @Override
    public String getMutationNameSuffix() {
        return "AssociatesRelation";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        final Map<String, DataFetcher> fetchers = super.getAttributeFetchers();
        fetchers.put("relatedThings", relatedThings());
        return fetchers;
    }

    public DataFetcher<Connection<XtdRelAssociates>> associates() {
        return environment -> {
            XtdRoot root = environment.getSource();
            List<String> ids = root.getAssociates().stream()
                    .map(XtdRelAssociates::getId)
                    .collect(Collectors.toList());
            var content = relAssociatesService.findAllByIds(ids);
            return Connection.of(content);
        };
    }

    public DataFetcher<Connection<XtdRelAssociates>> associatedBy() {
        return environment -> {
            XtdRoot source = environment.getSource();
            List<String> ids = source.getAssociatedBy().stream()
                    .map(XtdRelAssociates::getId)
                    .collect(Collectors.toList());
            var content = relAssociatesService.findAllByIds(ids);
            return Connection.of(content);
        };
    }

    public DataFetcher<Connection<XtdRoot>> relatedThings() {
        return env -> {
            XtdRelAssociates source = env.getSource();
            final RootSpecification specification = RootSpecification.builder()
                    .associatedBy(source.getId())
                    .build();
            return fetchRootConnection(env, specification);
        };
    }
}
