package de.bentrm.datacat.graphql.fetchers;

import de.bentrm.datacat.catalog.domain.XtdRelSpecializes;
import de.bentrm.datacat.catalog.domain.XtdRoot;
import de.bentrm.datacat.catalog.service.SpecializesService;
import de.bentrm.datacat.catalog.specification.RootSpecification;
import de.bentrm.datacat.graphql.Connection;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RelSpecializesFetchers
        extends AbstractEntityFetchers<XtdRelSpecializes, SpecializesService> {

    public RelSpecializesFetchers(SpecializesService entityService) {
        super(entityService);
    }

    @Override
    public String getTypeName() {
        return "XtdRelSpecializes";
    }

    @Override
    public String getFetcherName() {
        return "specializesRelation";
    }

    @Override
    public String getListFetcherName() {
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
