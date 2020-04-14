package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.domain.XtdMeasureWithUnit;
import de.bentrm.datacat.graphql.dto.RootInput;
import de.bentrm.datacat.graphql.dto.RootUpdateInput;
import de.bentrm.datacat.service.MeasureWithUnitService;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MeasureWithUnitDataFetcherProvider
		extends EntityDataFetcherProviderImpl<XtdMeasureWithUnit, RootInput, RootUpdateInput, MeasureWithUnitService>
		implements EntityDataFetcherProvider<XtdMeasureWithUnit> {

	public MeasureWithUnitDataFetcherProvider(MeasureWithUnitService entityService) {
		super(RootInput.class, RootUpdateInput.class, entityService);
	}

	@Override
    public Map<String, DataFetcher> getQueryDataFetchers() {
        return Map.ofEntries(
                Map.entry("measure", getOne()),
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
}
