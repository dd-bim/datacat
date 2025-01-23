package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.domain.XtdMultiLanguageText;
import de.bentrm.datacat.catalog.domain.XtdText;
import de.bentrm.datacat.catalog.repository.TextRepository;
import de.bentrm.datacat.catalog.repository.MultiLanguageTextRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.MultiLanguageTextRecordService;
import de.bentrm.datacat.catalog.service.TextRecordService;
import de.bentrm.datacat.catalog.service.dto.Relationships.TextsDtoProjection;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.util.Assert;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@Validated
@Transactional(readOnly = true)
public class MultiLanguageTextRecordServiceImpl
        extends AbstractSimpleRecordServiceImpl<XtdMultiLanguageText, MultiLanguageTextRepository>
        implements MultiLanguageTextRecordService {

    private final TextRepository textRepository;

    @Autowired
    private TextRecordService textRecordService;

    public MultiLanguageTextRecordServiceImpl(Neo4jTemplate neo4jTemplate,
            MultiLanguageTextRepository repository,
            TextRepository textRepository,
            CatalogCleanupService cleanupService) {
        super(XtdMultiLanguageText.class, neo4jTemplate, repository, cleanupService);
        this.textRepository = textRepository;
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.MultiLanguageText;
    }

    @Override
    public List<XtdText> getTexts(XtdMultiLanguageText multiLanguageText) {
        Assert.notNull(multiLanguageText, "MultiLanguageText must not be null");
        final List<String> textIds = getRepository()
                .findAllTextIdsAssignedToMultiLanguageText(multiLanguageText.getId());
        final Iterable<XtdText> texts = textRecordService.findAllEntitiesById(textIds);

        return StreamSupport
                .stream(texts.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public @NotNull XtdMultiLanguageText setRelatedRecords(@NotBlank String recordId,
                                                    @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {

        final XtdMultiLanguageText multiLanguageText = getRepository().findById(recordId).orElseThrow(() -> new IllegalArgumentException("No record with id " + recordId + " found."));

        switch (relationType) {
            case Texts -> {
                final Iterable<XtdText> items = textRepository.findAllById(relatedRecordIds);
                final List<XtdText> related = StreamSupport
                        .stream(items.spliterator(), false)
                        .collect(Collectors.toList());

                multiLanguageText.getTexts().clear();
                multiLanguageText.getTexts().addAll(related);
            }
            default -> log.error("Unsupported relation type: {}", relationType);
        }

        neo4jTemplate.saveAs(multiLanguageText, TextsDtoProjection.class);
        log.trace("Updated relationship: {}", multiLanguageText);
        return multiLanguageText;
    }
}
