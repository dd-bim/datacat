package de.bentrm.datacat.catalog.service;

import de.bentrm.datacat.catalog.domain.XtdExternalDocument;
import de.bentrm.datacat.catalog.domain.XtdLanguage;
import de.bentrm.datacat.catalog.domain.XtdConcept;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public interface ExternalDocumentRecordService extends SimpleRecordService<XtdExternalDocument> {

    List<XtdConcept> getConcepts(@NotNull XtdExternalDocument externalDocument);

    List<XtdLanguage> getLanguages(@NotNull XtdExternalDocument externalDocument);
}
