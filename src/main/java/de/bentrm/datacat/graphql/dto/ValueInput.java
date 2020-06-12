package de.bentrm.datacat.graphql.dto;

import de.bentrm.datacat.domain.XtdToleranceTypeEnum;
import de.bentrm.datacat.domain.XtdValueRoleEnum;
import de.bentrm.datacat.domain.XtdValueTypeEnum;
import de.bentrm.datacat.validation.NullOrNotEmpty;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotNull;

//@ToleranceComponentConstraint
public class ValueInput extends RootInput implements ToleranceComponentInput {

    @NotNull
    private XtdToleranceTypeEnum toleranceType;

    @NullOrNotEmpty
    private String lowerTolerance;

    @NullOrNotEmpty
    private String upperTolerance;

    @NotNull
    private XtdValueRoleEnum valueRole;

    @NotNull
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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("toleranceType", toleranceType)
                .append("lowerTolerance", lowerTolerance)
                .append("upperTolerance", upperTolerance)
                .append("valueRole", valueRole)
                .append("valueType", valueType)
                .append("nominalValue", nominalValue)
                .toString();
    }
}
