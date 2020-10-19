package de.bentrm.datacat.graphql.input;

import de.bentrm.datacat.catalog.domain.ToleranceType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SetToleranceInput {
    @NotBlank String id;
    @NotNull ToleranceType toleranceType;
    String lowerTolerance;
    String upperTolerance;
}
