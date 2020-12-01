package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.base.specification.QuerySpecification;
import de.bentrm.datacat.catalog.domain.Tag;
import de.bentrm.datacat.catalog.service.TagService;
import de.bentrm.datacat.catalog.specification.TagSpecification;
import de.bentrm.datacat.graphql.dto.LocalizedTextDto;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.cypher.query.Pagination;
import org.neo4j.ogm.cypher.query.SortOrder;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;

@Slf4j
@Service
@Validated
public class TagServiceImpl implements TagService {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private EntityRepository<Tag> tagRepository;

    protected final Consumer<LocalizedTextDto> translationValidator = (dto) -> {
        final Locale locale = Locale.forLanguageTag(dto.getLanguageTag());
        log.trace("Identifying locale from languageTag {} as {}", dto.getLanguageTag(), locale);
        if (locale.getLanguage().equals("")) throw new IllegalArgumentException("Illegal language tag provided.");
    };

    @Override
    public @NotNull Tag findById(@NotNull String id) {
        Assert.notNull(id, "id may not be null.");
        return tagRepository.findById(id).orElseThrow();
    }

    @Override
    public @NotNull Page<Tag> findAll(TagSpecification specification) {
        Collection<Tag> tags;
        Pageable pageable;
        final long count = count(specification);
        final Session session = sessionFactory.openSession();

        final Optional<Pageable> paged = specification.getPageable();
        if (paged.isPresent()) {
            pageable = paged.get();
            final Pagination pagination = new Pagination(pageable.getPageNumber(), pageable.getPageSize());

            if (pageable.getSort().isUnsorted()) {
                tags = session.loadAll(Tag.class, specification.getFilters(), pagination);
            } else {
                final Sort sort = pageable.getSort();
                final Sort.Direction direction = sort.get().findFirst().map(Sort.Order::getDirection).get();
                final String[] properties = sort.get().map(Sort.Order::getProperty).toArray(String[]::new);
                final SortOrder sortOrder = new SortOrder(SortOrder.Direction.valueOf(direction.name()), properties);
                tags = session.loadAll(Tag.class, specification.getFilters(), sortOrder, pagination);
            }
        } else {
            pageable = PageRequest.of(0, (int) Math.max(count, 10));
            tags = session.loadAll(Tag.class, specification.getFilters());
        }

        return PageableExecutionUtils.getPage(List.copyOf(tags), pageable, () -> count);
    }

    @Override
    public @NotNull long count(@NotNull QuerySpecification specification) {
        final Session session = sessionFactory.openSession();
        return session.count(Tag.class, specification.getFilters());
    }
}
