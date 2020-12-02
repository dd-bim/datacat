package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.XtdMeasureWithUnit;
import de.bentrm.datacat.catalog.repository.MeasureRepository;
import de.bentrm.datacat.catalog.service.MeasureService;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional(readOnly = true)
public class MeasureServiceImpl extends AbstractServiceImpl<XtdMeasureWithUnit> implements MeasureService {

    public MeasureServiceImpl(SessionFactory sessionFactory, MeasureRepository repository) {
        super(XtdMeasureWithUnit.class, sessionFactory, repository);
    }
}
