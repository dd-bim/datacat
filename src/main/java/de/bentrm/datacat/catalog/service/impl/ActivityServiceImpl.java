package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdActivity;
import de.bentrm.datacat.catalog.service.ActivityService;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional(readOnly = true)
public class ActivityServiceImpl extends AbstractServiceImpl<XtdActivity> implements ActivityService {

    public ActivityServiceImpl(SessionFactory sessionFactory, EntityRepository<XtdActivity> repository) {
        super(XtdActivity.class, sessionFactory, repository);
    }
}
