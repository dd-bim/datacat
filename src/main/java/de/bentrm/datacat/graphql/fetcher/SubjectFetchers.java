package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.domain.XtdSubject;
import de.bentrm.datacat.graphql.dto.RootInput;
import de.bentrm.datacat.graphql.dto.RootUpdateInput;
import de.bentrm.datacat.service.SubjectService;
import org.springframework.stereotype.Component;

@Component
public class SubjectFetchers
        extends AbstractEntityFetchers<XtdSubject, RootInput, RootUpdateInput, SubjectService> {

    public SubjectFetchers(SubjectService entityService) {
        super(RootInput.class, RootUpdateInput.class, entityService);
    }

    @Override
    public String getTypeName() {
        return "XtdSubject";
    }

    @Override
    public String getQueryName() {
        return "subjects";
    }

    @Override
    public String getMutationNameSuffix() {
        return "Subject";
    }
}
