package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.XtdClassification;
import de.bentrm.datacat.catalog.repository.ClassificationRepository;
import de.bentrm.datacat.catalog.service.ClassificationService;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional(readOnly = true)
public class ClassificationServiceImpl extends AbstractServiceImpl<XtdClassification> implements ClassificationService {

    public ClassificationServiceImpl(SessionFactory sessionFactory, ClassificationRepository repository) {
        super(XtdClassification.class, sessionFactory, repository);
    }
}
