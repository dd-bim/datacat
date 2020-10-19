package de.bentrm.datacat.graphql.fetchers;

import de.bentrm.datacat.catalog.domain.XtdBag;
import de.bentrm.datacat.catalog.service.NewBagService;
import org.springframework.stereotype.Component;

@Component
public class BagFetchers extends CollectionFetchers<XtdBag, NewBagService> {

    public BagFetchers(NewBagService entityService) {
        super(entityService);
    }

    @Override
    public String getTypeName() {
        return "XtdBag";
    }

    @Override
    public String getFetcherName() {
        return "bag";
    }

    @Override
    public String getListFetcherName() {
        return "bags";
    }

    @Override
    public String getMutationNameSuffix() {
        return "Bag";
    }
}
