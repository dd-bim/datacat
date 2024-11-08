package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.domain.*;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.LanguageService;
import de.bentrm.datacat.catalog.service.SimpleRecordService;
import de.bentrm.datacat.catalog.service.value.ValueMapper;
import de.bentrm.datacat.graphql.input.CatalogEntryPropertiesInput;
import de.bentrm.datacat.graphql.input.TranslationInput;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.session.SessionFactory;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Slf4j
public abstract class AbstractSimpleRecordServiceImpl<T extends CatalogRecord, R extends EntityRepository<T>>
        extends AbstractQueryServiceImpl<T, R>
        implements SimpleRecordService<T> {

    protected final ValueMapper VALUE_MAPPER = ValueMapper.INSTANCE;
    private final CatalogCleanupService cleanupService;

    @Autowired
    private LanguageService languageService;

    @Autowired
    private Session session;

    public AbstractSimpleRecordServiceImpl(Class<T> domainClass,
            SessionFactory sessionFactory,
            R repository,
            CatalogCleanupService cleanupService) {
        super(domainClass, sessionFactory, repository);
        this.cleanupService = cleanupService;
    }

    @Transactional
    @Override
    public @NotNull CatalogRecord addRecord(@Valid CatalogEntryPropertiesInput properties) {
        T newRecord;
        String id = properties.getId();
        try {
            newRecord = this.getDomainClass().getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            log.warn("Can not instantiate catalog records of type: {}", this.getDomainClass());
            throw new IllegalArgumentException("unsupported record type");
        }

        if (id != null) {
            final boolean idIsTaken = this.getRepository().existsById(id.trim());
            if (idIsTaken) {
                throw new IllegalArgumentException("Id is already in use.");
            } else {
                newRecord.setId(id);
            }

        }

        if (newRecord instanceof XtdObject xtdObject) {
            final XtdMultiLanguageText multiLanguage = xtdObject.getNames().stream().findFirst()
                    .orElse(new XtdMultiLanguageText());
            properties.getNames().forEach(name -> {
                xtdObject.getNames().clear();
                xtdObject.getNames().add(createText(multiLanguage, name));
            });
            if (properties.getComments() != null) {
                final XtdMultiLanguageText multiLanguageComment = xtdObject.getComments().stream().findFirst()
                        .orElse(new XtdMultiLanguageText());
                properties.getComments().forEach(comment -> {
                    xtdObject.getComments().clear();
                    xtdObject.getComments().add(createText(multiLanguageComment, comment));
                });
            }
            xtdObject.setMajorVersion(properties.getMajorVersion());
            xtdObject.setMinorVersion(properties.getMinorVersion());
            xtdObject.setStatus(properties.getStatus());
        }
        if (newRecord instanceof XtdConcept xtdConcept) {
            if (properties.getDescriptions() != null) {
                final XtdMultiLanguageText multiLanguage = xtdConcept.getDescriptions().stream().findFirst()
                        .orElse(new XtdMultiLanguageText());
                properties.getDescriptions().forEach(description -> {
                    xtdConcept.getDescriptions().clear();
                    xtdConcept.getDescriptions().add(createText(multiLanguage, description));
                });
            }
        }
        if (newRecord instanceof XtdProperty property) {
            VALUE_MAPPER.setProperties(properties.getPropertyProperties(), property);
        }
        if (newRecord instanceof XtdUnit unit) {
            VALUE_MAPPER.setProperties(properties.getUnitProperties(), unit);
        }
        if (newRecord instanceof XtdExternalDocument externalDocument) {
            VALUE_MAPPER.setProperties(properties.getExternalDocumentProperties(), externalDocument);
        }
        if (newRecord instanceof XtdCountry country) {
            VALUE_MAPPER.setProperties(properties.getCountryProperties(), country);
        }
        if (newRecord instanceof XtdSubdivision subdivision) {
            VALUE_MAPPER.setProperties(properties.getCountryProperties(), subdivision);
        }
        if (newRecord instanceof XtdValue value) {
            VALUE_MAPPER.setProperties(properties.getValueProperties(), value);
        }
        if (newRecord instanceof XtdOrderedValue orderedValue) {
            VALUE_MAPPER.setProperties(properties.getOrderedValueProperties(), orderedValue);
        }
        if (newRecord instanceof XtdInterval interval) {
            VALUE_MAPPER.setProperties(properties.getIntervalProperties(), interval);
        }
        if (newRecord instanceof XtdLanguage language) {
            VALUE_MAPPER.setProperties(properties.getLanguageProperties(), language);
        }
        if (newRecord instanceof XtdText text) {
            VALUE_MAPPER.setProperties(properties.getTextProperties(), text);
        }

        newRecord = this.getRepository().save(newRecord);
        log.trace("Persisted new catalog entry: {}", newRecord);
        return newRecord;
    }

    public XtdMultiLanguageText createText(XtdMultiLanguageText multiLanguage, TranslationInput translation) {
        final XtdLanguage language = languageService.findByCode(translation.getLanguageTag());

        final XtdText text = new XtdText();
        text.setText(translation.getValue());
        text.setId(translation.getId());
        text.setLanguage(language);

        multiLanguage.getTexts().add(text);

        session.save(multiLanguage);
        session.save(text);

        return multiLanguage;
    }

    @Transactional
    @Override
    public @NotNull T removeRecord(@NotBlank String id) {
        log.trace("Deleting simple catalog record with id {}...", id);
        final T entry = this.getRepository()
                .findById(id)
                .orElseThrow();

        cleanupService.purgeRelatedData(id);
        this.getRepository().deleteById(id);

        log.trace("Catalog item deleted: {}", entry);

        return entry;
    }

    @Transactional
    @Override
    public @NotNull T removeRelationship(@NotBlank String recordId, @NotBlank String relatedRecordId,
            @NotNull SimpleRelationType relationType) {
        log.trace("Deleting relationship from record with id {}...", recordId);
        final T entry = this.getRepository()
                .findById(recordId)
                .orElseThrow();

        cleanupService.purgeRelationship(recordId, relatedRecordId, relationType);
        return entry;
    }

    @Transactional
    @Override
    public @NotNull T setRelatedRecords(@NotBlank String recordId,
            @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {
        T record = this.getRepository()
                .findById(recordId)
                .orElseThrow();
        return record;
    }
}
