package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.CatalogRecord;
import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdRelAssignsProperties;
import de.bentrm.datacat.catalog.service.AssignsPropertiesRecordService;
import de.bentrm.datacat.catalog.service.CatalogService;
import de.bentrm.datacat.catalog.service.PropertyRecordService;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AssignsPropertiesFetchers extends AbstractFetchers<XtdRelAssignsProperties> {

    private final DataFetcher<XtdObject> relatingObject;
    private final DataFetcher<List<XtdProperty>> relatedProperties;

    public AssignsPropertiesFetchers(AssignsPropertiesRecordService queryService,
                                     CatalogService catalogService,
                                     PropertyRecordService propertyService) {
        super(queryService);

        this.relatingObject = environment -> {
            final XtdRelAssignsProperties source = environment.getSource();
            final String id = source.getRelatingObject().getId();
            return catalogService.getObject(id).orElseThrow();
        };

        this.relatedProperties = environment -> {
            final XtdRelAssignsProperties source = environment.getSource();
            final List<String> relatedPropertiesIds = source.getRelatedProperties().stream()
                    .map(CatalogRecord::getId)
                    .collect(Collectors.toList());
            return propertyService.findAllByIds(relatedPropertiesIds);
        };
    }

    @Override
    public String getFetcherName() {
        return "getAssignsProperties";
    }

    @Override
    public String getListFetcherName() {
        return "findAssignsProperties";
    }

    @Override
    public String getTypeName() {
        return "XtdRelAssignsProperties";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        final Map<String, DataFetcher> fetchers = new HashMap<>(super.getAttributeFetchers());
        fetchers.put("relatingObject", relatingObject);
        fetchers.put("relatedProperties", relatedProperties);
        return fetchers;
    }
}
