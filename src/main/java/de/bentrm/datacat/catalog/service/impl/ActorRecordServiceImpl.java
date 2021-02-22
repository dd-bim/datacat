package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.XtdActor;
import de.bentrm.datacat.catalog.repository.ActorRepository;
import de.bentrm.datacat.catalog.service.ActorRecordService;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Service
@Validated
@Transactional(readOnly = true)
public class ActorRecordServiceImpl extends AbstractSimpleRecordServiceImpl<XtdActor, ActorRepository> implements ActorRecordService {

    public ActorRecordServiceImpl(SessionFactory sessionFactory,
                                  ActorRepository repository,
                                  CatalogCleanupService cleanupService) {
        super(XtdActor.class, sessionFactory, repository, cleanupService);
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.Actor;
    }
}
