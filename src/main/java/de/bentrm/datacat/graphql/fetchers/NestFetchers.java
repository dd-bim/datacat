package de.bentrm.datacat.graphql.fetchers;

import de.bentrm.datacat.catalog.domain.XtdNest;
import de.bentrm.datacat.catalog.service.NewNestService;
import org.springframework.stereotype.Component;

@Component
public class NestFetchers extends CollectionFetchers<XtdNest, NewNestService> {

    public NestFetchers(NewNestService service) {
        super(service);
    }

    @Override
    public String getTypeName() {
        return "XtdNest";
    }

    @Override
    public String getFetcherName() {
        return "nest";
    }

    @Override
    public String getListFetcherName() {
        return "nests";
    }

    @Override
    public String getMutationNameSuffix() {
        return "Nest";
    }
}
