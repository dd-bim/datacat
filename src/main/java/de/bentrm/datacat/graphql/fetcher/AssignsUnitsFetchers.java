package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.CatalogRecord;
import de.bentrm.datacat.catalog.domain.Measure;
import de.bentrm.datacat.catalog.domain.XtdRelAssignsUnits;
import de.bentrm.datacat.catalog.domain.XtdUnit;
import de.bentrm.datacat.catalog.service.AssignsUnitsRecordService;
import de.bentrm.datacat.catalog.service.MeasureRecordService;
import de.bentrm.datacat.catalog.service.UnitRecordService;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AssignsUnitsFetchers extends AbstractFetchers<XtdRelAssignsUnits> {

    private final DataFetcher<Measure> relatingMeasure;
    private final DataFetcher<List<XtdUnit>> relatedUnits;

    public AssignsUnitsFetchers(AssignsUnitsRecordService entityService,
                                MeasureRecordService measureService,
                                UnitRecordService unitService) {
        super(entityService);

        this.relatingMeasure = environment -> {
            final XtdRelAssignsUnits source = environment.getSource();
            final String id = source.getRelatingMeasure().getId();
            return measureService.findById(id).orElseThrow();
        };

        this.relatedUnits = environment -> {
            final XtdRelAssignsUnits source = environment.getSource();
            final List<String> relatedValuesId = source.getRelatedUnits().stream()
                    .map(CatalogRecord::getId)
                    .collect(Collectors.toList());
            return unitService.findAllByIds(relatedValuesId);
        };
    }

    @Autowired

    @Override
    public String getTypeName() {
        return "XtdRelAssignsUnits";
    }

    @Override
    public String getFetcherName() {
        return "getAssignsUnits";
    }

    @Override
    public String getListFetcherName() {
        return "findAssignsUnits";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        final Map<String, DataFetcher> fetchers = new HashMap<>(super.getAttributeFetchers());
        fetchers.put("relatingMeasure", relatingMeasure);
        fetchers.put("relatedUnits", relatedUnits);
        return fetchers;
    }
}
