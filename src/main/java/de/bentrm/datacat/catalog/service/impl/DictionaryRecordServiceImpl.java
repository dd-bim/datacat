package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.domain.XtdDictionary;
import de.bentrm.datacat.catalog.domain.XtdMultiLanguageText;
import de.bentrm.datacat.catalog.repository.DictionaryRepository;
import de.bentrm.datacat.catalog.repository.MultiLanguageTextRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.DictionaryRecordService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.util.Assert;

import java.util.List;

import org.springframework.data.neo4j.core.Neo4jTemplate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Slf4j
@Service
@Validated
@Transactional(readOnly = true)
public class DictionaryRecordServiceImpl
        extends AbstractSimpleRecordServiceImpl<XtdDictionary, DictionaryRepository>
        implements DictionaryRecordService {

            private final MultiLanguageTextRepository multiLanguageTextRepository;

    public DictionaryRecordServiceImpl(Neo4jTemplate neo4jTemplate,
                                     DictionaryRepository repository,
                                     MultiLanguageTextRepository multiLanguageTextRepository,
                                     CatalogCleanupService cleanupService) {
        super(XtdDictionary.class, neo4jTemplate, repository, cleanupService);
        this.multiLanguageTextRepository = multiLanguageTextRepository;
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.Dictionary;
    }

    @Override
    public XtdMultiLanguageText getName(XtdDictionary dictionary) {
        Assert.notNull(dictionary.getId(), "Dictionary must be persistent.");
        final String nameId = multiLanguageTextRepository.findMultiLanguageTextIdAssignedToDictionary(dictionary.getId());
        if (nameId == null) {
            return null;
        }
        final XtdMultiLanguageText name = multiLanguageTextRepository.findById(nameId).orElse(null);
        return name;
    }

    @Transactional
    @Override
    public @NotNull XtdDictionary setRelatedRecords(@NotBlank String recordId,
                                                    @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {

        final XtdDictionary dictionary = getRepository().findById(recordId).orElseThrow();

        switch (relationType) {
            case Name -> {
                if (dictionary.getName() != null) {
                    throw new IllegalArgumentException("Dictionary already has a name assigned.");
                } else if (relatedRecordIds.size() != 1) {
                    throw new IllegalArgumentException("Exactly one name must be assigned to a dictionary.");
                } else {
                    final XtdMultiLanguageText name = multiLanguageTextRepository.findById(relatedRecordIds.get(0)).orElseThrow();
                    dictionary.setName(name);
                }   }
            default -> log.error("Unsupported relation type: {}", relationType);
        }
        
        final XtdDictionary persistentDictionary = getRepository().save(dictionary);
        log.trace("Updated relationship: {}", persistentDictionary);
        return persistentDictionary;
    }
}
