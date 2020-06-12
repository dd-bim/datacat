package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.domain.DomainValueRelationship;
import de.bentrm.datacat.domain.XtdMeasureWithUnit;
import de.bentrm.datacat.domain.XtdValue;
import de.bentrm.datacat.graphql.dto.MeasureInput;
import de.bentrm.datacat.graphql.dto.MeasureUpdateInput;
import de.bentrm.datacat.service.MeasureWithUnitService;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MeasureWithUnitDataFetcherProvider
        extends EntityDataFetcherProviderImpl<XtdMeasureWithUnit, MeasureInput, MeasureUpdateInput, MeasureWithUnitService>
        implements QueryDataFetcherProvider, MutationDataFetcherProvider {

    public MeasureWithUnitDataFetcherProvider(MeasureWithUnitService entityService) {
        super(MeasureInput.class, MeasureUpdateInput.class, entityService);
    }

    public Map<String, DataFetcher> getPropertyDataFetchers() {
        return Map.ofEntries(
                Map.entry("valueDomain", valueDomain())
        );
    }

    @Override
    public Map<String, DataFetcher> getQueryDataFetchers() {
        return Map.ofEntries(
                Map.entry("measures", getAll())
        );
    }

    @Override
    public Map<String, DataFetcher> getMutationDataFetchers() {
        return Map.ofEntries(
                Map.entry("createMeasure", add()),
                Map.entry("updateMeasure", update()),
                Map.entry("deleteMeasure", remove())
        );
    }

    public DataFetcher<List<XtdValue>> valueDomain() {
        return environment -> {
            XtdMeasureWithUnit source = environment.getSource();
            return source.getValueDomain()
                    .stream()
                    .map(DomainValueRelationship::getValue)
                    .collect(Collectors.toList());
        };
    }
}
