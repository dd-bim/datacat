package de.bentrm.datacat.graphql.fetchers;

import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.service.PropertyService;
import org.springframework.stereotype.Component;

@Component
public class PropertyFetchers extends ObjectFetchers<XtdProperty, PropertyService> {

    public PropertyFetchers(PropertyService entityService) {
        super(entityService);
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
}
