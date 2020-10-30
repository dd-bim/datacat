package de.bentrm.datacat.graphql.fetchers;

import de.bentrm.datacat.catalog.domain.XtdClassification;
import de.bentrm.datacat.catalog.service.ClassificationService;
import org.springframework.stereotype.Component;

@Component
public class ClassificationFetchers extends ObjectFetchers<XtdClassification, ClassificationService> {

    public ClassificationFetchers(ClassificationService entityService) {
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
