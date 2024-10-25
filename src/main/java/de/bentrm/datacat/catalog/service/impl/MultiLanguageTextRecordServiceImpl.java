package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.domain.XtdMultiLanguageText;
import de.bentrm.datacat.catalog.domain.XtdSubject;
import de.bentrm.datacat.catalog.domain.XtdText;
import de.bentrm.datacat.catalog.repository.TextRepository;
import de.bentrm.datacat.catalog.repository.MultiLanguageTextRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.MultiLanguageTextRecordService;
import lombok.extern.slf4j.Slf4j;

import org.neo4j.ogm.session.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.util.Assert;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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

    public MultiLanguageTextRecordServiceImpl(SessionFactory sessionFactory,
            MultiLanguageTextRepository repository,
            TextRepository textRepository,
            CatalogCleanupService cleanupService) {
        super(XtdMultiLanguageText.class, sessionFactory, repository, cleanupService);
        this.textRepository = textRepository;
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.MultiLanguageText;
    }

    @Override
    public List<XtdText> getTexts(XtdMultiLanguageText multiLanguageText) {
        Assert.notNull(multiLanguageText, "MultiLanguageText must not be null");
        final List<String> textIds = textRepository
                .findAllTextIdsAssignedToMultiLanguageText(multiLanguageText.getId());
        final Iterable<XtdText> texts = textRepository.findAllById(textIds);

        return StreamSupport
                .stream(texts.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public @NotNull XtdMultiLanguageText setRelatedRecords(@NotBlank String recordId,
                                                    @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {

        final XtdMultiLanguageText multiLanguageText = getRepository().findById(recordId, 0).orElseThrow();

        switch (relationType) {
            case Texts:
                final Iterable<XtdText> items = textRepository.findAllById(relatedRecordIds, 0);
                final List<XtdText> related = StreamSupport
                        .stream(items.spliterator(), false)
                        .collect(Collectors.toList());

                multiLanguageText.getTexts().clear();
                multiLanguageText.getTexts().addAll(related);
                break;
            default:
                log.error("Unsupported relation type: {}", relationType);
                break;
        }

        final XtdMultiLanguageText persistentMultiLanguageText = getRepository().save(multiLanguageText);
        log.trace("Updated relationship: {}", persistentMultiLanguageText);
        return persistentMultiLanguageText;
    }
}
