package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.domain.XtdRoot;
import de.bentrm.datacat.domain.relationship.XtdRelActsUpon;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.AssociationInput;
import de.bentrm.datacat.graphql.dto.AssociationUpdateInput;
import de.bentrm.datacat.service.RelActsUponService;
import de.bentrm.datacat.specification.RootSpecification;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RelActsUponFetchers extends AbstractEntityFetchers<XtdRelActsUpon, AssociationInput, AssociationUpdateInput, RelActsUponService> {

    public RelActsUponFetchers(RelActsUponService entityService) {
        super(AssociationInput.class, AssociationUpdateInput.class, entityService);
    }

    @Override
    public String getTypeName() {
        return "XtdRelActsUpon";
    }

    @Override
    public String getQueryName() {
        return "actsUponRelations";
    }

    @Override
    public String getMutationNameSuffix() {
        return "ActsUponRelation";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        final Map<String, DataFetcher> fetchers = super.getAttributeFetchers();
        fetchers.put("relatedThings", relatedThings());
        return fetchers;
    }

    public DataFetcher<Connection<XtdRelActsUpon>> actsUpon() {
        return environment -> {
            XtdRoot root = environment.getSource();
            List<String> ids = root.getActsUpon().stream()
                    .map(XtdRelActsUpon::getId)
                    .collect(Collectors.toList());
            List<XtdRelActsUpon> results = entityService.findAllByIds(ids);
            return Connection.of(results);
        };
    }

    public DataFetcher<Connection<XtdRelActsUpon>> actedUponBy() {
        return environment -> {
            XtdRoot source = environment.getSource();
            List<String> ids = source.getActedUponBy().stream()
                    .map(XtdRelActsUpon::getId)
                    .collect(Collectors.toList());
            List<XtdRelActsUpon> list = entityService.findAllByIds(ids);
            return Connection.of(list);
        };
    }

    public DataFetcher<Connection<XtdRoot>> relatedThings() {
        return env -> {
            XtdRelActsUpon source = env.getSource();
            final RootSpecification specification = RootSpecification.builder()
                    .actedUponBy(source.getId())
                    .build();
            return fetchRootConnection(env, specification);
        };
    }
}
