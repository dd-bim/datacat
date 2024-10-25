package de.bentrm.datacat.catalog.service;

import javax.validation.constraints.NotNull;

import de.bentrm.datacat.catalog.domain.XtdSymbol;
import de.bentrm.datacat.catalog.domain.XtdSubject;

public interface SymbolRecordService extends SimpleRecordService<XtdSymbol> {

    XtdSubject getSubject(@NotNull XtdSymbol symbol);
}
