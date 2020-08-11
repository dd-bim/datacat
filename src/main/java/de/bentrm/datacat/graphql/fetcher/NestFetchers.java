package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.domain.collection.XtdNest;
import de.bentrm.datacat.graphql.dto.RootInput;
import de.bentrm.datacat.graphql.dto.RootUpdateInput;
import de.bentrm.datacat.service.NestService;
import org.springframework.stereotype.Component;

@Component
public class NestFetchers extends AbstractEntityFetchers<XtdNest, RootInput, RootUpdateInput, NestService> {

    public NestFetchers(NestService entityService) {
        super(RootInput.class, RootUpdateInput.class, entityService);
    }

    @Override
    public String getTypeName() {
        return "XtdNest";
    }

    @Override
    public String getQueryName() {
        return "nests";
    }

    @Override
    public String getMutationNameSuffix() {
        return "Nest";
    }
}
