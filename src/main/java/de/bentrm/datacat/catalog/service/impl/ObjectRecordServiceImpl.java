package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.domain.XtdDictionary;
import de.bentrm.datacat.catalog.domain.XtdLanguage;
import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.catalog.domain.XtdSubject;
import de.bentrm.datacat.catalog.domain.XtdText;
import de.bentrm.datacat.catalog.domain.Enums.XtdStatusOfActivationEnum;
import de.bentrm.datacat.catalog.domain.XtdMultiLanguageText;
import de.bentrm.datacat.catalog.repository.DictionaryRepository;
import de.bentrm.datacat.catalog.repository.LanguageRepository;
import de.bentrm.datacat.catalog.repository.MultiLanguageTextRepository;
import de.bentrm.datacat.catalog.repository.ObjectRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.ObjectRecordService;
import de.bentrm.datacat.util.LocalizationUtils;
import de.bentrm.datacat.catalog.repository.TextRepository;
import lombok.extern.slf4j.Slf4j;

import org.neo4j.ogm.session.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.util.Assert;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Slf4j
@Service
@Validated
@Transactional(readOnly = true)
public class ObjectRecordServiceImpl
        extends AbstractSimpleRecordServiceImpl<XtdObject, ObjectRepository>
        implements ObjectRecordService {

            public final DictionaryRepository dictionaryRepository;
            private final MultiLanguageTextRepository multiLanguageTextRepository;
            private final ObjectRepository repository;
            private final LanguageRepository languageRepository;
            private final TextRepository textRepository;

    public ObjectRecordServiceImpl(SessionFactory sessionFactory,
                                    ObjectRepository repository,
                                    DictionaryRepository dictionaryRepository,
                                    MultiLanguageTextRepository multiLanguageTextRepository,
                                    LanguageRepository languageRepository,
                                    TextRepository textRepository,
                                    CatalogCleanupService cleanupService) {
        super(XtdObject.class, sessionFactory, repository, cleanupService);
        this.dictionaryRepository = dictionaryRepository;
        this.multiLanguageTextRepository = multiLanguageTextRepository;
        this.repository = repository;
        this.languageRepository = languageRepository;
        this.textRepository = textRepository;
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.Object;
    }

    @Override
    public XtdDictionary getDictionary(XtdObject object) {
        Assert.notNull(object.getId(), "Object must be persistent.");
        final String dictionaryId = dictionaryRepository.findDictionaryIdAssignedToObject(object.getId());
        if (dictionaryId == null) {
            return null;
        }
        final XtdDictionary dictionary = dictionaryRepository.findById(dictionaryId).orElse(null);
        return dictionary;
    }

    @Override
    public XtdMultiLanguageText getDeprecationExplanation(XtdObject object) {
        Assert.notNull(object.getId(), "Object must be persistent.");
        final String deprecationExplanationId = multiLanguageTextRepository.findMultiLanguageTextIdAssignedToObject(object.getId());
        if (deprecationExplanationId == null) {
            return null;
        }
        final XtdMultiLanguageText deprecationExplanation = multiLanguageTextRepository.findById(deprecationExplanationId).orElse(null);
        return deprecationExplanation;
    }

    @Override
    public List<XtdObject> getReplacedObjects(XtdObject object) {
        log.info("Fetching replaced objects for object {}", object.getId());
        Assert.notNull(object.getId(), "Object must be persistent.");
        final List<String> replacedObjectIds = repository.findAllReplacedObjectIdsAssignedToObject(object.getId());
        final Iterable<XtdObject> replacedObjects = repository.findAllById(replacedObjectIds);

        return StreamSupport
                .stream(replacedObjects.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public List<XtdObject> getReplacingObjects(XtdObject object) {
        Assert.notNull(object.getId(), "Object must be persistent.");
        final List<String> replacingObjectIds = repository.findAllReplacingObjectIdsAssignedToObject(object.getId());
        final Iterable<XtdObject> replacingObjects = repository.findAllById(replacingObjectIds);

        return StreamSupport
                .stream(replacingObjects.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public List<XtdMultiLanguageText> getNames(XtdObject object) {
        Assert.notNull(object.getId(), "Object must be persistent.");
        final List<String> nameIds = multiLanguageTextRepository.findAllNamesAssignedToObject(object.getId());
        final Iterable<XtdMultiLanguageText> names = multiLanguageTextRepository.findAllById(nameIds);

        return StreamSupport
                .stream(names.spliterator(), false)
                .collect(Collectors.toList());
    }
    
    @Transactional
    @Override
    public @NotNull XtdObject setRelatedRecords(@NotBlank String recordId,
                                                    @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {

        final XtdObject object = getRepository().findById(recordId, 0).orElseThrow();

        switch (relationType) {
            case Dictionary:
                if (object.getDictionary() != null) {
                    throw new IllegalArgumentException("Object already has a dictionary assigned.");
                } else if (relatedRecordIds.size() != 1) {
                    throw new IllegalArgumentException("Exactly one dictionary must be assigned.");
                } else {
                    final XtdDictionary dictionary = dictionaryRepository.findById(relatedRecordIds.get(0)).orElseThrow();
                    object.setDictionary(dictionary);
                }
                break;
            case DeprecationExplanation:
                if (object.getDeprecationExplanation() != null) {
                    throw new IllegalArgumentException("Object already has a deprecation explanation assigned.");
                } else if (relatedRecordIds.size() != 1) {
                    throw new IllegalArgumentException("Exactly one deprecation explanation must be assigned.");
                } else {
                    final XtdMultiLanguageText deprecationExplanation = multiLanguageTextRepository.findById(relatedRecordIds.get(0)).orElseThrow();
                    object.setDeprecationExplanation(deprecationExplanation);
                }
                break;
            case ReplacedObjects:
                final Iterable<XtdObject> replacedObjects = repository.findAllById(relatedRecordIds);
                final List<XtdObject> relatedObjects = StreamSupport
                        .stream(replacedObjects.spliterator(), false)
                        .collect(Collectors.toList());

                object.getReplacedObjects().clear();
                object.getReplacedObjects().addAll(relatedObjects);
                break;
            default:
                log.error("Unsupported relation type: {}", relationType);
                break;
        }

        final XtdObject persistentObject = getRepository().save(object);
        log.trace("Updated relationship: {}", persistentObject);
        return persistentObject;
    } 
    
    @Transactional
    @Override
    public XtdObject addComment(String id, String commentId, String languageTag, String value) {
        final XtdObject item = repository.findById(id).orElseThrow();
        final XtdLanguage language = languageRepository.findByCode(languageTag);

        final XtdText text = new XtdText();
        text.setText(value);
        text.setId(commentId);
        text.setLanguage(language);

        XtdMultiLanguageText multiLanguage = item.getComments().stream().findFirst().orElse(null);
        if (multiLanguage == null) {
            multiLanguage = new XtdMultiLanguageText();
        }
        multiLanguage.getTexts().add(text);

        item.getComments().clear();
        item.getComments().add(multiLanguage);

        return repository.save(item);
    }

    @Transactional
    @Override
    public XtdObject addName(String id, String nameId, String languageTag, String value) {
        final XtdObject item = repository.findById(id).orElseThrow();
        final XtdLanguage language = languageRepository.findByCode(languageTag);

        final XtdText text = new XtdText();
        text.setText(value);
        text.setId(nameId);
        text.setLanguage(language);

        XtdMultiLanguageText multiLanguage = item.getNames().stream().findFirst().orElse(null);
        if (multiLanguage == null) {
            multiLanguage = new XtdMultiLanguageText();
        }
        multiLanguage.getTexts().add(text);

        item.getNames().clear();
        item.getNames().add(multiLanguage);

        return repository.save(item);
    }

    @Transactional
    @Override
    public @NotNull XtdObject updateStatus(String id, XtdStatusOfActivationEnum status) {
        final XtdObject item = repository.findById(id).orElseThrow();
        item.setStatus(status);
        return repository.save(item);
    }

    @Transactional
    @Override
    public @NotNull XtdObject updateMajorVersion(String id, Integer majorVersion) {
        final XtdObject item = repository.findById(id).orElseThrow();
        item.setMajorVersion(majorVersion);
        return repository.save(item);
    }

    @Transactional
    @Override
    public @NotNull XtdObject updateMinorVersion(String id, Integer minorVersion) {
        final XtdObject item = repository.findById(id).orElseThrow();
        item.setMinorVersion(minorVersion);
        return repository.save(item);
    }
}
