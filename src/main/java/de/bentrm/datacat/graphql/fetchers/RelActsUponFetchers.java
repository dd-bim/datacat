package de.bentrm.datacat.graphql.fetchers;

import de.bentrm.datacat.catalog.domain.XtdRelActsUpon;
import de.bentrm.datacat.catalog.domain.XtdRoot;
import de.bentrm.datacat.catalog.service.ActsUponService;
import de.bentrm.datacat.catalog.specification.RootSpecification;
import de.bentrm.datacat.graphql.Connection;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RelActsUponFetchers extends AbstractFetchers<XtdRelActsUpon, ActsUponService> {

    public RelActsUponFetchers(ActsUponService entityService) {
        super(entityService);
    }

    @Override
    public String getTypeName() {
        return "XtdRelActsUpon";
    }

    @Override
    public String getFetcherName() {
        return "actsUponRelation";
    }

    @Override
    public String getListFetcherName() {
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
            List<XtdRelActsUpon> results = getEntityService().findAllByIds(ids);
            return Connection.of(results);
        };
    }

    public DataFetcher<Connection<XtdRelActsUpon>> actedUponBy() {
        return environment -> {
            XtdRoot source = environment.getSource();
            List<String> ids = source.getActedUponBy().stream()
                    .map(XtdRelActsUpon::getId)
                    .collect(Collectors.toList());
            List<XtdRelActsUpon> list = getEntityService().findAllByIds(ids);
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
