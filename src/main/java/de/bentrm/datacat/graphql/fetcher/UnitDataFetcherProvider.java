package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.domain.XtdUnit;
import de.bentrm.datacat.service.UnitService;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UnitDataFetcherProvider
        extends EntityDataFetcherProviderImpl<XtdUnit, UnitService>
        implements EntityDataFetcherProvider<XtdUnit> {

    public UnitDataFetcherProvider(UnitService entityService) {
        super(entityService);
    }

    @Override
    public Map<String, DataFetcher> getQueryDataFetchers() {
        return Map.ofEntries(
                Map.entry("unit", getOne()),
                Map.entry("units", getAll())
        );
    }

    @Override
    public Map<String, DataFetcher> getMutationDataFetchers() {
        return Map.ofEntries(
                Map.entry("createUnit", add()),
                Map.entry("updateUnit", update()),
                Map.entry("deleteUnit", remove())
        );
    }
}
