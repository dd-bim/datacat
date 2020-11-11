package de.bentrm.datacat.graphql.fetchers;

import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.service.AssignsPropertiesService;
import de.bentrm.datacat.catalog.service.PropertyService;
import de.bentrm.datacat.graphql.fetcher.AssignsPropertiesFetcher;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PropertyFetchers extends AbstractObjectFetchers<XtdProperty, PropertyService> {

    private final AssignsPropertiesFetcher assignsPropertiesFetcher;

    public PropertyFetchers(PropertyService entityService, AssignsPropertiesService assignsPropertiesService) {
        super(entityService);
        this.assignsPropertiesFetcher = new AssignsPropertiesFetcher(assignsPropertiesService);
    }

    @Override
    public String getTypeName() {
        return "XtdProperty";
    }

    @Override
    public String getFetcherName() {
        return "property";
    }

    @Override
    public String getListFetcherName() {
        return "properties";
    }

    @Override
    public String getMutationNameSuffix() {
        return "Property";
    }

    @Override
    public Map<String, DataFetcher> getAttributeFetchers() {
        final Map<String, DataFetcher> fetchers = super.getAttributeFetchers();
        fetchers.put("assignedTo", assignsPropertiesFetcher.assignedTo());
        return fetchers;
    }
}
