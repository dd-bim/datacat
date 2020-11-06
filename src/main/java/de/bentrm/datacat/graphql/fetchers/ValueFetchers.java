package de.bentrm.datacat.graphql.fetchers;

import de.bentrm.datacat.catalog.domain.XtdValue;
import de.bentrm.datacat.catalog.service.ValueService;
import org.springframework.stereotype.Component;

@Component
public class ValueFetchers extends AbstractObjectFetchers<XtdValue, ValueService> {


    public ValueFetchers(ValueService entityService) {
        super(entityService);
    }

    @Override
    public String getTypeName() {
        return "XtdValue";
    }

    @Override
    public String getFetcherName() {
        return "value";
    }

    @Override
    public String getListFetcherName() {
        return "values";
    }

    @Override
    public String getMutationNameSuffix() {
        return "Value";
    }
}
