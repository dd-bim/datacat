package de.bentrm.datacat.catalog.service;

import de.bentrm.datacat.catalog.domain.XtdOrderedValue;
import de.bentrm.datacat.catalog.domain.XtdValue;
import de.bentrm.datacat.catalog.domain.XtdValueList;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public interface OrderedValueRecordService extends SimpleRecordService<XtdOrderedValue> {

    @NotNull XtdValue getValue(@NotNull XtdOrderedValue orderedValue);

    List<XtdValueList> getValueLists(@NotNull XtdOrderedValue orderedValue);
}
