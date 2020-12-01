package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdActor;
import de.bentrm.datacat.catalog.service.ActorService;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional(readOnly = true)
public class ActorServiceImpl extends AbstractServiceImpl<XtdActor> implements ActorService {

    public ActorServiceImpl(SessionFactory sessionFactory, EntityRepository<XtdActor> repository) {
        super(XtdActor.class, sessionFactory, repository);
    }
}
