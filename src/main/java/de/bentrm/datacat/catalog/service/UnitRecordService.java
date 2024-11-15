package de.bentrm.datacat.catalog.service;

import de.bentrm.datacat.catalog.domain.XtdDimension;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdUnit;

import jakarta.validation.constraints.NotNull;
import java.util.List;
public interface UnitRecordService extends SimpleRecordService<XtdUnit> {

    List<XtdProperty> getProperties(@NotNull XtdUnit unit);

    XtdDimension getDimension(@NotNull XtdUnit unit);
}
