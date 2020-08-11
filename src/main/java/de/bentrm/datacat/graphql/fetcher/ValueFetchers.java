package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.domain.XtdValue;
import de.bentrm.datacat.graphql.dto.ValueInput;
import de.bentrm.datacat.graphql.dto.ValueUpdateInput;
import de.bentrm.datacat.service.ValueService;
import org.springframework.stereotype.Component;

@Component
public class ValueFetchers
        extends AbstractEntityFetchers<XtdValue, ValueInput, ValueUpdateInput, ValueService> {


    public ValueFetchers(ValueService entityService) {
        super(ValueInput.class, ValueUpdateInput.class, entityService);
    }

    @Override
    public String getTypeName() {
        return "XtdValue";
    }

    @Override
    public String getQueryName() {
        return "values";
    }

    @Override
    public String getMutationNameSuffix() {
        return "Value";
    }
}
