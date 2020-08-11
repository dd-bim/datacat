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
public class MeasureWithUnitFetchers extends AbstractEntityFetchers<XtdMeasureWithUnit, MeasureInput, MeasureUpdateInput, MeasureWithUnitService> {

    public MeasureWithUnitFetchers(MeasureWithUnitService entityService) {
        super(MeasureInput.class, MeasureUpdateInput.class, entityService);
    }

    @Override
    public String getTypeName() {
        return "XtdMeasureWithUnit";
    }

    @Override
    public String getQueryName() {
        return "measures";
    }

    @Override
    public String getMutationNameSuffix() {
        return "Measure";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        final Map<String, DataFetcher> fetchers = super.getAttributeFetchers();
        fetchers.put("valueDomain", valueDomain());
        return fetchers;
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
