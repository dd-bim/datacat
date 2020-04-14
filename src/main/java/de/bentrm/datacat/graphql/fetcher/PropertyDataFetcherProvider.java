package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.domain.XtdProperty;
import de.bentrm.datacat.graphql.dto.RootInput;
import de.bentrm.datacat.graphql.dto.RootUpdateInput;
import de.bentrm.datacat.service.PropertyService;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PropertyDataFetcherProvider
        extends EntityDataFetcherProviderImpl<XtdProperty, RootInput, RootUpdateInput, PropertyService>
        implements EntityDataFetcherProvider<XtdProperty> {

    public PropertyDataFetcherProvider(PropertyService entityService) {
        super(RootInput.class, RootUpdateInput.class, entityService);
    }

    @Override
    public Map<String, DataFetcher> getQueryDataFetchers() {
        return Map.ofEntries(
                Map.entry("property", getOne()),
                Map.entry("properties", getAll())
        );
    }

    @Override
    public Map<String, DataFetcher> getMutationDataFetchers() {
        return Map.ofEntries(
                Map.entry("createProperty", add()),
                Map.entry("updateProperty", update()),
                Map.entry("deleteProperty", remove())
        );
    }
}
