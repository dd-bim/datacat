package de.bentrm.datacat.catalog.service;

import jakarta.validation.constraints.NotNull;

import de.bentrm.datacat.catalog.domain.XtdInterval;
import de.bentrm.datacat.catalog.domain.XtdValueList;

public interface IntervalRecordService extends SimpleRecordService<XtdInterval> {

    XtdValueList getMinimum(@NotNull XtdInterval interval);

    XtdValueList getMaximum(@NotNull XtdInterval interval);
}
