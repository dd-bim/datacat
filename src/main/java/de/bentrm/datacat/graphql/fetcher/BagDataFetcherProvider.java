package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.domain.collection.XtdBag;
import de.bentrm.datacat.graphql.dto.RootInput;
import de.bentrm.datacat.graphql.dto.RootUpdateInput;
import de.bentrm.datacat.service.BagService;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BagDataFetcherProvider
        extends EntityDataFetcherProviderImpl<XtdBag, RootInput, RootUpdateInput, BagService>
        implements QueryDataFetcherProvider, MutationDataFetcherProvider {

    public BagDataFetcherProvider(BagService entityService) {
        super(RootInput.class, RootUpdateInput.class, entityService);
    }

    @Override
    public Map<String, DataFetcher> getQueryDataFetchers() {
        return Map.ofEntries(
                Map.entry("bags", getAll())
        );
    }

    @Override
    public Map<String, DataFetcher> getMutationDataFetchers() {
        return Map.ofEntries(
                Map.entry("createBag", add()),
                Map.entry("updateBag", update()),
                Map.entry("deleteBag", remove())
        );
    }
}
