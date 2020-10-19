package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.base.specification.QuerySpecification;
import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdRelAssignsProperties;
import de.bentrm.datacat.catalog.repository.ObjectRepository;
import de.bentrm.datacat.catalog.repository.PropertyRepository;
import de.bentrm.datacat.catalog.repository.RelAssignsPropertiesRepository;
import de.bentrm.datacat.catalog.service.AssignsPropertiesService;
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
public class AssignsPropertiesServiceImpl implements AssignsPropertiesService {

    private final EntityMapper entityMapper = EntityMapper.INSTANCE;
    private final RelAssignsPropertiesRepository assignsCollectionsRepository;
    private final ObjectRepository objectRepository;
    private final PropertyRepository propertyRepository;

    private final QueryServiceDelegate<XtdRelAssignsProperties> queryDelegate;

    public AssignsPropertiesServiceImpl(RelAssignsPropertiesRepository repository,
                                        ObjectRepository objectRepository,
                                        PropertyRepository propertyRepository) {
        this.assignsCollectionsRepository = repository;
        this.objectRepository = objectRepository;
        this.propertyRepository = propertyRepository;
        this.queryDelegate = new QueryServiceDelegate<>(repository);
    }

    @Transactional
    @Override
    public @NotNull XtdRelAssignsProperties create(OneToManyRelationshipValue value) {
        final XtdRelAssignsProperties relation = new XtdRelAssignsProperties();

        entityMapper.setProperties(value, relation);

        final XtdObject relating = objectRepository.findById(value.getFrom()).orElseThrow();
        relation.setRelatingObject(relating);

        final Iterable<XtdProperty> things = propertyRepository.findAllById(value.getTo());
        final List<XtdProperty> related = new ArrayList<>();
        things.forEach(related::add);
        if (related.isEmpty()) {
            throw new IllegalArgumentException("A relationship must have at least one related member.");
        }
        relation.getRelatedProperties().addAll(related);

        return assignsCollectionsRepository.save(relation);
    }

    @Override
    public @NotNull Optional<XtdRelAssignsProperties> findById(@NotNull String id) {
        return queryDelegate.findById(id);
    }

    @Override
    public @NotNull List<XtdRelAssignsProperties> findAllByIds(@NotNull List<String> ids) {
        return queryDelegate.findAllByIds(ids);
    }

    @Override
    public @NotNull Page<XtdRelAssignsProperties> findAll(@NotNull QuerySpecification specification) {
        return queryDelegate.findAll(specification);
    }

    @Override
    public @NotNull long count(@NotNull QuerySpecification specification) {
        return queryDelegate.count(specification);
    }
}
