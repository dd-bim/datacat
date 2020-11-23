package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.Tag;
import de.bentrm.datacat.catalog.repository.TagRepository;
import de.bentrm.datacat.catalog.service.TagService;
import de.bentrm.datacat.catalog.specification.TagSpecification;
import de.bentrm.datacat.graphql.dto.LocalizedTextDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Locale;
import java.util.function.Consumer;

@Slf4j
@Service
@Validated
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

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
        Assert.notNull(specification, "specification may not be null");
        return tagRepository.findAll(specification);
    }
}
