package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.base.specification.QuerySpecification;
import de.bentrm.datacat.catalog.domain.XtdActivity;
import de.bentrm.datacat.catalog.domain.XtdRelSequences;
import de.bentrm.datacat.catalog.repository.ActivityRepository;
import de.bentrm.datacat.catalog.repository.RelSequencesRepository;
import de.bentrm.datacat.catalog.service.EntityMapper;
import de.bentrm.datacat.catalog.service.SequencesService;
import de.bentrm.datacat.catalog.service.value.OneToOneRelationshipValue;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Service
@Validated
@Transactional(readOnly = true)
public class SequencesServiceImpl implements SequencesService {

    private final EntityMapper entityMapper = EntityMapper.INSTANCE;
    private final RelSequencesRepository sequencesRepository;
    private final ActivityRepository activityRepository;

    private final QueryServiceDelegate<XtdRelSequences> queryDelegate;

    public SequencesServiceImpl(RelSequencesRepository repository, ActivityRepository activityRepository) {
        this.sequencesRepository = repository;
        this.activityRepository = activityRepository;
        this.queryDelegate = new QueryServiceDelegate<>(repository);
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

        return sequencesRepository.save(ref);
    }

    @Override
    public @NotNull Optional<XtdRelSequences> findById(@NotNull String id) {
        return queryDelegate.findById(id);
    }

    @Override
    public @NotNull List<XtdRelSequences> findAllByIds(@NotNull List<String> ids) {
        return queryDelegate.findAllByIds(ids);
    }

    @Override
    public @NotNull Page<XtdRelSequences> findAll(@NotNull QuerySpecification specification) {
        return queryDelegate.findAll(specification);
    }

    @Override
    public @NotNull long count(@NotNull QuerySpecification specification) {
        return queryDelegate.count(specification);
    }
}
