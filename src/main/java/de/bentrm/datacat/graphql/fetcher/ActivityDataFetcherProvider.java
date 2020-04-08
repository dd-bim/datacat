package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.domain.XtdActivity;
import de.bentrm.datacat.service.ActivityService;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ActivityDataFetcherProvider
        extends EntityDataFetcherProviderImpl<XtdActivity, ActivityService>
        implements EntityDataFetcherProvider<XtdActivity> {

    public ActivityDataFetcherProvider(ActivityService entityService) {
        super(entityService);
    }

    @Override
    public Map<String, DataFetcher> getQueryDataFetchers() {
        return Map.ofEntries(
                Map.entry("activity", getOne()),
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
