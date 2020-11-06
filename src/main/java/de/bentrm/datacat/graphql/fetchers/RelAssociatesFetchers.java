package de.bentrm.datacat.graphql.fetchers;

import de.bentrm.datacat.catalog.domain.XtdRelAssociates;
import de.bentrm.datacat.catalog.domain.XtdRoot;
import de.bentrm.datacat.catalog.service.AssociatesService;
import de.bentrm.datacat.catalog.specification.RootSpecification;
import de.bentrm.datacat.graphql.Connection;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RelAssociatesFetchers
        extends AbstractFetchers<XtdRelAssociates, AssociatesService> {

    public RelAssociatesFetchers(AssociatesService entityService) {
        super(entityService);
    }

    @Override
    public String getTypeName() {
        return "XtdRelAssociates";
    }

    @Override
    public String getFetcherName() {
        return "associatesRelation";
    }

    @Override
    public String getListFetcherName() {
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
            var content = getEntityService().findAllByIds(ids);
            return Connection.of(content);
        };
    }

    public DataFetcher<Connection<XtdRelAssociates>> associatedBy() {
        return environment -> {
            XtdRoot source = environment.getSource();
            List<String> ids = source.getAssociatedBy().stream()
                    .map(XtdRelAssociates::getId)
                    .collect(Collectors.toList());
            var content = getEntityService().findAllByIds(ids);
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
