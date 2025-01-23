package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.domain.XtdLanguage;
import de.bentrm.datacat.catalog.domain.XtdText;
import de.bentrm.datacat.catalog.repository.LanguageRepository;
import de.bentrm.datacat.catalog.repository.TextRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.TextRecordService;
import de.bentrm.datacat.catalog.service.dto.TextDtoProjection;
import de.bentrm.datacat.catalog.service.dto.Relationships.LanguageDtoProjection;
import de.bentrm.datacat.graphql.dto.TextCountResult;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.util.Assert;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jTemplate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Slf4j
@Service
@Validated
@Transactional(readOnly = true)
public class TextRecordServiceImpl
        extends AbstractSimpleRecordServiceImpl<XtdText, TextRepository>
        implements TextRecordService {

            @Autowired
            private LanguageRepository languageRepository;

    public TextRecordServiceImpl(Neo4jTemplate neo4jTemplate,
                                     TextRepository repository,
                                     CatalogCleanupService cleanupService) {
        super(XtdText.class, neo4jTemplate, repository, cleanupService);
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.Text;
    }

    @Override
    public XtdLanguage getLanguage(XtdText text) {
        Assert.notNull(text, "Text must not be null");
        final String languageId = getRepository().findLanguageIdByTextId(text.getId());
        if (languageId == null) {
            return null;
        }
  
        final XtdLanguage language = languageRepository.findByIdWithDirectRelations(languageId).orElse(null);  

        return language;
        
    }

    @Transactional
    @Override
    public @NotNull XtdText setRelatedRecords(@NotBlank String recordId,
                                                    @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {

        final XtdText text = getRepository().findById(recordId).orElseThrow(() -> new IllegalArgumentException("No record with id " + recordId + " found."));

        switch (relationType) {
            case Language -> {
                if (text.getLanguage() != null) {
                    throw new IllegalArgumentException("Text already has a language assigned.");
                } else if (relatedRecordIds.size() != 1) {
                    throw new IllegalArgumentException("Exactly one language must be assigned to a text.");
                } else {
                    final XtdLanguage language = neo4jTemplate.findById(relatedRecordIds.get(0), XtdLanguage.class).orElseThrow(() -> new IllegalArgumentException("No record with id " + relatedRecordIds.get(0) + " found."));

                    text.setLanguage(language);
                }
                    }
            default -> log.error("Unsupported relation type: {}", relationType);
        }

        neo4jTemplate.saveAs(text, LanguageDtoProjection.class);
        log.trace("Updated relationship: {}", text);
        return text;
    }   

    @Transactional
    @Override
    public XtdText updateText(String textId, String value) {
        final XtdText item = getRepository().findById(textId).orElseThrow(() -> new IllegalArgumentException("No record with id " + textId + " found."));
        item.setText(value.trim());
        neo4jTemplate.saveAs(item, TextDtoProjection.class);
        return item;
    }

    @Transactional
    @Override
    public XtdText deleteText(String textId) {
        final XtdText item = getRepository().findById(textId).orElseThrow(() -> new IllegalArgumentException("No record with id " + textId + " found."));
        getRepository().deleteById(textId);
        return item;
    }

    @Override
    public TextCountResult countTexts(String textId) {
        return getRepository().countTexts(textId);
    }
}
