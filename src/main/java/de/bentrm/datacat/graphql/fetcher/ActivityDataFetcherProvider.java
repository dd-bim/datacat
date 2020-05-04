package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.domain.XtdActivity;
import de.bentrm.datacat.graphql.dto.RootInput;
import de.bentrm.datacat.graphql.dto.RootUpdateInput;
import de.bentrm.datacat.service.ActivityService;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ActivityDataFetcherProvider
        extends EntityDataFetcherProviderImpl<XtdActivity, RootInput, RootUpdateInput, ActivityService>
        implements QueryDataFetcherProvider, MutationDataFetcherProvider {

    public ActivityDataFetcherProvider(ActivityService entityService) {
        super(RootInput.class, RootUpdateInput.class, entityService);
    }

    @Override
    public Map<String, DataFetcher> getQueryDataFetchers() {
        return Map.ofEntries(
                Map.entry("activities", getAll())
        );
    }

    @Override
    public Map<String, DataFetcher> getMutationDataFetchers() {
        return Map.ofEntries(
                Map.entry("createActivity", add()),
                Map.entry("updateActivity", update()),
                Map.entry("deleteActivity", remove())
        );
    }
}
