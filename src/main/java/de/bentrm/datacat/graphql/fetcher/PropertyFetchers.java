package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.domain.XtdProperty;
import de.bentrm.datacat.graphql.dto.RootInput;
import de.bentrm.datacat.graphql.dto.RootUpdateInput;
import de.bentrm.datacat.service.PropertyService;
import org.springframework.stereotype.Component;

@Component
public class PropertyFetchers extends AbstractEntityFetchers<XtdProperty, RootInput, RootUpdateInput, PropertyService> {

    public PropertyFetchers(PropertyService entityService) {
        super(RootInput.class, RootUpdateInput.class, entityService);
    }

    @Override
    public String getTypeName() {
        return "XtdProperty";
    }

    @Override
    public String getQueryName() {
        return "properties";
    }

    @Override
    public String getMutationNameSuffix() {
        return "Property";
    }
}
