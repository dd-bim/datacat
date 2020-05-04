package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.domain.XtdValue;
import de.bentrm.datacat.graphql.dto.ValueInput;
import de.bentrm.datacat.graphql.dto.ValueUpdateInput;
import de.bentrm.datacat.service.ValueService;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ValueDataFetcherProvider
        extends EntityDataFetcherProviderImpl<XtdValue, ValueInput, ValueUpdateInput, ValueService>
        implements QueryDataFetcherProvider, MutationDataFetcherProvider {


    public ValueDataFetcherProvider(ValueService entityService) {
        super(ValueInput.class, ValueUpdateInput.class, entityService);
    }

    @Override
    public Map<String, DataFetcher> getQueryDataFetchers() {
        return Map.ofEntries(
                Map.entry("values", getAll())
        );
    }

    @Override
    public Map<String, DataFetcher> getMutationDataFetchers() {
        return Map.ofEntries(
                Map.entry("createValue", add()),
                Map.entry("updateValue", update()),
                Map.entry("deleteValue", remove())
        );
    }
}
