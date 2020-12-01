package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.XtdClassification;
import de.bentrm.datacat.catalog.domain.XtdRelClassifies;
import de.bentrm.datacat.catalog.domain.XtdRoot;
import de.bentrm.datacat.catalog.service.ClassifiesRelationshipService;
import de.bentrm.datacat.catalog.service.EntityMapper;
import de.bentrm.datacat.catalog.service.value.OneToManyRelationshipValue;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Service
@Validated
@Transactional(readOnly = true)
public class ClassifiesRelationshipServiceImpl extends AbstractServiceImpl<XtdRelClassifies> implements ClassifiesRelationshipService {

    private final EntityMapper entityMapper = EntityMapper.INSTANCE;
    private final EntityRepository<XtdClassification> classificationRepository;
    private final EntityRepository<XtdRoot> rootRepository;

    public ClassifiesRelationshipServiceImpl(SessionFactory sessionFactory,
                                             EntityRepository<XtdRelClassifies> repository,
                                             EntityRepository<XtdClassification> classificationRepository,
                                             EntityRepository<XtdRoot> rootRepository) {
        super(XtdRelClassifies.class, sessionFactory, repository);
        this.classificationRepository = classificationRepository;
        this.rootRepository = rootRepository;
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

        return getRepository().save(relation);
    }
}
