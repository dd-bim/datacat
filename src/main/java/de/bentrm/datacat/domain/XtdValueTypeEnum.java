package de.bentrm.datacat.domain;

import java.util.List;

public enum XtdValueTypeEnum {
    Nil,
    XtdString,
    XtdNumber,
    XtdInteger,
    XtdReal,
    XtdBoolean,
    XtdLogical;

    public static final List<XtdValueTypeEnum> NUMERIC_VALUE_TYPES = List.of(
            XtdValueTypeEnum.XtdNumber,
            XtdValueTypeEnum.XtdInteger,
            XtdValueTypeEnum.XtdReal
    );

}
