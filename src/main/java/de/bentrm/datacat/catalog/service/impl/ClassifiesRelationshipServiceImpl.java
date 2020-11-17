package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.base.specification.QuerySpecification;
import de.bentrm.datacat.catalog.domain.XtdClassification;
import de.bentrm.datacat.catalog.domain.XtdRelClassifies;
import de.bentrm.datacat.catalog.domain.XtdRoot;
import de.bentrm.datacat.catalog.repository.ClassificationRepository;
import de.bentrm.datacat.catalog.repository.ClassifiesRelationshipRepository;
import de.bentrm.datacat.catalog.repository.RootRepository;
import de.bentrm.datacat.catalog.service.ClassifiesRelationshipService;
import de.bentrm.datacat.catalog.service.EntityMapper;
import de.bentrm.datacat.catalog.service.value.OneToManyRelationshipValue;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Validated
@Transactional(readOnly = true)
public class ClassifiesRelationshipServiceImpl implements ClassifiesRelationshipService {

    private final EntityMapper entityMapper = EntityMapper.INSTANCE;
    private final ClassifiesRelationshipRepository classifiesRelationshipRepository;
    private final ClassificationRepository classificationRepository;
    private final RootRepository rootRepository;

    private final QueryDelegate<XtdRelClassifies> queryDelegate;

    public ClassifiesRelationshipServiceImpl(ClassifiesRelationshipRepository repository,
                                             ClassificationRepository classificationRepository,
                                             RootRepository rootRepository) {
        this.classifiesRelationshipRepository = repository;
        this.classificationRepository = classificationRepository;
        this.rootRepository = rootRepository;
        this.queryDelegate = new QueryDelegate<>(repository);
    }

    @Transactional
    @Override
    public @NotNull XtdRelClassifies create(OneToManyRelationshipValue value) {
        final XtdRelClassifies relation = new XtdRelClassifies();

        entityMapper.setProperties(value, relation);

        final XtdClassification relating = classificationRepository.findById(value.getFrom()).orElseThrow();
        relation.setRelatingClassification(relating);

        final Iterable<XtdRoot> things = rootRepository.findAllById(value.getTo());
        final List<XtdRoot> related = new ArrayList<>();
        things.forEach(related::add);
        if (related.isEmpty()) {
            throw new IllegalArgumentException("A relationship must have at least one related member.");
        }
        relation.getRelatedThings().addAll(related);

        return classifiesRelationshipRepository.save(relation);
    }

    @Override
    public @NotNull Optional<XtdRelClassifies> findById(@NotNull String id) {
        return queryDelegate.findById(id);
    }

    @Override
    public @NotNull List<XtdRelClassifies> findAllByIds(@NotNull List<String> ids) {
        return queryDelegate.findAllByIds(ids);
    }

    @Override
    public @NotNull Page<XtdRelClassifies> findAll(@NotNull QuerySpecification specification) {
        return queryDelegate.findAll(specification);
    }

    @Override
    public @NotNull long count(@NotNull QuerySpecification specification) {
        return queryDelegate.count(specification);
    }
}
