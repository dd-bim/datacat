package de.bentrm.datacat.catalog.service.impl;

import de.bentrm.datacat.catalog.domain.CatalogRecordType;
import de.bentrm.datacat.catalog.domain.SimpleRelationType;
import de.bentrm.datacat.catalog.domain.XtdSymbol;
import de.bentrm.datacat.catalog.domain.XtdText;
import de.bentrm.datacat.catalog.domain.XtdSubject;
import de.bentrm.datacat.catalog.repository.SymbolRepository;
import de.bentrm.datacat.catalog.service.CatalogCleanupService;
import de.bentrm.datacat.catalog.service.SubjectRecordService;
import de.bentrm.datacat.catalog.service.SymbolRecordService;
import de.bentrm.datacat.catalog.service.TextRecordService;
import de.bentrm.datacat.catalog.service.dto.Relationships.SubjectDtoProjection;
import de.bentrm.datacat.catalog.service.dto.Relationships.SymbolDtoProjection;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Slf4j
@Service
@Validated
@Transactional(readOnly = true)
public class SymbolRecordServiceImpl
        extends AbstractSimpleRecordServiceImpl<XtdSymbol, SymbolRepository>
        implements SymbolRecordService {

            @Autowired
            private SubjectRecordService subjectRecordService;

            @Autowired
            private TextRecordService textRecordService;

    public SymbolRecordServiceImpl(Neo4jTemplate neo4jTemplate,
                                     SymbolRepository repository,
                                     CatalogCleanupService cleanupService) {
        super(XtdSymbol.class, neo4jTemplate, repository, cleanupService);
    }

    @Override
    public @NotNull CatalogRecordType getSupportedCatalogRecordType() {
        return CatalogRecordType.Symbol;
    }

    @Override
    public Optional<XtdSubject> getSubject(XtdSymbol symbol) {
        Assert.notNull(symbol.getId(), "Symbol must be persistent.");
        final String subjectId = getRepository().findSubjectIdAssignedToSymbol(symbol.getId());
        if (subjectId == null) {
            return null;
        }
        final Optional<XtdSubject> subject = subjectRecordService.findByIdWithDirectRelations(subjectId);
        // final Optional<XtdSubject> subject = getRepository().findSubjectAssignedToSymbol(symbol.getId());
        return subject;
    }

    @Override
    public Optional<XtdText> getSymbolText(XtdSymbol symbol) {
        Assert.notNull(symbol.getId(), "Symbol must be persistent.");
        final String textId = getRepository().findSymbolText(symbol.getId());
        if (textId == null) {
            return null;
        }
        final Optional<XtdText> text = textRecordService.findByIdWithDirectRelations(textId);
        return text;
    }

    @Transactional
    @Override
    public @NotNull XtdSymbol setRelatedRecords(@NotBlank String recordId,
                                                    @NotEmpty List<@NotBlank String> relatedRecordIds, @NotNull SimpleRelationType relationType) {

        final XtdSymbol symbol = getRepository().findByIdWithDirectRelations(recordId).orElseThrow(() -> new IllegalArgumentException("No record with id " + recordId + " found."));

        switch (relationType) {
            case Subject -> {
                if (symbol.getSubject() != null) {
                    throw new IllegalArgumentException("Symbol already has a subject assigned.");
                } else if (relatedRecordIds.size() != 1) {
                    throw new IllegalArgumentException("Exactly one subject must be assigned.");
                } else {
                    final XtdSubject subject = subjectRecordService.findByIdWithDirectRelations(relatedRecordIds.get(0)).orElseThrow(() -> new IllegalArgumentException("No record with id " + relatedRecordIds.get(0) + " found."));
                    symbol.setSubject(subject);
                }
                neo4jTemplate.saveAs(symbol, SubjectDtoProjection.class);
            }
            case Symbol -> {
                if (symbol.getSymbol() != null) {
                    throw new IllegalArgumentException("Symbol already has a text assigned.");
                } else if (relatedRecordIds.size() != 1) {
                    throw new IllegalArgumentException("Exactly one text must be assigned.");
                } else {
                    final XtdText text = textRecordService.findByIdWithDirectRelations(relatedRecordIds.get(0)).orElseThrow(() -> new IllegalArgumentException("No record with id " + relatedRecordIds.get(0) + " found."));
                    symbol.setSymbol(text);
                }
                neo4jTemplate.saveAs(symbol, SymbolDtoProjection.class);
            }
            default -> log.error("Unsupported relation type: {}", relationType);
        }

        log.trace("Updated relationship: {}", symbol);
        return symbol;
    }
}
