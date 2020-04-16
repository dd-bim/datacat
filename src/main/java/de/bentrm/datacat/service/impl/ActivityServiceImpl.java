package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.domain.XtdActivity;
import de.bentrm.datacat.graphql.dto.RootInput;
import de.bentrm.datacat.graphql.dto.RootUpdateInput;
import de.bentrm.datacat.repository.ActivityRepository;
import de.bentrm.datacat.service.ActivityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional(readOnly = true)
public class ActivityServiceImpl
        extends CrudEntityServiceImpl<XtdActivity, RootInput, RootUpdateInput, ActivityRepository>
        implements ActivityService {

    public ActivityServiceImpl(ActivityRepository repository) {
        super(repository);
    }

    @Override
    protected XtdActivity newEntityInstance() {
        return new XtdActivity();
    }
}
