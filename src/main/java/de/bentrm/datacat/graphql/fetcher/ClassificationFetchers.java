package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.domain.XtdClassification;
import de.bentrm.datacat.graphql.dto.RootInput;
import de.bentrm.datacat.graphql.dto.RootUpdateInput;
import de.bentrm.datacat.service.ClassificationService;
import org.springframework.stereotype.Component;

@Component
public class ClassificationFetchers extends AbstractEntityFetchers<XtdClassification, RootInput, RootUpdateInput, ClassificationService> {

    public ClassificationFetchers(ClassificationService entityService) {
        super(RootInput.class, RootUpdateInput.class, entityService);
    }

    @Override
    public String getTypeName() {
        return "XtdClassification";
    }

    @Override
    public String getQueryName() {
        return "classifications";
    }

    @Override
    public String getMutationNameSuffix() {
        return "Classification";
    }
}
