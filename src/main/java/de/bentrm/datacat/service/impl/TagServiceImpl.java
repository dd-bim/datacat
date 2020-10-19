package de.bentrm.datacat.service.impl;

import de.bentrm.datacat.catalog.domain.Tag;
import de.bentrm.datacat.catalog.repository.TagRepository;
import de.bentrm.datacat.catalog.service.value.ValueMapper;
import de.bentrm.datacat.catalog.specification.TagSpecification;
import de.bentrm.datacat.graphql.dto.LocalizedTextDto;
import de.bentrm.datacat.service.TagService;
import de.bentrm.datacat.service.dto.TagDto;
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

    private final ValueMapper valueMapper = ValueMapper.INSTANCE;

    @Autowired
    private TagRepository tagRepository;

    protected final Consumer<LocalizedTextDto> translationValidator = (dto) -> {
        final Locale locale = Locale.forLanguageTag(dto.getLanguageTag());
        log.trace("Identifying locale from languageTag {} as {}", dto.getLanguageTag(), locale);
        if (locale.getLanguage().equals("")) throw new IllegalArgumentException("Illegal language tag provided.");
    };

    @Override
    public TagDto create(TagDto dto) {
        Assert.notEmpty(dto.getNames(), "At least one translation of the tags name must be provided.");
        dto.getNames().forEach(translationValidator);
        dto.getDescriptions().forEach(translationValidator);
        Tag tag = valueMapper.toTag(dto);
        tag = tagRepository.save(tag);

        return valueMapper.toDto(tag);
    }

    @Override
    public TagDto update(TagDto dto) {
        Assert.notNull(dto.getId(), "No id provided.");
        Assert.notEmpty(dto.getNames(), "At least one translation of the tags name must be provided.");
        dto.getNames().forEach(translationValidator);
        dto.getDescriptions().forEach(translationValidator);
        Tag tag = tagRepository.findById(dto.getId()).orElseThrow();
        valueMapper.setProperties(dto, tag);
        tag = tagRepository.save(tag);
        return valueMapper.toDto(tag);
    }

    @Override
    public TagDto delete(String id) {
        final Tag tag = tagRepository.findById(id).orElseThrow();
        tagRepository.delete(tag);
        return valueMapper.toDto(tag);
    }

    @Override
    public @NotNull TagDto findById(@NotNull String id) {
        Assert.notNull(id, "id may not be null.");
        final Tag tag = tagRepository.findById(id).orElseThrow();
        return valueMapper.toDto(tag);
    }

    @Override
    public @NotNull Page<TagDto> findAll(TagSpecification specification) {
        Assert.notNull(specification, "specification may not be null");

        final Page<Tag> results = tagRepository.findAll(specification);
        log.trace("Retrieved {} tag items.", results.getNumberOfElements());
        log.trace("Result page content: {}", results.getContent());

        final Page<TagDto> payload = results.map(tag -> valueMapper.toDto(tag));
        log.trace("Dto page content: {}", payload.getContent());
        return payload;
    }
}
