package de.bentrm.datacat.graphql.fetchers;

import de.bentrm.datacat.catalog.domain.XtdClassification;
import de.bentrm.datacat.catalog.service.NewClassificationService;
import org.springframework.stereotype.Component;

@Component
public class ClassificationFetchers extends AbstractEntityFetchers<XtdClassification, NewClassificationService> {

    public ClassificationFetchers(NewClassificationService entityService) {
        super(entityService);
    }

    @Override
    public String getTypeName() {
        return "XtdClassification";
    }

    @Override
    public String getFetcherName() {
        return "classification";
    }

    @Override
    public String getListFetcherName() {
        return "classifications";
    }

    @Override
    public String getMutationNameSuffix() {
        return "Classification";
    }
}
