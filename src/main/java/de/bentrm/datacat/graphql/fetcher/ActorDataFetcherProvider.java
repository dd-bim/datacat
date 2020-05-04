package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.domain.XtdActor;
import de.bentrm.datacat.graphql.dto.RootInput;
import de.bentrm.datacat.graphql.dto.RootUpdateInput;
import de.bentrm.datacat.service.ActorService;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ActorDataFetcherProvider
        extends EntityDataFetcherProviderImpl<XtdActor, RootInput, RootUpdateInput, ActorService>
        implements QueryDataFetcherProvider, MutationDataFetcherProvider {

    public ActorDataFetcherProvider(ActorService entityService) {
        super(RootInput.class, RootUpdateInput.class, entityService);
    }

    @Override
    public Map<String, DataFetcher> getQueryDataFetchers() {
        return Map.ofEntries(
                Map.entry("actors", getAll())
        );
    }

    @Override
    public Map<String, DataFetcher> getMutationDataFetchers() {
        return Map.ofEntries(
                Map.entry("createActor", add()),
                Map.entry("updateActor", update()),
                Map.entry("deleteActor", remove())
        );
    }
}
