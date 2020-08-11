package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.domain.relationship.XtdRelAssignsPropertyWithValues;
import de.bentrm.datacat.graphql.dto.AssignsPropertyWithValuesInput;
import de.bentrm.datacat.graphql.dto.AssignsPropertyWithValuesUpdateInput;
import de.bentrm.datacat.service.RelAssignsPropertyWithValuesService;
import org.springframework.stereotype.Component;

@Component
public class RelAssignsPropertyWithValuesFetchers extends AbstractEntityFetchers<XtdRelAssignsPropertyWithValues, AssignsPropertyWithValuesInput, AssignsPropertyWithValuesUpdateInput, RelAssignsPropertyWithValuesService> {

    public RelAssignsPropertyWithValuesFetchers(RelAssignsPropertyWithValuesService entityService) {
        super(AssignsPropertyWithValuesInput.class, AssignsPropertyWithValuesUpdateInput.class, entityService);
    }

    @Override
    public String getTypeName() {
        return "XtdRelAssignsPropertyWithValues";
    }

    @Override
    public String getQueryName() {
        return "assignsPropertyWithValues";
    }

    @Override
    public String getMutationNameSuffix() {
        return "AssignsPropertyWithValues";
    }
}
