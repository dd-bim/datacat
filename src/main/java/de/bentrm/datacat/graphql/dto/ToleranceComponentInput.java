package de.bentrm.datacat.graphql.dto;

import de.bentrm.datacat.domain.XtdToleranceTypeEnum;

public interface ToleranceComponentInput {

    public XtdToleranceTypeEnum getToleranceType();

    public String getLowerTolerance();

    public String getUpperTolerance();
}
