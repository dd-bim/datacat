package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.domain.XtdUnit;
import de.bentrm.datacat.graphql.dto.RootInput;
import de.bentrm.datacat.graphql.dto.RootUpdateInput;
import de.bentrm.datacat.service.UnitService;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UnitDataFetcherProvider
        extends EntityDataFetcherProviderImpl<XtdUnit, RootInput, RootUpdateInput, UnitService>
        implements EntityDataFetcherProvider<XtdUnit> {

    public UnitDataFetcherProvider(UnitService entityService) {
        super(RootInput.class, RootUpdateInput.class, entityService);
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
