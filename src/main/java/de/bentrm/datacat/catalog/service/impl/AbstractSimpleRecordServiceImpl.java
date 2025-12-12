package de.bentrm.datacat.catalog.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.transaction.annotation.Transactional;

import de.bentrm.datacat.base.repository.EntityRepository;
import de.bentrm.datacat.catalog.repository.CountryRepository;
import de.bentrm.datacat.catalog.repository.LanguageRepository;
import de.bentrm.datacat.catalog.repository.MultiLanguageTextRepository;
import de.bentrm.datacat.catalog.repository.TextRepository;
import de.bentrm.datacat.catalog.domain.CatalogRecord;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.domain.XtdConcept;
import de.bentrm.datacat.catalog.domain.XtdCountry;
import de.bentrm.datacat.catalog.domain.XtdDictionary;
import de.bentrm.datacat.catalog.domain.XtdDimension;
import de.bentrm.datacat.catalog.domain.XtdExternalDocument;
import de.bentrm.datacat.catalog.domain.XtdInterval;
import de.bentrm.datacat.catalog.domain.XtdLanguage;
import de.bentrm.datacat.catalog.domain.XtdMultiLanguageText;
import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.catalog.domain.XtdOrderedValue;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdRational;
import de.bentrm.datacat.catalog.domain.XtdSubdivision;
import de.bentrm.datacat.catalog.domain.XtdSymbol;
import de.bentrm.datacat.catalog.domain.XtdText;
import de.bentrm.datacat.catalog.domain.XtdUnit;
import de.bentrm.datacat.catalog.domain.XtdValue;
import de.bentrm.datacat.catalog.domain.XtdValueList;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.SimpleRecordService;
import de.bentrm.datacat.catalog.service.dto.Relationships.CountryOfOriginDtoProjection;
import de.bentrm.datacat.catalog.service.value.ValueMapper;
import de.bentrm.datacat.graphql.input.CatalogEntryPropertiesInput;
import de.bentrm.datacat.graphql.input.DimensionInput;
import de.bentrm.datacat.graphql.input.RationalInput;
import de.bentrm.datacat.graphql.input.TranslationInput;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractSimpleRecordServiceImpl<T extends CatalogRecord, R extends EntityRepository<T>>
        extends AbstractQueryServiceImpl<T, R> implements SimpleRecordService<T> {

    protected final ValueMapper VALUE_MAPPER = ValueMapper.INSTANCE;
    private final CatalogCleanupService cleanupService;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private TextRepository textRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private MultiLanguageTextRepository multiLanguageTextRepository;

    public AbstractSimpleRecordServiceImpl(Class<T> domainClass, Neo4jTemplate neo4jTemplate, R repository,
            CatalogCleanupService cleanupService) {
        super(domainClass, neo4jTemplate, repository);
        this.cleanupService = cleanupService;
    }

    @Transactional
    @Override
    public @NotNull CatalogRecord addRecord(@Valid CatalogEntryPropertiesInput properties) {
        T newRecord;
        String id = null;
        if (properties.getId() != null && !properties.getId().isBlank())
            id = properties.getId();

        try {
            newRecord = this.getDomainClass().getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            log.warn("Can not instantiate catalog records of type: {}", this.getDomainClass());
            throw new IllegalArgumentException("unsupported record type");
        }

        if (id != null) {
            final Boolean idIsTaken = this.getRepository().existsById(id.trim());
            if (idIsTaken) {
                throw new IllegalArgumentException("Id is already in use.");
            } else {
                newRecord.setId(id);
            }
        }

        if (newRecord instanceof XtdObject xtdObject) {
            setMultiLanguageText(properties.getNames(), xtdObject.getNames()::add);
            setMultiLanguageText(properties.getComments(), xtdObject.getComments()::add);
            setMultiLanguageText(properties.getDeprecationExplanation(), xtdObject::setDeprecationExplanation);

            xtdObject.setMajorVersion(properties.getMajorVersion());
            xtdObject.setMinorVersion(properties.getMinorVersion());
            xtdObject.setDateOfCreation(properties.getDateOfCreation());
            xtdObject.setStatus(properties.getStatus());
        }
        if (newRecord instanceof XtdConcept xtdConcept) {
            setMultiLanguageText(properties.getDescriptions(), xtdConcept.getDescriptions()::add);
            setMultiLanguageText(properties.getDefinition(), xtdConcept::setDefinition);
            setMultiLanguageText(properties.getExamples(), xtdConcept.getExamples()::add);
            setLanguage(properties.getLanguageOfCreator(), xtdConcept::setLanguageOfCreator);
        }
        if (newRecord instanceof XtdProperty property) {
            VALUE_MAPPER.setProperties(properties.getPropertyProperties(), property);
        }
        if (newRecord instanceof XtdUnit unit) {
            VALUE_MAPPER.setProperties(properties.getUnitProperties(), unit);
            if (properties.getUnitProperties() != null) {
                if (properties.getUnitProperties().getSymbol() != null) {
                    setMultiLanguageText(properties.getUnitProperties().getSymbol(), unit::setSymbol);
                }
                if (properties.getUnitProperties().getCoefficient() != null) {
                    unit.setCoefficient(createRational(properties.getUnitProperties().getCoefficient()));
                }
                if (properties.getUnitProperties().getOffset() != null) {
                    unit.setOffset(createRational(properties.getUnitProperties().getOffset()));
                }
            }
        }
        if (newRecord instanceof XtdExternalDocument externalDocument) {
            if (properties.getExternalDocumentProperties() != null) {
                VALUE_MAPPER.setProperties(properties.getExternalDocumentProperties(), externalDocument);
                List<String> langTag = properties.getExternalDocumentProperties().getLanguageTag();
                if (langTag != null) {
                    for (String tag : langTag) {
                        setLanguage(tag, externalDocument.getLanguages()::add);
                    }
                }
            }
        }
        if (newRecord instanceof XtdCountry country) {
            VALUE_MAPPER.setProperties(properties.getCountryProperties(), country);
        }
        if (newRecord instanceof XtdSubdivision subdivision) {
            VALUE_MAPPER.setProperties(properties.getSubdivisionProperties(), subdivision);
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
        if (newRecord instanceof XtdRational rational) {
            VALUE_MAPPER.setProperties(properties.getRationalProperties(), rational);
        }
        if (newRecord instanceof XtdValueList valueList) {
            if (properties.getValueListProperties() != null) {
                setLanguage(properties.getValueListProperties().getLanguageTag(), valueList::setLanguage);
            }
        }
        if (newRecord instanceof XtdSymbol symbol) {
            if (properties.getSymbolProperties() != null) {
                XtdText symbolText = createText(properties.getSymbolProperties().getSymbol());
                symbol.setSymbol(symbolText);
            } else {
                throw new IllegalArgumentException("Symbol properties are required.");
            }
        }
        if (newRecord instanceof XtdDictionary dictionary) {
            XtdMultiLanguageText multiLanguage = createMultiLanguageText(properties.getNames());
            dictionary.setName(multiLanguage);
        }
        if (newRecord instanceof XtdDimension dimension) {
            DimensionInput dimensionProperties = properties.getDimensionProperties();
            if (dimensionProperties != null) {
                dimension.setAmountOfSubstanceExponent(
                        createRational(dimensionProperties.getAmountOfSubstanceExponent()));
                dimension.setElectricCurrentExponent(createRational(dimensionProperties.getElectricCurrentExponent()));
                dimension.setLengthExponent(createRational(dimensionProperties.getLengthExponent()));
                dimension.setLuminousIntensityExponent(
                        createRational(dimensionProperties.getLuminousIntensityExponent()));
                dimension.setMassExponent(createRational(dimensionProperties.getMassExponent()));
                dimension.setThermodynamicTemperatureExponent(
                        createRational(dimensionProperties.getThermodynamicTemperatureExponent()));
                dimension.setTimeExponent(createRational(dimensionProperties.getTimeExponent()));
            }
        }

        newRecord = this.getRepository().save(newRecord);

        if (newRecord instanceof XtdConcept concept) {
            setCountry(properties.getCountryOfOrigin(), concept::setCountryOfOrigin);
            neo4jTemplate.saveAs(concept, CountryOfOriginDtoProjection.class);
        }

        log.trace("Persisted new catalog entry: {}", newRecord);
        return newRecord;
    }

    @Transactional
    public XtdMultiLanguageText createMultiLanguageText(List<TranslationInput> translations) {
        XtdMultiLanguageText multiLanguage = new XtdMultiLanguageText();
        Set<XtdText> texts = new HashSet<>();
        translations.forEach(translation -> {
            XtdText text = createText(translation);
            texts.add(text);
        });
        multiLanguage.getTexts().addAll(texts);
        multiLanguage = multiLanguageTextRepository.save(multiLanguage);
        return multiLanguage;
    }

    private void setMultiLanguageText(List<TranslationInput> translations, Consumer<XtdMultiLanguageText> setter) {
        if (translations != null) {
            XtdMultiLanguageText multiLanguage = createMultiLanguageText(translations);
            setter.accept(multiLanguage);
        }
    }

    private void setLanguage(String languageCode, Consumer<XtdLanguage> setter) {
        if (languageCode != null) {
            XtdLanguage language = languageRepository.findByCode(languageCode).orElseThrow(
                    () -> new IllegalArgumentException("No language record with code " + languageCode + " found."));
            setter.accept(language);
        }
    }

    private void setCountry(String countryCode, Consumer<XtdCountry> setter) {
        if (countryCode != null) {
            XtdCountry country = countryRepository.findByCode(countryCode).orElseThrow(
                    () -> new IllegalArgumentException("No country record with code " + countryCode + " found."));
            setter.accept(country);
        }
    }

    @Transactional
    public XtdText createText(TranslationInput translation) {
        final XtdLanguage language = languageRepository.findByCode(translation.getLanguageTag())
                .orElseThrow(() -> new IllegalArgumentException(
                        "No language record with code " + translation.getLanguageTag() + " found."));

        final XtdText text = new XtdText();
        text.setText(translation.getValue());
        if (translation.getId() != null && !translation.getId().isBlank())
            text.setId(translation.getId());
        text.setLanguage(language);

        textRepository.save(text);
        return text;
    }

    private XtdRational createRational(RationalInput input) {
        if (input == null) {
            return null;
        }
        XtdRational rational = new XtdRational();
        rational.setNumerator(input.getNumerator());
        rational.setDenominator(input.getDenominator());
        return rational;
    }

    @Transactional
    @Override
    public @NotNull T removeRecord(@NotBlank String id) {
        log.trace("Deleting simple catalog record with id {}...", id);
        final T entry = this.getRepository().findByIdWithDirectRelations(id)
                .orElseThrow(() -> new IllegalArgumentException("No record with id " + id + " found."));

        cleanupService.deleteNodeWithRelationships(id);

        log.trace("Catalog item deleted: {}", entry);

        return entry;
    }

    @Transactional
    @Override
    public @NotNull T removeRelationship(@NotBlank String recordId, @NotBlank String relatedRecordId,
            @NotNull SimpleRelationType relationType) {
        log.info("removeRelationship called: recordId={}, relatedRecordId={}, relationType={}", 
            recordId, relatedRecordId, relationType.getRelationProperty());
        log.trace("Deleting relationship from record with id {}...", recordId);
        final T entry = this.getRepository().findByIdWithDirectRelations(recordId)
                .orElseThrow(() -> new IllegalArgumentException("No record with id " + recordId + " found."));

        log.info("Found entry, calling purgeRelationship...");
        cleanupService.purgeRelationship(recordId, relatedRecordId, relationType);
        log.info("purgeRelationship completed");
        return entry;
    }

    @Transactional
    @Override
    public @NotNull T setRelatedRecords(@NotBlank String recordId, @NotEmpty List<@NotBlank String> relatedRecordIds,
            @NotNull SimpleRelationType relationType) {
        T record = this.getRepository().findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("No record with id " + recordId + " found."));
        return record;
    }
}
