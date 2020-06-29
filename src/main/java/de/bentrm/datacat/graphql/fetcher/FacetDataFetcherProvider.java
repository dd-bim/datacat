package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.domain.Facet;
import de.bentrm.datacat.service.FacetService;
import de.bentrm.datacat.service.dto.FacetInput;
import de.bentrm.datacat.service.dto.FacetUpdateInput;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class FacetDataFetcherProvider
        extends EntityDataFetcherProviderImpl<Facet, FacetInput, FacetUpdateInput, FacetService>
        implements QueryDataFetcherProvider, MutationDataFetcherProvider {

    public FacetDataFetcherProvider(FacetService entityService) {
        super(FacetInput.class, FacetUpdateInput.class, entityService);
    }

    @Override
    public Map<String, DataFetcher> getQueryDataFetchers() {
        return Map.ofEntries(
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
}
