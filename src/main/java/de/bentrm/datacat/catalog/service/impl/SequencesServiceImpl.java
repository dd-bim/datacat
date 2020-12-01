package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdActivity;
import de.bentrm.datacat.catalog.domain.XtdRelSequences;
import de.bentrm.datacat.catalog.service.EntityMapper;
import de.bentrm.datacat.catalog.service.SequencesService;
import de.bentrm.datacat.catalog.service.value.OneToOneRelationshipValue;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Service
@Validated
@Transactional(readOnly = true)
public class SequencesServiceImpl extends AbstractServiceImpl<XtdRelSequences> implements SequencesService {

    private final EntityMapper entityMapper = EntityMapper.INSTANCE;
    private final EntityRepository<XtdActivity> activityRepository;

    public SequencesServiceImpl(SessionFactory sessionFactory, EntityRepository<XtdRelSequences> repository, EntityRepository<XtdActivity> activityRepository) {
        super(XtdRelSequences.class, sessionFactory, repository);
        this.activityRepository = activityRepository;
    }

    @Transactional
    @Override
    public @NotNull XtdRelSequences create(OneToOneRelationshipValue value) {
        final XtdRelSequences ref = new XtdRelSequences();

        entityMapper.setProperties(value, ref);

        final XtdActivity relating = activityRepository.findById(value.getFrom()).orElseThrow();
        ref.setRelatingActivity(relating);

        final XtdActivity related = activityRepository.findById(value.getFrom()).orElseThrow();
        ref.setRelatedActivity(related);

        return getRepository().save(ref);
    }
}
