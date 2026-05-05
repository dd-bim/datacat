package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.domain.XtdDictionary;
import de.bentrm.datacat.catalog.domain.XtdMultiLanguageText;
import de.bentrm.datacat.catalog.domain.XtdObject;
import de.bentrm.datacat.catalog.repository.DictionaryRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.DictionaryRecordService;
import de.bentrm.datacat.catalog.service.MultiLanguageTextRecordService;
import de.bentrm.datacat.catalog.service.ObjectRecordService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.util.Assert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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

    @Autowired
    private MultiLanguageTextRecordService multiLanguageTextRecordService;

    @Autowired 
    @Lazy
    private ObjectRecordService objectRecordService;

    public DictionaryRecordServiceImpl(Neo4jTemplate neo4jTemplate,
                                     DictionaryRepository repository,
                                     CatalogCleanupService cleanupService) {
        super(XtdDictionary.class, neo4jTemplate, repository, cleanupService);
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.Dictionary;
    }

    @Override
    public @NotNull XtdMultiLanguageText getName(XtdDictionary dictionary) {
        Assert.notNull(dictionary.getId(), "Dictionary must be persistent.");
        final String nameId = getRepository().findMultiLanguageTextIdAssignedToDictionary(dictionary.getId());
        if (nameId == null) {
            return null;
        }
        final XtdMultiLanguageText name = multiLanguageTextRecordService.findByIdWithDirectRelations(nameId).orElseThrow(() -> new IllegalArgumentException("No record with id " + nameId + " found."));
        return name;
    }

    @Transactional
    @Override
    public @NotNull XtdDictionary setRelatedRecords(@NotBlank String recordId,
                                                    @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {

        final XtdDictionary dictionary = getRepository().findById(recordId).orElseThrow(() -> new IllegalArgumentException("No record with id " + recordId + " found."));

        return dictionary;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public @NotNull Page<XtdObject> getConcepts(@NotNull XtdDictionary dictionary, @NotNull Pageable pageable) {
        Assert.notNull(dictionary.getId(), "Dictionary must be persistent.");
        
        final List<String> conceptIds = getRepository().findConceptsByDictionaryIdPaginated(
            dictionary.getId(), 
            (int) pageable.getOffset(), 
            pageable.getPageSize()
        );
        
        final Iterable<XtdObject> concepts = objectRecordService.findAllEntitiesById(conceptIds);
        final List<XtdObject> conceptsList = StreamSupport.stream(concepts.spliterator(), false)
            .collect(Collectors.toList());
        
        // Get total count for pagination
        final Long totalCount = getRepository().countConceptsByDictionaryId(dictionary.getId());
        
        return PageableExecutionUtils.getPage(conceptsList, pageable, () -> totalCount);
    }
}
