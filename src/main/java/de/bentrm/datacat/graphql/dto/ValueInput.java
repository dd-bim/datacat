package de.bentrm.datacat.graphql.dto;

import de.bentrm.datacat.domain.XtdToleranceTypeEnum;
import de.bentrm.datacat.domain.XtdValueRoleEnum;
import de.bentrm.datacat.domain.XtdValueTypeEnum;
import de.bentrm.datacat.validation.NullOrNotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
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
}
