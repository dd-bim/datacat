package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.domain.collection.XtdNest;
import de.bentrm.datacat.graphql.dto.RootInput;
import de.bentrm.datacat.graphql.dto.RootUpdateInput;
import de.bentrm.datacat.service.NestService;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NestDataFetcherProvider
        extends EntityDataFetcherProviderImpl<XtdNest, RootInput, RootUpdateInput, NestService>
        implements QueryDataFetcherProvider, MutationDataFetcherProvider {

    public NestDataFetcherProvider(NestService entityService) {
        super(RootInput.class, RootUpdateInput.class, entityService);
    }

    @Override
    public Map<String, DataFetcher> getQueryDataFetchers() {
        return Map.ofEntries(
                Map.entry("nests", getAll())
        );
    }

    @Override
    public Map<String, DataFetcher> getMutationDataFetchers() {
        return Map.ofEntries(
                Map.entry("createNest", add()),
                Map.entry("updateNest", update()),
                Map.entry("deleteNest", remove())
        );
    }
}
