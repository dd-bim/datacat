package de.bentrm.datacat.graphql.fetchers;

import de.bentrm.datacat.catalog.domain.XtdSubject;
import de.bentrm.datacat.catalog.service.NewSubjectService;
import org.springframework.stereotype.Component;

@Component
public class SubjectFetchers extends ObjectFetchers<XtdSubject, NewSubjectService> {

    public SubjectFetchers(NewSubjectService entityService) {
        super(entityService);
    }

    @Override
    public String getTypeName() {
        return "XtdSubject";
    }

    @Override
    public String getFetcherName() {
        return "subject";
    }

    @Override
    public String getListFetcherName() {
        return "subjects";
    }

    @Override
    public String getMutationNameSuffix() {
        return "Subject";
    }
}
