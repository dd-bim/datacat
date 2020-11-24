package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdRelAssignsPropertyWithValues;
import de.bentrm.datacat.catalog.service.AssignsPropertyWithValuesService;
import org.springframework.stereotype.Component;

@Component
public class AssignsPropertyWithValuesFetchers extends AbstractFetchers<XtdRelAssignsPropertyWithValues> {

    public AssignsPropertyWithValuesFetchers(AssignsPropertyWithValuesService entityService) {
        super(entityService);
    }

    @Override
    public String getTypeName() {
        return "XtdRelAssignsPropertyWithValues";
    }

    @Override
    public String getFetcherName() {
        return "getAssignsPropertyWithValues";
    }

    @Override
    public String getListFetcherName() {
        return "findAssignsPropertyWithValues";
    }
}
