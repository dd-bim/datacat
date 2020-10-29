package de.bentrm.datacat.graphql.fetchers;

import de.bentrm.datacat.catalog.domain.XtdActivity;
import de.bentrm.datacat.catalog.service.NewActivityService;
import org.springframework.stereotype.Component;

@Component
public class ActivityFetchers extends ObjectFetchers<XtdActivity, NewActivityService> {

    public ActivityFetchers(NewActivityService service) {
        super(service);
    }

    @Override
    public String getTypeName() {
        return "XtdActivity";
    }

    @Override
    public String getFetcherName() {
        return "activity";
    }

    @Override
    public String getListFetcherName() {
        return "activities";
    }

    @Override
    public String getMutationNameSuffix() {
        return "Activity";
    }
}
