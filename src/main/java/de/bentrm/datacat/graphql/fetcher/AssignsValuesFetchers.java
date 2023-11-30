package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.CatalogRecord;
import de.bentrm.datacat.catalog.domain.Measure;
import de.bentrm.datacat.catalog.domain.XtdRelAssignsValues;
import de.bentrm.datacat.catalog.domain.XtdValue;
import de.bentrm.datacat.catalog.service.AssignsValuesRecordService;
import de.bentrm.datacat.catalog.service.MeasureRecordService;
import de.bentrm.datacat.catalog.service.ValueRecordService;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AssignsValuesFetchers extends AbstractFetchers<XtdRelAssignsValues> {

    private final DataFetcher<Measure> relatingMeasure;
    private final DataFetcher<List<XtdValue>> relatedValues;

    public AssignsValuesFetchers(AssignsValuesRecordService entityService,
                                 MeasureRecordService measureService,
                                 ValueRecordService valueService) {
        super(entityService);

        this.relatingMeasure = environment -> {
            final XtdRelAssignsValues source = environment.getSource();
            final String id = source.getRelatingMeasure().getId();
            return measureService.findById(id).orElseThrow();
        };

        this.relatedValues = environment -> {
            final XtdRelAssignsValues source = environment.getSource();
            final List<String> relatedValuesId = source.getRelatedValues().stream()
                    .map(CatalogRecord::getId)
                    .collect(Collectors.toList());
            return valueService.findAllByIds(relatedValuesId);
        };
    }

    @Autowired

    @Override
    public String getTypeName() {
        return "XtdRelAssignsValues";
    }

    @Override
    public String getFetcherName() {
        return "getAssignsValues";
    }

    @Override
    public String getListFetcherName() {
        return "findAssignsValues";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        final Map<String, DataFetcher> fetchers = new HashMap<>(super.getAttributeFetchers());
        fetchers.put("relatingMeasure", relatingMeasure);
        fetchers.put("relatedValues", relatedValues);
        return fetchers;
    }
}
