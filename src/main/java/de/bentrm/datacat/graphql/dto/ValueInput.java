package de.bentrm.datacat.graphql.dto;

import de.bentrm.datacat.domain.XtdToleranceTypeEnum;
import de.bentrm.datacat.domain.XtdValueRoleEnum;
import de.bentrm.datacat.domain.XtdValueTypeEnum;
import de.bentrm.datacat.validation.NullOrNotEmpty;
import de.bentrm.datacat.validation.ToleranceComponentConstraint;

@ToleranceComponentConstraint
public class ValueInput extends RootInput implements ToleranceComponentInput {

    private XtdToleranceTypeEnum toleranceType;

    @NullOrNotEmpty
    private String lowerTolerance;

    @NullOrNotEmpty
    private String upperTolerance;

    private XtdValueRoleEnum valueRole;

    private XtdValueTypeEnum valueType;

    @NullOrNotEmpty
    private String nominalValue;

    public XtdToleranceTypeEnum getToleranceType() {
        return toleranceType;
    }

    public String getLowerTolerance() {
        return lowerTolerance;
    }

    public String getUpperTolerance() {
        return upperTolerance;
    }

    public XtdValueRoleEnum getValueRole() {
        return valueRole;
    }

    public XtdValueTypeEnum getValueType() {
        return valueType;
    }

    public String getNominalValue() {
        return nominalValue;
    }
}
