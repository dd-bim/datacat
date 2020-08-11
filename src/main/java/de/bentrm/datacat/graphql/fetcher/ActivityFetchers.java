package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.domain.XtdActivity;
import de.bentrm.datacat.graphql.dto.RootInput;
import de.bentrm.datacat.graphql.dto.RootUpdateInput;
import de.bentrm.datacat.service.ActivityService;
import org.springframework.stereotype.Component;

@Component
public class ActivityFetchers extends AbstractEntityFetchers<XtdActivity, RootInput, RootUpdateInput, ActivityService> {

    public ActivityFetchers(ActivityService entityService) {
        super(RootInput.class, RootUpdateInput.class, entityService);
    }

    @Override
    public String getTypeName() {
        return "XtdActivity";
    }

    @Override
    public String getQueryName() {
        return "activities";
    }

    @Override
    public String getMutationNameSuffix() {
        return "activity";
    }
}
