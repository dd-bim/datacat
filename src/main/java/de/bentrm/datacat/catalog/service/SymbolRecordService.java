package de.bentrm.datacat.catalog.service;

import jakarta.validation.constraints.NotNull;
import de.bentrm.datacat.catalog.domain.XtdSubject;
import de.bentrm.datacat.catalog.domain.XtdSymbol;
import de.bentrm.datacat.catalog.domain.XtdText;

import java.util.Optional;

public interface SymbolRecordService extends SimpleRecordService<XtdSymbol> {

    Optional<XtdSubject> getSubject(@NotNull XtdSymbol symbol);

    Optional<XtdText> getSymbolText(@NotNull XtdSymbol symbol);
}
