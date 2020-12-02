package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.Tag;
import de.bentrm.datacat.catalog.repository.TagRepository;
import de.bentrm.datacat.catalog.service.TagService;
import de.bentrm.datacat.catalog.specification.TagSpecification;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Slf4j
@Service
@Validated
public class TagServiceImpl extends AbstractServiceImpl<Tag> implements TagService {

    public TagServiceImpl(SessionFactory sessionFactory, TagRepository repository) {
        super(Tag.class, sessionFactory, repository);
    }

    @Override
    public @NotNull Page<Tag> findAll(@NotNull TagSpecification specification) {
        return super.findAll(specification);
    }

    @Override
    public @NotNull long count(@NotNull TagSpecification specification) {
        return super.count(specification);
    }
}
