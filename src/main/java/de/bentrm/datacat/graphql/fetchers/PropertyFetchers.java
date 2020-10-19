package de.bentrm.datacat.graphql.fetchers;

import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.service.NewPropertyService;
import org.springframework.stereotype.Component;

@Component
public class PropertyFetchers extends ObjectFetchers<XtdProperty, NewPropertyService> {

    public PropertyFetchers(NewPropertyService entityService) {
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
