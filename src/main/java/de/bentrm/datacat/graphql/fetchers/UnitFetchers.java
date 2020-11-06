package de.bentrm.datacat.graphql.fetchers;

import de.bentrm.datacat.catalog.domain.XtdUnit;
import de.bentrm.datacat.catalog.service.UnitService;
import org.springframework.stereotype.Component;

@Component
public class UnitFetchers extends AbstractObjectFetchers<XtdUnit, UnitService> {

    public UnitFetchers(UnitService entityService) {
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
