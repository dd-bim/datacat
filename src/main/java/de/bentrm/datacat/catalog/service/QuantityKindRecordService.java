package de.bentrm.datacat.catalog.service;

import java.util.List;

import jakarta.validation.constraints.NotNull;

import de.bentrm.datacat.catalog.domain.XtdDimension;
import de.bentrm.datacat.catalog.domain.XtdQuantityKind;
import de.bentrm.datacat.catalog.domain.XtdUnit;

public interface QuantityKindRecordService extends SimpleRecordService<XtdQuantityKind> {

    List<XtdUnit> getUnits(@NotNull XtdQuantityKind quantityKind);

    XtdDimension getDimension(@NotNull XtdQuantityKind quantityKind);
}
