package de.bentrm.datacat.graphql.dto;

import de.bentrm.datacat.domain.XtdValueRoleEnum;
import de.bentrm.datacat.domain.XtdValueTypeEnum;

public interface ValueComponentInput {
    XtdValueRoleEnum getValueRole();

    XtdValueTypeEnum getValueType();

    String getNominalValue();
}
