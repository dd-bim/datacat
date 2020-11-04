package de.bentrm.datacat.graphql.input;

import de.bentrm.datacat.catalog.domain.ToleranceType;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ToleranceInput {
    @NotNull ToleranceType toleranceType;
    String lowerTolerance;
    String upperTolerance;
}
