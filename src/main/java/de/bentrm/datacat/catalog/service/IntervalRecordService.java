package de.bentrm.datacat.catalog.service;

import jakarta.validation.constraints.NotNull;

import java.util.Optional;

import de.bentrm.datacat.catalog.domain.XtdInterval;
import de.bentrm.datacat.catalog.domain.XtdValueList;

public interface IntervalRecordService extends SimpleRecordService<XtdInterval> {

    Optional<XtdValueList> getMinimum(@NotNull XtdInterval interval);

    Optional<XtdValueList> getMaximum(@NotNull XtdInterval interval);
}
