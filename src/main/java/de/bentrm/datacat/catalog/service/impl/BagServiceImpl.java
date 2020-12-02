package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.XtdBag;
import de.bentrm.datacat.catalog.repository.BagRepository;
import de.bentrm.datacat.catalog.service.BagService;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional(readOnly = true)
public class BagServiceImpl extends AbstractServiceImpl<XtdBag> implements BagService {

    public BagServiceImpl(SessionFactory sessionFactory, BagRepository repository) {
        super(XtdBag.class, sessionFactory, repository);
    }
}
