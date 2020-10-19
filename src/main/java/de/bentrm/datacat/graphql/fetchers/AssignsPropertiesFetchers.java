package de.bentrm.datacat.graphql.fetchers;

import de.bentrm.datacat.catalog.domain.CatalogItem;
import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdRelAssignsProperties;
import de.bentrm.datacat.catalog.service.CatalogService;
import de.bentrm.datacat.catalog.service.NewPropertyService;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AssignsPropertiesFetchers extends EntryFetchers implements AttributeFetchers {

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private NewPropertyService propertyService;

    @Autowired

    @Override
    public String getTypeName() {
        return "XtdRelAssignsProperties";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        final Map<String, DataFetcher> fetchers = super.getAttributeFetchers();
        fetchers.put("relatingObject", relatingObject());
        fetchers.put("relatedProperties", relatedProperties());
        return fetchers;
    }

    private DataFetcher<XtdObject> relatingObject() {
        return environment -> {
            final XtdRelAssignsProperties source = environment.getSource();
            final String id = source.getRelatingObject().getId();
            return catalogService.getObject(id).orElseThrow();
        };
    }

    public DataFetcher<List<XtdProperty>> relatedProperties() {
        return environment -> {
            final XtdRelAssignsProperties source = environment.getSource();
            final List<String> relatedPropertiesIds = source.getRelatedProperties().stream()
                    .map(CatalogItem::getId)
                    .collect(Collectors.toList());
            return propertyService.findAllByIds(relatedPropertiesIds);
        };
    }
}
