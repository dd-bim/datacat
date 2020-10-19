package de.bentrm.datacat.graphql.fetchers;

import de.bentrm.datacat.catalog.domain.XtdUnit;
import de.bentrm.datacat.catalog.service.NewUnitService;
import org.springframework.stereotype.Component;

@Component
public class UnitFetchers extends AbstractEntityFetchers<XtdUnit, NewUnitService> {

    public UnitFetchers(NewUnitService entityService) {
        super(entityService);
    }

    @Override
    public String getTypeName() {
        return "XtdUnit";
    }

    @Override
    public String getFetcherName() {
        return "unit";
    }

    @Override
    public String getListFetcherName() {
        return "units";
    }

    @Override
    public String getMutationNameSuffix() {
        return "Unit";
    }
}
