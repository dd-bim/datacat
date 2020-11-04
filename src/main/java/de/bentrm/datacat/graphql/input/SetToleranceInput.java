package de.bentrm.datacat.graphql.input;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SetToleranceInput {
    @NotBlank String id;
    @NotNull @Valid ToleranceInput tolerance;
}
