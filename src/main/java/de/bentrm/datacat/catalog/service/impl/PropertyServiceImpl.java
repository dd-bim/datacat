package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.repository.PropertyRepository;
import de.bentrm.datacat.catalog.service.PropertyService;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional(readOnly = true)
public class PropertyServiceImpl extends AbstractServiceImpl<XtdProperty> implements PropertyService {

    public PropertyServiceImpl(SessionFactory sessionFactory, PropertyRepository repository) {
        super(XtdProperty.class, sessionFactory, repository);
    }
}
