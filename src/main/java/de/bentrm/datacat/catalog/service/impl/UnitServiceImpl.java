package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.XtdUnit;
import de.bentrm.datacat.catalog.repository.UnitRepository;
import de.bentrm.datacat.catalog.service.UnitService;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional(readOnly = true)
public class UnitServiceImpl extends AbstractServiceImpl<XtdUnit> implements UnitService {

    public UnitServiceImpl(SessionFactory sessionFactory, UnitRepository repository) {
        super(XtdUnit.class, sessionFactory, repository);
    }
}
