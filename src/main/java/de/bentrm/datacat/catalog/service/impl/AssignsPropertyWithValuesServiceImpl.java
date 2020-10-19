package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.base.specification.QuerySpecification;
import de.bentrm.datacat.catalog.domain.XtdRelAssignsPropertyWithValues;
import de.bentrm.datacat.catalog.repository.ObjectRepository;
import de.bentrm.datacat.catalog.repository.PropertyRepository;
import de.bentrm.datacat.catalog.repository.RelAssignsPropertyWithValuesRepository;
import de.bentrm.datacat.catalog.service.AssignsPropertyWithValuesService;
import de.bentrm.datacat.catalog.service.EntityMapper;
import de.bentrm.datacat.catalog.service.value.QualifiedOneToOneRelationshipValue;
import org.apache.commons.lang3.NotImplementedException;
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
public class AssignsPropertyWithValuesServiceImpl implements AssignsPropertyWithValuesService {

    private final EntityMapper entityMapper = EntityMapper.INSTANCE;
    private final RelAssignsPropertyWithValuesRepository repository;
    private final ObjectRepository objectRepository;
    private final PropertyRepository propertyRepository;

    private final QueryServiceDelegate<XtdRelAssignsPropertyWithValues> queryDelegate;

    public AssignsPropertyWithValuesServiceImpl(RelAssignsPropertyWithValuesRepository repository,
                                                ObjectRepository objectRepository,
                                                PropertyRepository propertyRepository) {
        this.repository = repository;
        this.objectRepository = objectRepository;
        this.propertyRepository = propertyRepository;
        this.queryDelegate = new QueryServiceDelegate<>(repository);
    }

    @Transactional
    @Override
    public @NotNull XtdRelAssignsPropertyWithValues create(QualifiedOneToOneRelationshipValue value) {
        final XtdRelAssignsPropertyWithValues relation = new XtdRelAssignsPropertyWithValues();

        throw new NotImplementedException();
//        entityMapper.setProperties(value, relation);
//
//        final XtdObject relating = objectRepository.findById(value.getFrom()).orElseThrow();
//        relation.setRelatingObject(relating);
//
//        final Iterable<XtdProperty> things = propertyRepository.findAllById(value.getTo());
//        final List<XtdProperty> related = new ArrayList<>();
//        things.forEach(related::add);
//        if (related.isEmpty()) {
//            throw new IllegalArgumentException("A relationship must have at least one related member.");
//        }
//        relation.getRelatedProperties().addAll(related);
//
//        return assignsCollectionsRepository.save(relation);
    }

    @Override
    public @NotNull Optional<XtdRelAssignsPropertyWithValues> findById(@NotNull String id) {
        return queryDelegate.findById(id);
    }

    @Override
    public @NotNull List<XtdRelAssignsPropertyWithValues> findAllByIds(@NotNull List<String> ids) {
        return queryDelegate.findAllByIds(ids);
    }

    @Override
    public @NotNull Page<XtdRelAssignsPropertyWithValues> findAll(@NotNull QuerySpecification specification) {
        return queryDelegate.findAll(specification);
    }

    @Override
    public @NotNull long count(@NotNull QuerySpecification specification) {
        return queryDelegate.count(specification);
    }
}
