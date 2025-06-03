package de.bentrm.datacat.catalog.service;

import java.util.List;
import java.util.Optional;

import de.bentrm.datacat.catalog.domain.XtdOrderedValue;
import de.bentrm.datacat.catalog.domain.XtdValue;
import jakarta.validation.constraints.NotNull;

public interface ValueRecordService extends SimpleRecordService<XtdValue> {

    Optional<List<XtdOrderedValue>> getOrderedValues(@NotNull XtdValue value);
}
