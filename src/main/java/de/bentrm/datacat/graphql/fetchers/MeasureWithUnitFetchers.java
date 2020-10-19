package de.bentrm.datacat.graphql.fetchers;

import de.bentrm.datacat.catalog.domain.DomainValueRelationship;
import de.bentrm.datacat.catalog.domain.XtdMeasureWithUnit;
import de.bentrm.datacat.catalog.domain.XtdValue;
import de.bentrm.datacat.catalog.service.NewMeasureService;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MeasureWithUnitFetchers extends AbstractEntityFetchers<XtdMeasureWithUnit, NewMeasureService> {

    public MeasureWithUnitFetchers(NewMeasureService entityService) {
        super(entityService);
    }

    @Override
    public String getTypeName() {
        return "XtdMeasureWithUnit";
    }

    @Override
    public String getFetcherName() {
        return "measure";
    }

    @Override
    public String getListFetcherName() {
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
