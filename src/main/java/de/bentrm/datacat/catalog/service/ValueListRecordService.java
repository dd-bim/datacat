package de.bentrm.datacat.catalog.service;

import de.bentrm.datacat.catalog.domain.XtdOrderedValue;
import de.bentrm.datacat.catalog.domain.XtdProperty;
import de.bentrm.datacat.catalog.domain.XtdUnit;
import de.bentrm.datacat.catalog.domain.XtdValueList;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface ValueListRecordService extends SimpleRecordService<XtdValueList> {

    List<XtdOrderedValue> getOrderedValues(@NotNull XtdValueList valueList);

    List<XtdProperty> getProperties(@NotNull XtdValueList valueList);

    XtdUnit getUnit(@NotNull XtdValueList valueList);

}
