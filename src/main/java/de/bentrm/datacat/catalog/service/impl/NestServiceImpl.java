package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdNest;
import de.bentrm.datacat.catalog.service.NestService;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional(readOnly = true)
public class NestServiceImpl extends AbstractServiceImpl<XtdNest> implements NestService {

    public NestServiceImpl(SessionFactory sessionFactory, EntityRepository<XtdNest> repository) {
        super(XtdNest.class, sessionFactory, repository);
    }
}
