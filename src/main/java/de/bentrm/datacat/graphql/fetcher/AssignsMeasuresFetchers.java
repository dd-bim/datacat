package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.CatalogItem;
import de.bentrm.datacat.catalog.domain.XtdMeasureWithUnit;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdRelAssignsMeasures;
import de.bentrm.datacat.catalog.service.AssignsMeasuresRelationshipService;
import de.bentrm.datacat.catalog.service.MeasureService;
import de.bentrm.datacat.catalog.service.PropertyService;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AssignsMeasuresFetchers extends AbstractFetchers<XtdRelAssignsMeasures> {

    private final DataFetcher<XtdProperty> relatingProperty;
    private final DataFetcher<List<XtdMeasureWithUnit>> relatedMeasures;

    public AssignsMeasuresFetchers(AssignsMeasuresRelationshipService queryService,
                                   PropertyService propertyService,
                                   MeasureService measureService) {
        super(queryService);

        this.relatingProperty = environment -> {
            final XtdRelAssignsMeasures source = environment.getSource();
            final String id = source.getRelatingProperty().getId();
            return propertyService.findById(id).orElseThrow();
        };

        this.relatedMeasures = environment -> {
            final XtdRelAssignsMeasures source = environment.getSource();
            final List<String> relatedMeasureIds = source.getRelatedMeasures().stream()
                    .map(CatalogItem::getId)
                    .collect(Collectors.toList());
            return measureService.findAllByIds(relatedMeasureIds);
        };
    }

    @Override
    public String getFetcherName() {
        return "getAssignsMeasures";
    }

    @Override
    public String getListFetcherName() {
        return "findAssignsMeasures";
    }

    @Override
    public String getTypeName() {
        return "XtdRelAssignsMeasures";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        final Map<String, DataFetcher> fetchers = new HashMap<>(super.getAttributeFetchers());
        fetchers.put("relatingProperty", relatingProperty);
        fetchers.put("relatedMeasures", relatedMeasures);
        return fetchers;
    }
}
