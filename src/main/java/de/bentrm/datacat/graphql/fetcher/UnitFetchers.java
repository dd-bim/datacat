package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.domain.XtdUnit;
import de.bentrm.datacat.graphql.dto.RootInput;
import de.bentrm.datacat.graphql.dto.RootUpdateInput;
import de.bentrm.datacat.service.UnitService;
import org.springframework.stereotype.Component;

@Component
public class UnitFetchers
        extends AbstractEntityFetchers<XtdUnit, RootInput, RootUpdateInput, UnitService> {

    public UnitFetchers(UnitService entityService) {
        super(RootInput.class, RootUpdateInput.class, entityService);
    }

    @Override
    public String getTypeName() {
        return "XtdUnit";
    }

    @Override
    public String getQueryName() {
        return "units";
    }

    @Override
    public String getMutationNameSuffix() {
        return "Unit";
    }
}
