package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.domain.XtdSymbol;
import de.bentrm.datacat.catalog.domain.XtdText;
import de.bentrm.datacat.catalog.domain.XtdSubject;
import de.bentrm.datacat.catalog.repository.SymbolRepository;
import de.bentrm.datacat.catalog.repository.SubjectRepository;
import de.bentrm.datacat.catalog.repository.TextRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.SymbolRecordService;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.util.Assert;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.websocket.Decoder.Text;

@Slf4j
@Service
@Validated
@Transactional(readOnly = true)
public class SymbolRecordServiceImpl
        extends AbstractSimpleRecordServiceImpl<XtdSymbol, SymbolRepository>
        implements SymbolRecordService {

    private final SubjectRepository subjectRepository;
    private final TextRepository textRepository;

    public SymbolRecordServiceImpl(SessionFactory sessionFactory,
                                     SymbolRepository repository,
                                     SubjectRepository subjectRepository,
                                     TextRepository textRepository,
                                     CatalogCleanupService cleanupService) {
        super(XtdSymbol.class, sessionFactory, repository, cleanupService);
        this.subjectRepository = subjectRepository;
        this.textRepository = textRepository;
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.Symbol;
    }

    @Override
    public XtdSubject getSubject(XtdSymbol symbol) {
        Assert.notNull(symbol.getId(), "Symbol must be persistent.");
        final String subjectId = subjectRepository.findSubjectIdAssignedToSymbol(symbol.getId());
        if (subjectId == null) {
            return null;
        }
        final XtdSubject subject = subjectRepository.findById(subjectId).orElse(null);
        return subject;
    }

    @Transactional
    @Override
    public @NotNull XtdSymbol setRelatedRecords(@NotBlank String recordId,
                                                    @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {

        final XtdSymbol symbol = getRepository().findById(recordId, 0).orElseThrow();

        switch (relationType) {
            case Subject:
                if (symbol.getSubject() != null) {
                    throw new IllegalArgumentException("Symbol already has a subject assigned.");
                } else if (relatedRecordIds.size() != 1) {
                    throw new IllegalArgumentException("Exactly one subject must be assigned.");
                } else {
                    final XtdSubject subject = subjectRepository.findById(relatedRecordIds.get(0)).orElseThrow();
                    symbol.setSubject(subject);
                }
                break;
            case Symbol:
                if (symbol.getSymbol() != null) {
                    throw new IllegalArgumentException("Symbol already has a text assigned.");
                } else if (relatedRecordIds.size() != 1) {
                    throw new IllegalArgumentException("Exactly one text must be assigned.");
                } else {
                    final XtdText text = textRepository.findById(relatedRecordIds.get(0)).orElseThrow();
                    symbol.setSymbol(text);
                }
                break;
            default:
                    log.error("Unsupported relation type: {}", relationType);
                    break;
        }

        final XtdSymbol persistentSymbol = getRepository().save(symbol);
        log.trace("Updated relationship: {}", persistentSymbol);
        return persistentSymbol;
    }
}
