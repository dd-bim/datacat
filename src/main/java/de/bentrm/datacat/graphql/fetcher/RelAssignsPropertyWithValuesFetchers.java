package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdRelAssignsPropertyWithValues;
import de.bentrm.datacat.catalog.service.AssignsPropertyWithValuesService;
import org.springframework.stereotype.Component;

@Component
public class RelAssignsPropertyWithValuesFetchers extends AbstractFetchers<XtdRelAssignsPropertyWithValues> {

    public RelAssignsPropertyWithValuesFetchers(AssignsPropertyWithValuesService entityService) {
        super(entityService);
    }

    @Override
    public String getTypeName() {
        return "XtdRelAssignsPropertyWithValues";
    }

    @Override
    public String getFetcherName() {
        return "assignsPropertyWithValuesRelation";
    }

    @Override
    public String getListFetcherName() {
        return "assignsPropertyWithValuesRelations";
    }
}
