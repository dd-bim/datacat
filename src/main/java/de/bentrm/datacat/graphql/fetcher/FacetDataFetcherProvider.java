package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.domain.Facet;
import de.bentrm.datacat.service.FacetService;
import de.bentrm.datacat.service.dto.FacetInput;
import de.bentrm.datacat.service.dto.FacetUpdateInput;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class FacetDataFetcherProvider
        extends EntityDataFetcherProviderImpl<Facet, FacetInput, FacetUpdateInput, FacetService>
        implements QueryDataFetcherProvider, MutationDataFetcherProvider {

    private final FacetService entityService;

    public FacetDataFetcherProvider(FacetService entityService) {
        super(FacetInput.class, FacetUpdateInput.class, entityService);
        this.entityService = entityService;
    }

    @Override
    public Map<String, DataFetcher> getQueryDataFetchers() {
        return Map.ofEntries(
                Map.entry("facet", get()),
                Map.entry("facets", getAll())
        );
    }

    @Override
    public Map<String, DataFetcher> getMutationDataFetchers() {
        return Map.ofEntries(
                Map.entry("createFacet", add()),
                Map.entry("updateFacet", update()),
                Map.entry("deleteFacet", remove())
        );
    }

    private DataFetcher<Optional<Facet>> get() {
        return env -> {
            final String id = env.getArgument("id");
            return this.entityService.findById(id);
        };
    }
}
