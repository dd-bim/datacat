package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.*;
import de.bentrm.datacat.catalog.service.AssignsPropertyWithValuesService;
import de.bentrm.datacat.catalog.service.CatalogService;
import de.bentrm.datacat.catalog.service.PropertyRecordService;
import de.bentrm.datacat.catalog.service.ValueRecordService;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AssignsPropertyWithValuesFetchers extends AbstractFetchers<XtdRelAssignsPropertyWithValues> {

    private final DataFetcher<XtdObject> relatingObject;
    private final DataFetcher<XtdProperty> relatedProperty;
    private final DataFetcher<List<XtdValue>> relatedValues;

    public AssignsPropertyWithValuesFetchers(AssignsPropertyWithValuesService entityService,
                                             CatalogService catalogService,
                                             PropertyRecordService propertyService,
                                             ValueRecordService valueService) {
        super(entityService);

        this.relatingObject = environment -> {
            final XtdRelAssignsPropertyWithValues source = environment.getSource();
            final String id = source.getRelatingObject().getId();
            return catalogService.getObject(id).orElseThrow();
        };

        this.relatedProperty = environment -> {
            final XtdRelAssignsPropertyWithValues source = environment.getSource();
            final String id = source.getRelatedProperty().getId();
            return propertyService.findById(id).orElseThrow();
        };

        this.relatedValues = environment -> {
            final XtdRelAssignsPropertyWithValues source = environment.getSource();
            final List<String> relatedValuesId = source.getRelatedValues().stream()
                    .map(CatalogItem::getId)
                    .collect(Collectors.toList());
            return valueService.findAllByIds(relatedValuesId);
        };
    }

    @Override
    public String getTypeName() {
        return "XtdRelAssignsPropertyWithValues";
    }

    @Override
    public String getFetcherName() {
        return "getAssignsPropertyWithValues";
    }

    @Override
    public String getListFetcherName() {
        return "findAssignsPropertyWithValues";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        final Map<String, DataFetcher> fetchers = new HashMap<>(super.getAttributeFetchers());
        fetchers.put("relatingObject", relatingObject);
        fetchers.put("relatedProperty", relatedProperty);
        fetchers.put("relatedValues", relatedValues);
        return fetchers;
    }
}
