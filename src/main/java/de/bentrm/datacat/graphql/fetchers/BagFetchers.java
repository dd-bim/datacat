package de.bentrm.datacat.graphql.fetchers;

import de.bentrm.datacat.catalog.domain.XtdBag;
import de.bentrm.datacat.catalog.service.BagService;
import org.springframework.stereotype.Component;

@Component
public class BagFetchers extends AbstractCollectionFetchers<XtdBag, BagService> {

    public BagFetchers(BagService entityService) {
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
