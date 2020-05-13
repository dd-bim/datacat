package de.bentrm.datacat.graphql.dto;

import de.bentrm.datacat.domain.XtdToleranceTypeEnum;

public interface ToleranceComponentInput {

    XtdToleranceTypeEnum getToleranceType();

    String getLowerTolerance();

    String getUpperTolerance();
}
